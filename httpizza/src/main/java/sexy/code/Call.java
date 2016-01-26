package sexy.code;

import java.io.IOException;
import java.util.concurrent.Executor;

public class Call<T> {

    private final ConverterProvider.ResponseConverter<T> converter;

    private final Request request;

    private final HttpEngine engine;

    Call(final HttpEngine httpEngine, final ConverterProvider.ResponseConverter<T> converter, final Request request) {
        this.engine = httpEngine;
        this.converter = converter;
        this.request = request;
    }

    public Response<T> execute() throws IOException {
        try {
            final RequestConvertTask<?> task = request.getPendingTask();
            if (task != null) {
                request.setBody(task.execute());
            }
            final HttpResponse httpResponse = engine.execute(request);
            T obj = null;
            if (httpResponse.isSuccess()) {
                obj = converter.convert(httpResponse.getBody());
            }
            return new Response<>(httpResponse, obj);
        } catch (RuntimeException e) {
            throw new IOException(e);
        }
    }

    public void enqueue(final Callback<T> callback) {
        final Executor callbackExecutor = engine.callbackExecutor();

        engine.httpExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final Response<T> response = execute();
                    callbackExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            callback.onResponse(response);
                        }
                    });
                } catch (final Throwable t) {
                    callbackExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure(t);
                        }
                    });
                }
            }
        });
    }
}
