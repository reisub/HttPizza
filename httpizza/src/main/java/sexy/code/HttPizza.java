package sexy.code;

import java.util.concurrent.Executor;

public class HttPizza {

    private final HttpConfig httpConfig;

    HttPizza(HttpConfig config) {
        this.httpConfig = config;
    }

    public HttPizza() {
        this(new HttpConfig());
    }

    public Call newCall(final Request request) {
        return new Call(new HttpEngine(httpConfig), request);
    }

    public Request.Builder newRequest() {
        return new Request.Builder();
    }

    public static class Builder {

        private final HttpConfig httpConfig = new HttpConfig();

        /**
         * Set the connect timeout in milliseconds. The default is set to 10 seconds.
         *
         * @see java.net.URLConnection#setConnectTimeout(int)
         */
        public Builder connectTimeout(int timeoutMillis) {
            httpConfig.connectTimeout = timeoutMillis;
            return this;
        }

        /**
         * Set the read timeout in milliseconds. The default is set to 10 seconds.
         *
         * @see java.net.URLConnection#setReadTimeout(int)
         */
        public Builder readTimeout(int timeoutMillis) {
            httpConfig.readTimeout = timeoutMillis;
            return this;
        }

        public Builder httpExecutor(Executor executor) {
            httpConfig.httpExecutor = executor;
            return this;
        }

        public Builder callbackExecutor(Executor executor) {
            httpConfig.callbackExecutor = executor;
            return this;
        }

        public HttPizza build() {
            return new HttPizza(httpConfig);
        }
    }
}
