package sexy.code;

import org.junit.Test;

import java.net.HttpURLConnection;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CallTest {

    public static final MediaType MEDIA_TYPE_PLAIN_TEXT = MediaType.parse("text/plain; charset=utf-8");

    private CallTestCase getTestCase = new CallTestCase() {
        @Override
        protected Request prepareRequest(HttpUrl url) throws Exception {
            MockResponse mockResponse = new MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody("body")
                    .addHeader("Content-Type: text/plain");
            server.enqueue(mockResponse);

            return client.newRequest()
                    .url(url)
                    .header("User-Agent", "UnitTest")
                    .get()
                    .build();
        }

        @Override
        protected void verifyRequest(HttpUrl url, RecordedRequest recordedRequest) throws Exception {
            assertEquals("GET", recordedRequest.getMethod());
            assertEquals(urlPathWithQuery(url), recordedRequest.getPath());
            assertEquals(0, recordedRequest.getBody().size());
            assertEquals("UnitTest", recordedRequest.getHeader("User-Agent"));
            assertNull(recordedRequest.getHeader("Content-Length"));
        }

        @Override
        protected void verifyResponse(HttpUrl url, Response response) throws Exception {
            assertEquals(200, response.code());
            assertEquals("text/plain", response.headers().get("Content-Type").get(0));
            assertEquals("body", response.body().string());
            assertTrue(response.isSuccessful());
        }
    };

    @Test
    public void get() throws Exception {
        getTestCase.test("/path?query1=1&query2=2");
        getTestCase.testAsync("/path?query1=1&query2=2");
    }

    private CallTestCase postTestCase = new CallTestCase() {
        @Override
        protected Request prepareRequest(HttpUrl url) throws Exception {
            MockResponse mockResponse = new MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody("body")
                    .addHeader("Content-Type: text/plain");
            server.enqueue(mockResponse);

            return client.newRequest()
                    .url(url)
                    .header("User-Agent", "UnitTest")
                    .post(RequestBody.create(MEDIA_TYPE_PLAIN_TEXT, "requestBody"))
                    .build();
        }

        @Override
        protected void verifyRequest(HttpUrl url, RecordedRequest recordedRequest) throws Exception {
            assertEquals("POST", recordedRequest.getMethod());
            assertEquals(urlPathWithQuery(url), recordedRequest.getPath());
            assertEquals(11, recordedRequest.getBody().size());
            assertEquals("11", recordedRequest.getHeader("Content-Length"));
            assertEquals("text/plain; charset=utf-8", recordedRequest.getHeader("Content-Type"));
            assertEquals("UnitTest", recordedRequest.getHeader("User-Agent"));
        }

        @Override
        protected void verifyResponse(HttpUrl url, Response response) throws Exception {
            assertEquals(200, response.code());
            assertEquals("text/plain", response.headers().get("Content-Type").get(0));
            assertEquals("body", response.body().string());
            assertTrue(response.isSuccessful());
        }
    };

    @Test
    public void post() throws Exception {
        postTestCase.test("/path?query1=1&query2=2");
        postTestCase.testAsync("/path?query1=1&query2=2");
    }

    private CallTestCase zeroLengthPostTestCase = new CallTestCase() {
        @Override
        protected Request prepareRequest(HttpUrl url) throws Exception {
            MockResponse mockResponse = new MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody("body")
                    .addHeader("Content-Type: text/plain");
            server.enqueue(mockResponse);

            return client.newRequest()
                    .url(url)
                    .header("User-Agent", "UnitTest")
                    .post(RequestBody.create(MEDIA_TYPE_PLAIN_TEXT, ""))
                    .build();
        }

        @Override
        protected void verifyRequest(HttpUrl url, RecordedRequest recordedRequest) throws Exception {
            assertEquals("POST", recordedRequest.getMethod());
            assertEquals(urlPathWithQuery(url), recordedRequest.getPath());
            assertEquals(0, recordedRequest.getBody().size());
            assertEquals(null, recordedRequest.getHeader("Content-Length"));
            assertEquals("UnitTest", recordedRequest.getHeader("User-Agent"));
        }

        @Override
        protected void verifyResponse(HttpUrl url, Response response) throws Exception {
            assertEquals(200, response.code());
            assertEquals("text/plain", response.headers().get("Content-Type").get(0));
            assertEquals("body", response.body().string());
            assertTrue(response.isSuccessful());
        }
    };

    @Test
    public void postZeroLength() throws Exception {
        zeroLengthPostTestCase.test("/path?query1=1&query2=2");
        zeroLengthPostTestCase.testAsync("/path?query1=1&query2=2");
    }

    private CallTestCase putTestCase = new CallTestCase() {
        @Override
        protected Request prepareRequest(HttpUrl url) throws Exception {
            MockResponse mockResponse = new MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody("body")
                    .addHeader("Content-Type: text/plain");
            server.enqueue(mockResponse);

            return client.newRequest()
                    .url(url)
                    .header("User-Agent", "UnitTest")
                    .put(RequestBody.create(MEDIA_TYPE_PLAIN_TEXT, "requestBody"))
                    .build();
        }

        @Override
        protected void verifyRequest(HttpUrl url, RecordedRequest recordedRequest) throws Exception {
            assertEquals("PUT", recordedRequest.getMethod());
            assertEquals(urlPathWithQuery(url), recordedRequest.getPath());
            assertEquals(11, recordedRequest.getBody().size());
            assertEquals("11", recordedRequest.getHeader("Content-Length"));
            assertEquals("text/plain; charset=utf-8", recordedRequest.getHeader("Content-Type"));
            assertEquals("UnitTest", recordedRequest.getHeader("User-Agent"));
        }

        @Override
        protected void verifyResponse(HttpUrl url, Response response) throws Exception {
            assertEquals(200, response.code());
            assertEquals("text/plain", response.headers().get("Content-Type").get(0));
            assertEquals("body", response.body().string());
            assertTrue(response.isSuccessful());
        }
    };

    @Test
    public void put() throws Exception {
        putTestCase.test("/path?query1=1&query2=2");
        putTestCase.testAsync("/path?query1=1&query2=2");
    }

    private CallTestCase deleteTestCase = new CallTestCase() {
        @Override
        protected Request prepareRequest(HttpUrl url) throws Exception {
            MockResponse mockResponse = new MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody("body")
                    .addHeader("Content-Type: text/plain");
            server.enqueue(mockResponse);

            return client.newRequest()
                    .url(url)
                    .header("User-Agent", "UnitTest")
                    .delete()
                    .build();
        }

        @Override
        protected void verifyRequest(HttpUrl url, RecordedRequest recordedRequest) throws Exception {
            assertEquals("DELETE", recordedRequest.getMethod());
            assertEquals(urlPathWithQuery(url), recordedRequest.getPath());
            assertEquals(0, recordedRequest.getBody().size());
            assertEquals(null, recordedRequest.getHeader("Content-Length"));
            assertNull(recordedRequest.getHeader("Content-Type"));
            assertEquals("UnitTest", recordedRequest.getHeader("User-Agent"));
        }

        @Override
        protected void verifyResponse(HttpUrl url, Response response) throws Exception {
            assertEquals(200, response.code());
            assertEquals("text/plain", response.headers().get("Content-Type").get(0));
            assertEquals("body", response.body().string());
            assertTrue(response.isSuccessful());
        }
    };

    @Test
    public void delete() throws Exception {
        deleteTestCase.test("/path?query1=1&query2=2");
        deleteTestCase.testAsync("/path?query1=1&query2=2");
    }

    private CallTestCase headTestCase = new CallTestCase() {
        @Override
        protected Request prepareRequest(HttpUrl url) throws Exception {
            MockResponse mockResponse = new MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_OK)
                    .addHeader("Content-Type: text/plain");
            server.enqueue(mockResponse);

            return client.newRequest()
                    .url(url)
                    .header("User-Agent", "UnitTest")
                    .method("HEAD", null)
                    .build();
        }

        @Override
        protected void verifyRequest(HttpUrl url, RecordedRequest recordedRequest) throws Exception {
            assertEquals("HEAD", recordedRequest.getMethod());
            assertEquals(urlPathWithQuery(url), recordedRequest.getPath());
            assertEquals(0, recordedRequest.getBody().size());
            assertEquals(null, recordedRequest.getHeader("Content-Length"));
            assertNull(recordedRequest.getHeader("Content-Type"));
            assertEquals("UnitTest", recordedRequest.getHeader("User-Agent"));
        }

        @Override
        protected void verifyResponse(HttpUrl url, Response response) throws Exception {
            assertEquals(200, response.code());
            assertEquals("text/plain", response.headers().get("Content-Type").get(0));
            assertTrue(response.isSuccessful());
        }
    };

    @Test
    public void head() throws Exception {
        headTestCase.test("/path?query1=1&query2=2");
        headTestCase.testAsync("/path?query1=1&query2=2");
    }
}
