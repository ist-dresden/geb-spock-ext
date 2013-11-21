package test.framework.report

import org.spockframework.runtime.model.ErrorInfo
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

    private String[] currentSection

    private Result _result

    class Result {

        public int _successCount
        public int _failureCount

        private Map<String, Result> _results

        public Result() {
            _results = new HashMap<String, Result>()
        }

        public Result getResult(String[] path, int index = 0) {
            Result result = this
            if (path && index < path.length) {
                result = _results.get(path[index])
                if (!result) {
                    result = new Result()
                    _results.put(path[index], result)
                }
                result = result.getResult(path, index + 1)
            }
            result
        }

        public void success() {
            _successCount++;
        }

        public void failure() {
            _failureCount++;
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

        public Map getProperties() {
            ['successCount': getSuccessCount(), 'failureCount': getFailureCount()]
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

    public Result getResult(String[] path = currentSection) {
        _result.getResult(path)
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
            result(getResult())
            indent(1 + i * 2);
            writeln("</li>")
            if (!(index + 1 == path.length && index + 1 == currentSection.length)) {
                indent(i * 2)
                writeln("</ul>")
            }
        }
        for (int i = index; i < path.length; i++) {
            String label = path[i]
            if (!(index + 1 == path.length && index + 1 == currentSection.length)) {
                indent(i * 2);
                writeln("<ul class=\"section\">")
            }
            indent(1 + i * 2);
            writeln("<li><h4 class=\"path\">${label}</h4>")
        }
        currentSection = path
    }

    protected String getContextPath(Object context) {
        String path = context instanceof String ? (String) context :
            (context instanceof Class ? ((Class) context).name : context?.getClass().name);
        path = path.replace('.', '/').toLowerCase()
    }

    public void indent(int depth = -1) {
        if (depth < 0) {
            depth = currentSection ? currentSection.length * 2 : 0
        }
        for (int i = 0; i < depth; i++) write("  ")
    }

    public void result(Result result) {
        writeTemplate("result.html", result.properties)
    }

    public void success(String text = null) {
        getResult().success()
        if (text && !text.empty) {
            info(text)
        }
    }

    public void error(String text) {
        getResult().failure()
        writeln(text)
    }

    public void error(ErrorInfo info) {
        getResult().failure()
        writeTemplate("error.html", info.properties)
    }

    public void info(String text) {
        writeln(text)
    }

    public void text(String text) {
        write(text)
    }

    protected void write(String text) {
        reportStream.print(text)
    }

    public void writeln(String text = "") {
        reportStream.println(text)
    }

    public void reset() {
        _sniffer.reset()
    }

    public String getSniffed() {
        _sniffer.toString("UTF-8")
    }

    protected void setUp() {
        _sniffer = new ByteArrayOutputStream()
        System.setOut(new TeePrintBuffer(systemOut = System.out, _sniffer))
        System.setErr(new TeePrintBuffer(systemErr = System.err, _sniffer))
        systemOut.println("Reporting.setUp()")
        reportRoot = new File('test')
        reportRoot.mkdirs()
        reportFile = new File(reportRoot, 'report.html')
        reportFileStream = new FileOutputStream(reportFile)
        reportStream = new PrintStream(reportFileStream)
        writeTemplate("htmlProlog.html")
    }

    protected void tearDown() {
        adjustSection("")
        writeTemplate "htmlEpilog.html"
        reportStream.close()
        reportFileStream.close()
        systemOut.println("Reporting.tearDown()")
        System.setOut(systemOut)
        System.setErr(systemErr)
    }

    public writeTemplate(String templateFileName, Map binding = new HashMap()) {
        def engine = new groovy.text.SimpleTemplateEngine()
        def resource = getClass().getResource('/report/template/' + templateFileName)
        systemOut.println "template: ${resource}"
        def template = engine.createTemplate(resource.text)
        def result = template.make(binding)
        reportStream.print(result)
    }
}
