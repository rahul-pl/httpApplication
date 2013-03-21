package prj.httpApplication.utils;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ConcurrencyUtils
{
    private static ConcurrencyUtils _instance;
    private ScheduledExecutorService _scheduledExecutor;

    private ConcurrencyUtils(ScheduledExecutorService scheduledExecutor)
    {
        _scheduledExecutor = scheduledExecutor;
    }

    public static void initialize(ScheduledExecutorService scheduledExecutor)
    {
        _instance = new ConcurrencyUtils(scheduledExecutor);
    }

    public static ConcurrencyUtils getInstance()
    {
        return _instance;
    }

    public ScheduledFuture<?> scheduleRunnable(Runnable runnable, int delay, TimeUnit timeUnit)
    {
        return _scheduledExecutor.schedule(runnable, delay, timeUnit);
    }
}
