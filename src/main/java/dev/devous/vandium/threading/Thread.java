package dev.devous.vandium.threading;

import com.mongodb.Mongo;

import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Thread {

    private final UUID uniqueId;
    private final String name;
    private final ScheduledExecutorService service;

    public Thread(UUID uniqueId, String name) {
        this.uniqueId = uniqueId;
        this.name = name;
        service = new ScheduledThreadPoolExecutor(1);
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getName() {
        return name;
    }

    public void runTask(final Runnable task) {
        service.execute(task);
    }

    public void runTaskLater(final Runnable task, final long delay, final TimeUnit unit) {
        service.schedule(task, delay, unit);
    }

    public void runTaskTimer(final Runnable task, final long delay, final TimeUnit unit) {
        service.scheduleAtFixedRate(task, delay, delay, unit);
    }

    public void terminate() {
        service.shutdown();
    }

}
