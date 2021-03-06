package test.framework.report

import org.spockframework.runtime.model.ErrorInfo
import test.framework.Spec
import test.framework.geb.ExtendedConfiguration
import test.framework.report.io.TeePrintBuffer

public class Reporting {

    private static Reporting _instance

    public static Reporting setUp(Object owner) {
        if (!_instance) {
            _instance = new Reporting(owner)
            _instance.setUp()
        }
        _instance
    }

    public static void tearDown(Object owner) {
        if (_instance && _instance._owner == owner) {
            _instance.tearDown()
            _instance = null
        }
    }

    private Object _owner

    private ByteArrayOutputStream _sniffer
    public PrintStream systemOut
    public PrintStream systemErr

    File getReportBase() {
        new File('test')
    }

    File getTemplateBase() {
        new File('test')
    }

    public void indent(int depth = -1) {
        if (depth < 0) {
            depth = currentSection ? currentSection.length * 2 : 0
        }
        for (int i = 0; i < depth; i++) write("  ")
    }

    public void description(String text) {
        indent()
        write "<div class=\"description\">"
        write text
        write "</div>"
    }

    public void info(String text) {
        indent()
        write "<p>"
        write text
        write "</p>"
    }

    public void image(File file, String type, String group) {
        String base = reportBase.absolutePath
        String path = file.absolutePath
        if (path.startsWith(base)) {
            path = path.substring(base.length() + 1)
        }
        write "<div class=\"${type} image link\">"
        write "<h5>${type}</h5>"
        write "<a href=\"${path}\" rel=\"${group}\\\"><img src=\"${path}\" title=\"${type} - ${path}\" /></a>"
        write "</div>"
    }

    public void text(String text) {
        writeln(text)
    }

    public void output() {
        String content = sniffed.trim()
        if (!content.isAllWhitespace()) {
            indent()
            write "<div class=\"output\">"
            write content
            write "</div>"
        }
        reset()
    }

    private String[] currentSection

    private Result _result

    class Result {

        public int _successCount
        public int _failureCount
        public boolean running = false;

        private Map<String, Result> _results

        public Result() {
            _results = new HashMap<String, Result>()
        }

        public Result get(String key) {
            Result value = _results.get(key)
            if (!value) {
                value = new Result();
                _results.put(key, value)
            }
            value
        }

        public void success() {
            if (running) {
                running = false
                _successCount++
            }
        }

        public void failure() {
            if (running) {
                running = false
                _failureCount++
            }
        }

        public int getSuccessCount() {
            int value = this._successCount
            for (Map.Entry<String, Result> entry : _results) {
                value += entry.value.getSuccessCount()
            }
            value
        }

        public int getFailureCount() {
            int value = this._failureCount
            for (Map.Entry<String, Result> entry : _results) {
                value += entry.value.getFailureCount()
            }
            value
        }

        public String getSummary() {
            getFailureCount() > 0 ? "failed" : getSuccessCount() > 0 ? "passed" : "skipped"
        }

        public Map getProperties() {
            ['summary': getSummary(), 'successCount': getSuccessCount(), 'failureCount': getFailureCount()]
        }
    }

    private File reportRoot

    private File reportFile
    private PrintStream reportStream
    private FileOutputStream reportFileStream

    protected Reporting(Object owner) {
        _owner = owner
        _result = new Result()
    }

    public static Reporting getInstance() {
        _instance
    }

    /**
     * Retrieves the result instance for a given test section.
     */
    public Result getResult(int index = currentSection.length - 1) {
        Result result = _result
        for (int i = 0; i <= index; i++) {
            result = result.get(currentSection[i])
        }
        result
    }

    public void adjustSection(Object context, String feature) {
        adjustSection(getContextPath(context) + "/" + getContextPath(feature))
    }

    public void adjustSection(Object context) {
        adjustSection(getContextPath(context))
    }

    public void adjustSection(String pathString) {
        String[] path = pathString.empty ? new String[0] : pathString.split('/')
        if (!currentSection) {
            currentSection = new String[0]
        }
        int index = 0;
        while (index < Math.min(currentSection.length, path.length) &&
                currentSection[index].equals(path[index])) {
            index++
        }
        for (int i = currentSection.length - 1; i >= index; i--) {
            write("</div>")
            Result segmentResult = getResult(i);
            result(segmentResult);
            indent(1 + i * 2);
            writeln("</li>")
            if (!(index + 1 == path.length && index + 1 == currentSection.length)) {
                indent(i * 2)
                writeln("</ul>")
            }
        }
        output()
        for (int i = index; i < path.length; i++) {
            String label = path[i]
            if (!(index + 1 == path.length && index + 1 == currentSection.length)) {
                indent(i * 2);
                writeln("<ul class=\"section\">")
            }
            indent(1 + i * 2);
            writeln("<li><h4 class=\"path\"><span class=\"toggle\"></span>${label}</h4><div class=\"report\">")
        }
        currentSection = path
    }

    protected String getContextPath(Object context) {
        String path = context instanceof String ? (String) context :
                (context instanceof Class ? ((Class) context).name : context?.getClass().name);
        path = path.replace('.', '/').toLowerCase()
    }

    public void result(Result result) {
        writeTemplate("result.html", result.properties)
    }

    public void success(String text = null) {
        getResult().success()
        if (text && !text.empty) {
            info text
        }
    }

    public void error(String text) {
        getResult().failure()
        info text
    }

    public void error(ErrorInfo info) {
        getResult().failure()
        writeTemplate("error.html", info.properties)
    }

    public String getSniffed() {
        _sniffer.toString("UTF-8")
    }

    public void reset() {
        _sniffer.reset()
    }

    protected void write(String text) {
        reportStream.print(text)
    }

    public void writeln(String text = "") {
        reportStream.println(text)
    }

    protected void setUp() {
        _sniffer = new ByteArrayOutputStream()
        System.setOut(new TeePrintBuffer(systemOut = System.out, _sniffer))
        System.setErr(new TeePrintBuffer(systemErr = System.err, _sniffer))
        //systemOut.println("Reporting.setUp()")
        reportRoot = new File('test')
        reportRoot.mkdirs()
        reportFile = new File(reportRoot, 'report.html')
        reportFileStream = new FileOutputStream(reportFile)
        reportStream = new PrintStream(reportFileStream)
        writeln "<html>"
        writeln "<head>"
        writeln "<style>"
        writeSnippet "style.css"
        writeln "</style>"
        writeSnippet "libs.html"
        writeln "<script>"
        writeSnippet "script.js"
        writeln "</script>"
        writeln "</head>"
        writeln "<body>"
        writeln "  <div id=\"sections\">"
    }

    protected void tearDown() {
        adjustSection("")
        writeTemplate "tail.html"
        reportStream.close()
        reportFileStream.close()
        //systemOut.println("Reporting.tearDown()")
        System.setOut(systemOut)
        System.setErr(systemErr)
    }

    public writeTemplate(String templateFileName, Map binding = new HashMap()) {
        def engine = new groovy.text.SimpleTemplateEngine()
        def resource = getClass().getResource('/report/template/' + templateFileName)
        def template = engine.createTemplate(resource.text)
        def result = template.make(binding)
        reportStream.print(result)
    }

    public writeSnippet(String snippetFileName) {
        def resource = getClass().getResource('/report/template/' + snippetFileName)
        reportStream.print(resource.text)
    }
}
