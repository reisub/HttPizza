package sexy.code;

import java.io.BufferedOutputStream;
import java.io.IOException;

public abstract class RequestBody {

    public abstract String contentType();

    public abstract void writeTo(final BufferedOutputStream os) throws IOException;

    public long contentLength() throws IOException {
        return -1;
    }

}
