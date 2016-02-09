package sexy.code;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Parts of the code were copied from OkHttp and modified.
 */
public abstract class RequestBody {

    /** Returns the Content-Type header for this body. */
    public abstract MediaType contentType();

    public abstract void writeTo(final BufferedOutputStream os) throws IOException;

    public long contentLength() throws IOException {
        return -1;
    }

    /**
     * Returns a new request body that transmits {@code content}. If {@code contentType} is non-null
     * and lacks a charset, this will use UTF-8.
     */
    public static RequestBody create(MediaType contentType, String content) {
        Charset charset = Util.UTF_8;
        if (contentType != null) {
            charset = contentType.charset();
            if (charset == null) {
                charset = Util.UTF_8;
                contentType = MediaType.parse(contentType + "; charset=utf-8");
            }
        }
        byte[] bytes = content.getBytes(charset);
        return create(contentType, bytes);
    }

    /** Returns a new request body that transmits {@code content}. */
    public static RequestBody create(final MediaType contentType, final byte[] content) {
        return create(contentType, content, 0, content.length);
    }

    /** Returns a new request body that transmits {@code content}. */
    public static RequestBody create(final MediaType contentType, final byte[] content, final int offset, final int byteCount) {
        if (content == null) {
            throw new NullPointerException("content == null");
        }
        Util.checkOffsetAndCount(content.length, offset, byteCount);
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() {
                return byteCount;
            }

            @Override
            public void writeTo(BufferedOutputStream os) throws IOException {
                os.write(content, offset, byteCount);
            }
        };
    }

    /** Returns a new request body that transmits the content of {@code file}. */
    public static RequestBody create(final MediaType contentType, final File file) {
        if (file == null) {
            throw new NullPointerException("content == null");
        }

        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() {
                return file.length();
            }

            @Override
            public void writeTo(BufferedOutputStream os) throws IOException {
                InputStream fileInputStream = new BufferedInputStream(new FileInputStream(file));

                try {
                    int d;
                    while ((d = fileInputStream.read()) != -1) {
                        os.write(d);
                    }
                } finally {
                    Util.closeQuietly(fileInputStream);
                }
            }
        };
    }
}
