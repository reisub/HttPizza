package sexy.code;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class HttpConfig {

    public int connectTimeout = (int) TimeUnit.SECONDS.toMillis(10);

    public int readTimeout = (int) TimeUnit.SECONDS.toMillis(10);

    public Executor httpExecutor = new ThreadPoolExecutor(1, 10, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());

    public Executor callbackExecutor = new Executor() {

        Handler mainHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            mainHandler.post(command);
        }
    };
}
