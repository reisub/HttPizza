package sexy.code;

import java.util.concurrent.Executor;

public class SynchronousExecutor implements Executor {

    @Override
    public void execute(Runnable command) {
        command.run();
    }
}
