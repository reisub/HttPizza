package sexy.code;

import java.io.IOException;

import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public abstract class CallTestCase {

    protected HttPizza client;

    protected MockWebServer server;

    protected abstract Request prepareRequest(HttpUrl url) throws Exception;

    protected abstract void verifyRequest(HttpUrl url, RecordedRequest recordedRequest) throws Exception;

    protected abstract void verifyResponse(HttpUrl url, Response response) throws Exception;

    protected void setUp() throws Exception {
        server = new MockWebServer();
        server.start();
    }

    protected void tearDown() throws Exception {
        try {
            client = null;
            server.shutdown();
            server = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected HttpUrl fullUrl(String path) {
        return convertHttpUrl(server.url(path));
    }

    public static HttpUrl convertHttpUrl(okhttp3.HttpUrl okHttpUrl) {
        return HttpUrl.parse(okHttpUrl.toString());
    }

    public static String urlPathWithQuery(HttpUrl url) {
        String queryString = url.encodedQuery() == null ? "" : "?" + url.encodedQuery();
        return url.encodedPath() + queryString;
    }

    public void test(String path) throws Exception {
        setUp();

        client = new HttPizza();
        HttpUrl url = fullUrl(path);

        Request request = prepareRequest(url);
        Response response = client.newCall(request).execute();

        verifyRequest(url, server.takeRequest());
        verifyResponse(url, response);

        tearDown();
    }

    public void testAsync(String path) throws Exception {
        setUp();

        client = new HttPizza.Builder()
                .httpExecutor(new SynchronousExecutor())
                .callbackExecutor(new SynchronousExecutor())
                .build();
        final HttpUrl url = fullUrl(path);

        Request request = prepareRequest(url);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Response response) {
                try {
                    verifyResponse(url, response);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                throw new RuntimeException(t);
            }
        });

        verifyRequest(url, server.takeRequest());

        tearDown();
    }
}
