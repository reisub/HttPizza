package sexy.code;

import java.lang.reflect.Type;
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

    private RequestConvertTask<?> pendingTask;

    Request(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = builder.headers;
        this.body = builder.body;
        this.pendingTask = builder.task;
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

    RequestConvertTask<?> getPendingTask() {
        return pendingTask;
    }

    public static class Builder {

        private final ConverterProvider converterProvider;

        private HttpUrl url;

        private String method;

        private Map<String, String> headers;

        private RequestBody body;

        private RequestConvertTask<?> task;

        Builder(final ConverterProvider converterProvider) {
            this.converterProvider = converterProvider;
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

        public <T> Builder post(T body, Type type) {
            return method(METHOD_POST, body, type);
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

        public <T> Builder put(T body, Type type) {
            return method(METHOD_PUT, body, type);
        }

        public Builder addHeader(String name, String value) {
            headers.put(name, value);
            return this;
        }

        public Builder method(String method, RequestBody body) {
            this.method = method;
            this.body = body;
            this.task = null;
            return this;
        }

        public <T> Builder method(String method, T body, Type type) {
            this.method = method;
            this.body = null;
            this.task = new RequestConvertTask<>(body, converterProvider.requestConverter(type));
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
