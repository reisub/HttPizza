package sexy.code;

import java.util.HashMap;
import java.util.Map;

public class Request {

    public static final String METHOD_GET = "GET";

    public static final String METHOD_POST = "POST";

    public static final String METHOD_PUT = "PUT";

    public static final String METHOD_DELETE = "DELETE";

    public static final String METHOD_HEAD = "HEAD";

    public static final String METHOD_OPTIONS = "OPTIONS";

    public static final String METHOD_TRACE = "TRACE";

    private HttpUrl url;

    private String method;

    private Map<String, String> headers;

    private RequestBody body;

    Request(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = builder.headers;
        this.body = builder.body;
    }

    public HttpUrl getUrl() {
        return url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getMethod() {
        return method;
    }

    public RequestBody getBody() {
        return body;
    }

    void setBody(RequestBody body) {
        this.body = body;
    }

    public static class Builder {

        private HttpUrl url;

        private String method;

        private Map<String, String> headers;

        private RequestBody body;

        Builder() {
            method = METHOD_GET;
            headers = new HashMap<>();
        }

        public Builder url(HttpUrl url) {
            if (url == null) {
                throw new IllegalArgumentException("url == null");
            }
            this.url = url;
            return this;
        }

        public Builder url(String url) {
            if (url == null) {
                throw new IllegalArgumentException("url == null");
            }
            HttpUrl parsed = HttpUrl.parse(url);
            if (parsed == null) {
                throw new IllegalArgumentException("unexpected url: " + url);
            }
            return url(parsed);
        }

        public Builder get() {
            return method(METHOD_GET, null);
        }

        public Builder post(RequestBody body) {
            return method(METHOD_POST, body);
        }

        /**
         * HttpUrlConnection does not support sending request body with DELETE request
         */
        public Builder delete() {
            return method(METHOD_DELETE, null);
        }

        public Builder put(RequestBody body) {
            return method(METHOD_PUT, body);
        }

        public Builder addHeader(String name, String value) {
            headers.put(name, value);
            return this;
        }

        public Builder method(String method, RequestBody body) {
            this.method = method;
            this.body = body;
            return this;
        }

        public Request build() {
            if (url == null) {
                throw new IllegalStateException("url == null");
            }
            return new Request(this);
        }
    }
}
