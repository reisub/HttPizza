package sexy.code;

import java.io.IOException;
import java.util.concurrent.Executor;

public class Call {

    private final Request request;

    private final HttpEngine engine;

    Call(final HttpEngine httpEngine, final Request request) {
        this.engine = httpEngine;
        this.request = request;
    }

    public Response execute() throws IOException {
        try {
            return engine.execute(request);
        } catch (RuntimeException e) {
            throw new IOException(e);
        }
    }

    public void enqueue(final Callback callback) {
        final Executor callbackExecutor = engine.callbackExecutor();
        engine.httpExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final Response response = execute();
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
