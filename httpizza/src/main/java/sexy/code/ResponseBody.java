package sexy.code;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.net.HttpURLConnection;

public class ResponseBody implements Closeable {

    private final String contentType;

    private final long contentLength;

    private final BufferedInputStream inputStream;

    private HttpURLConnection connection;

    ResponseBody(final String contentType, final long contentLength, final BufferedInputStream inputStream, HttpURLConnection connection) {
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.inputStream = inputStream;
        this.connection = connection;
    }

    @Override
    public void close() {
        Util.closeQuietly(inputStream);
        connection.disconnect();
        connection = null;
    }

    public String contentType() {
        return contentType;
    }

    public BufferedInputStream byteStream() {
        return inputStream;
    }

    public long contentLength() {
        return contentLength;
    }

    public byte[] bytes() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int n = 0;
            while (-1 != (n = inputStream.read(buffer))) {
                os.write(buffer, 0, n);
            }
            return os.toByteArray();
        } finally {
            Util.closeQuietly(os);
            Util.closeQuietly(this);
        }
    }

    public final String string() throws IOException {
        return new String(bytes(), Util.UTF_8);
    }
}
