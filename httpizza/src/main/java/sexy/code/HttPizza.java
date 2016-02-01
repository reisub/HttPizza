package sexy.code;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.Executor;

public class HttPizza {

    private final HttpConfig httpConfig;

    private final ConverterProvider converterProvider;

    private ConnectionListener connectionListener;

    HttPizza(HttpConfig config, ConverterProvider converterProvider) {
        this.httpConfig = config;
        this.converterProvider = converterProvider;
    }

    public HttPizza() {
        this(new HttpConfig(), DEFAULT_CONVERTER);
    }

    public void setConnectionListener(final ConnectionListener listener) {
        connectionListener = listener;
    }

    public <T> Call<T> newCall(final Request request, ConverterProvider.ResponseConverter<T> converter) {
        return new Call<>(new HttpEngine(httpConfig, connectionListener), converter, request);
    }

    public <T> Call<T> newCall(final Request request, Type type) {
        return newCall(request, converterProvider.<T>responseConverter(type));
    }

    public Call<String> newCall(final Request request) {
        return newCall(request, DEFAULT_CONVERTER.<String>responseConverter(String.class));
    }

    public Request.Builder newRequest() {
        return new Request.Builder(converterProvider);
    }

    public static class Builder {

        private final HttpConfig httpConfig = new HttpConfig();

        private ConverterProvider converterProvider = DEFAULT_CONVERTER;

        /**
         * Set the connect timeout in milliseconds. The default is set to 10 seconds.
         *
         * @see java.net.URLConnection#setConnectTimeout(int)
         */
        public void setConnectTimeout(int timeoutMillis) {
            httpConfig.connectTimeout = timeoutMillis;
        }

        /**
         * Set the read timeout in milliseconds. The default is set to 10 seconds.
         *
         * @see java.net.URLConnection#setReadTimeout(int)
         */
        public void setReadTimeout(int timeoutMillis) {
            httpConfig.readTimeout = timeoutMillis;
        }

        public void setHttpExecutor(Executor executor) {
            httpConfig.httpExecutor = executor;
        }

        public void setCallbackExecutor(Executor executor) {
            httpConfig.callbackExecutor = executor;
        }

        public void setConverterProvider(ConverterProvider provider) {
            converterProvider = provider;
        }

        public HttPizza build() {
            return new HttPizza(httpConfig, converterProvider);
        }
    }

    private static final ConverterProvider DEFAULT_CONVERTER =
            new ConverterProvider() {
                @Override
                protected <T> RequestConverter<T> requestConverter(Type type) {
                    return new RequestConverter<T>() {
                        @Override
                        public RequestBody convert(final T origin) throws IOException {
                            return new RequestBody() {
                                @Override
                                public String contentType() {
                                    return "text/plain; charset=utf-8";
                                }

                                @Override
                                public void writeTo(BufferedOutputStream os) throws IOException {
                                    if (origin == null) {
                                        return;
                                    }
                                    os.write(origin.toString().getBytes(Util.UTF_8));
                                }

                                @Override
                                public long contentLength() throws IOException {
                                    if (origin == null) {
                                        return 0;
                                    } else {
                                        return origin.toString().length();
                                    }
                                }
                            };
                        }
                    };
                }

                @Override
                protected <T> ResponseConverter<T> responseConverter(Type type) {
                    return new ResponseConverter<T>() {
                        @Override
                        public T convert(ResponseBody body) throws IOException {
                            try {
                                return (T) body.string();
                            } catch (ClassCastException e) {
                                throw new IOException(e);
                            }
                        }
                    };
                }
            };
}
