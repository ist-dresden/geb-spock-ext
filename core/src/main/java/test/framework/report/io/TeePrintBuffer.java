package test.framework.report.io;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class TeePrintBuffer extends PrintStream {

    private PrintStream wrappedStream;
    private ByteArrayOutputStream printBuffer;

    TeePrintBuffer(PrintStream wrappedStream, ByteArrayOutputStream printBuffer) {
        super(new TeeOutputStream(wrappedStream, printBuffer));
        this.wrappedStream = wrappedStream;
        this.printBuffer = printBuffer;
    }

    public String toString() {
        String result = "";
        try {
            result = printBuffer.toString("UTF-8");
        } catch (UnsupportedEncodingException ueex) {
            ueex.printStackTrace(wrappedStream);
        }
        return result;
    }

    public void reset() {
        printBuffer.reset();
    }
}