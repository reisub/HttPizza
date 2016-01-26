package sexy.code;

import org.junit.After;
import org.junit.Before;

import java.io.IOException;

import okhttp3.mockwebserver.MockWebServer;

public class MockWebServerTest {

    protected MockWebServer server;

    protected HttPizza client;

    @Before
    public void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        client = new HttPizza();
    }

    @After
    public void tearDown() {
        try {
            server.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected HttpUrl fullUrl(String url) {
        return convertHttpUrl(server.url(url));
    }

    public static HttpUrl convertHttpUrl(okhttp3.HttpUrl okHttpUrl) {
        return HttpUrl.parse(okHttpUrl.toString());
    }

    public static String urlPathWithQuery(HttpUrl url) {
        String queryString = url.encodedQuery() == null ? "" : "?" + url.encodedQuery();
        return url.encodedPath() + queryString;
    }
}
