package test.framework.report.io;

import java.io.IOException;
import java.io.OutputStream;

public class TeeOutputStream extends OutputStream {

    private OutputStream wrappedStream;
    private OutputStream secondStream;

    public TeeOutputStream(OutputStream wrappedStream, OutputStream secondStream) {
        this.wrappedStream = wrappedStream;
        this.secondStream = secondStream;
    }

    public OutputStream getWrappedStream() {
        return wrappedStream;
    }

    public OutputStream getSecondStream() {
        return secondStream;
    }

    @Override
    public void write(int b) throws IOException {
        wrappedStream.write(b);
        secondStream.write(b);
    }
}
