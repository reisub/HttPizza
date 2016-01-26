package sexy.code;

import java.io.Closeable;
import java.nio.charset.Charset;

public final class Util {

    /**
     * A cheap and type-safe constant for the UTF-8 Charset.
     */
    public static final Charset UTF_8 = Charset.forName("UTF-8");

    private Util() {
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (RuntimeException rethrown) {
            throw rethrown;
        } catch (Exception ignore) {

        }
    }
}
