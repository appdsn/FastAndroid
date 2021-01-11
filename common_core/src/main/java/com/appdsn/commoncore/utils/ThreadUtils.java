package com.appdsn.commoncore.utils;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.CallSuper;
import android.util.Log;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Desc:线程执行：UI线程，子线程，线程延迟或定期执行
 *
 * @Author: wangbaozhong
 * @Date: 2019/10/29 15:15
 * @Copyright: Copyright (c) 2016-2020
 * @""
 * @Version: 1.0
 */
public class ThreadUtils {

    private static Handler sHandler = new Handler(Looper.getMainLooper());
    private static ExecutorService THREAD_POOL_EXECUTOR = null;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = Math.max(2, CPU_COUNT * 2);
    private static final int MAXIMUM_POOL_SIZE = CORE_POOL_SIZE;
    private static final int KEEP_ALIVE_SECONDS = 30;
    private static final Map<ThreadTask, TaskInfo> TASK_TASKINFO_MAP = new ConcurrentHashMap<>();
    private static final Timer TIMER = new Timer();

    private static ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "ThreadUtils #" + mCount.getAndIncrement());
        }
    };

    private static final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>();

    static {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
                sPoolWorkQueue, sThreadFactory, new ThreadPoolExecutor.DiscardPolicy());
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        THREAD_POOL_EXECUTOR = threadPoolExecutor;
    }

    /**
     * Return whether the thread is the main thread.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * Cancel the given task.
     *
     * @param task The task to cancel.
     */
    public static void cancel(final ThreadTask task) {
        if (task == null) {
            return;
        }
        task.cancel();
    }

    /**
     * Executes the given task in a custom thread pool.
     *
     * @param task The task to execute.
     * @param <T>  The type of the task's result.
     */
    public static <T> void execute(final ThreadTask<T> task) {
        execute(THREAD_POOL_EXECUTOR, task, 0, 0);
    }

    /**
     * Executes the given task in a custom thread pool.
     *
     * @param task        The task to execute.
     * @param delayMillis delay in milliseconds before task is to be executed.
     * @param <T>         The type of the task's result.
     */
    public static <T> void executeDelay(final ThreadTask<T> task, final long delayMillis) {
        execute(THREAD_POOL_EXECUTOR, task, delayMillis, 0);
    }

    /**
     * Executes the given task in a custom thread pool.
     *
     * @param task         The task to execute.
     * @param delayMillis  delay in milliseconds before task is to be executed.
     * @param periodMillis time in milliseconds between successive task executions.
     * @param <T>          The type of the task's result.
     */
    public static <T> void executeAtFixRate(final ThreadTask<T> task, final long delayMillis, final long periodMillis) {
        execute(THREAD_POOL_EXECUTOR, task, delayMillis, periodMillis);
    }

    /**
     * Executes the given task in a custom thread pool.
     *
     * @param pool The custom thread pool.
     * @param task The task to execute.
     * @param <T>  The type of the task's result.
     */
    public static <T> void executeByCustom(final ExecutorService pool, final ThreadTask<T> task, final long delayMillis, final long periodMillis) {
        execute(pool, task, delayMillis, periodMillis);
    }

    private static <T> void execute(final ExecutorService pool, final ThreadTask<T> task,
                                    long delayMillis, final long periodMillis) {
        TaskInfo taskInfo;
        synchronized (TASK_TASKINFO_MAP) {
            if (TASK_TASKINFO_MAP.get(task) != null) {
                Log.e("ThreadUtils", "Task can only be executed once.");
                return;
            }
            taskInfo = new TaskInfo(pool);
            TASK_TASKINFO_MAP.put(task, taskInfo);
        }
        if (periodMillis == 0) {
            if (delayMillis == 0) {
                pool.execute(task);
            } else {
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        task.checkTimeout();
                        pool.execute(task);
                    }
                };
                taskInfo.mTimerTask = timerTask;
                TIMER.schedule(timerTask, delayMillis);
            }
        } else {
            task.setSchedule(true);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    task.checkTimeout();
                    pool.execute(task);
                }
            };
            taskInfo.mTimerTask = timerTask;
            TIMER.scheduleAtFixedRate(timerTask, delayMillis, periodMillis);
        }
    }

    public static void runOnUiThread(Runnable task) {
        sHandler.post(task);
    }

    public static void runOnUiThreadDelay(Runnable task, long delayMillis) {
        sHandler.postDelayed(task, delayMillis);
    }

    public static void runOnUiThreadAtFixRate(final Runnable task, long delayMillis, final long periodMillis) {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                sHandler.post(task);
            }
        };
        TIMER.scheduleAtFixedRate(timerTask, delayMillis, periodMillis);
    }

    public static void removeUiThreadTask(Runnable task) {
        if (task != null) {
            sHandler.removeCallbacks(task);
        }
    }

    public abstract static class SimpleTask extends ThreadTask<Void> {
        @Override
        public void onSuccess(Void result) {
        }

        @Override
        public void onCancel() {
            Log.e("ThreadUtils", "onCancel: " + Thread.currentThread());
        }

        @Override
        public void onFail(Throwable t) {
            Log.e("ThreadUtils", "onFail: ", t);
        }
    }

    public abstract static class ThreadTask<T> implements Runnable {

        private static final int NEW = 0;
        private static final int RUNNING = 1;
        private static final int EXCEPTIONAL = 2;
        private static final int COMPLETING = 3;
        private static final int CANCELLED = 4;
        private static final int INTERRUPTED = 5;
        private static final int TIMEOUT = 6;

        private final AtomicInteger state = new AtomicInteger(NEW);

        private volatile boolean isSchedule;
        private volatile Thread runner;
        private long mTimeoutMillis = 10 * 1000;

        public abstract T doInBackground() throws Throwable;

        public abstract void onSuccess(T result);

        public void onCancel() {
        }

        public void onFail(Throwable t) {
        }

        @Override
        public void run() {
            if (isSchedule) {
                if (runner == null) {
                    if (!state.compareAndSet(NEW, RUNNING)) {
                        return;
                    }
                    runner = Thread.currentThread();
                } else {
                    if (state.get() != RUNNING) {
                        return;
                    }
                }
            } else {
                if (!state.compareAndSet(NEW, RUNNING)) {
                    return;
                }
                runner = Thread.currentThread();
            }
            try {
                final T result = doInBackground();
                if (isSchedule) {
                    if (state.get() != RUNNING) {
                        return;
                    }
                    sHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            onSuccess(result);
                        }
                    });
                } else {
                    if (!state.compareAndSet(RUNNING, COMPLETING)) {
                        return;
                    }

                    sHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            onSuccess(result);
                            onDone();
                        }
                    });
                }
            } catch (InterruptedException ignore) {
                state.compareAndSet(CANCELLED, INTERRUPTED);
            } catch (final Throwable throwable) {
                if (!state.compareAndSet(RUNNING, EXCEPTIONAL)) {
                    return;
                }
                sHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onFail(throwable);
                        onDone();
                    }
                });
            }
        }

        public void cancel() {
            cancel(true);
        }

        public void cancel(boolean mayInterruptIfRunning) {
            synchronized (state) {
                if (state.get() > RUNNING) {
                    return;
                }
                state.set(CANCELLED);
            }
            if (mayInterruptIfRunning) {
                if (runner != null) {
                    runner.interrupt();
                }
            }
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    onCancel();
                    onDone();
                }
            });
        }

        private void timeout() {
            synchronized (state) {
                if (state.get() > RUNNING) {
                    return;
                }
                state.set(TIMEOUT);
            }
            if (runner != null) {
                runner.interrupt();
            }
            onFail(new Exception("超时"));
            onDone();
        }

        public boolean isCanceled() {
            return state.get() >= CANCELLED;
        }

        public boolean isDone() {
            return state.get() > RUNNING;
        }

        private Runnable mTimeoutTask = new Runnable() {
            @Override
            public void run() {
                if (!isDone()) {
                    timeout();
                }
            }
        };

        private void checkTimeout() {
            if (mTimeoutMillis > 0) {
                sHandler.removeCallbacks(mTimeoutTask);
                sHandler.postDelayed(mTimeoutTask, mTimeoutMillis);
            }
        }

        public void setTimeout(final long timeoutMillis) {
            mTimeoutMillis = timeoutMillis;
        }

        private void setSchedule(boolean isSchedule) {
            this.isSchedule = isSchedule;
        }

        @CallSuper
        protected void onDone() {
            TASK_TASKINFO_MAP.remove(this);
            sHandler.removeCallbacks(mTimeoutTask);
        }
    }

    private static class TaskInfo {
        private TimerTask mTimerTask;
        private ExecutorService mService;

        private TaskInfo(ExecutorService service) {
            mService = service;
        }
    }
}
