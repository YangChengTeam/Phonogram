package yc.com.rthttplibrary.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TaskUtil is a task runer util.
 */
public class TaskUtil {
    private static TaskUtil taskUtil = new TaskUtil();
    private ExecutorService service;

    private TaskUtil() {
        int num = Runtime.getRuntime().availableProcessors();
        service = Executors.newFixedThreadPool(num);
    }

    public static TaskUtil getImpl() {
        return taskUtil;
    }

    /**
     * execute task with executor service
     *
     * @param task the task execute with Runnable
     */
    public void runTask(Runnable task) {
        service.submit(task);
    }

    public void stop() {
        if (service != null) {
            service.shutdownNow();
        }
    }
}
