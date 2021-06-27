package dev.devous.vandium.threading.registry;

import dev.devous.vandium.threading.Thread;

import java.util.*;

public class ThreadRegistry {

    private final Map<UUID, Thread> threads;

    public ThreadRegistry() {
        threads = new HashMap<>();
    }

    public Thread register(String name) {
        Thread thread = new Thread(UUID.randomUUID(), name);
        threads.put(thread.getUniqueId(), thread);
        return thread;
    }

    public Thread unregister(Thread thread) {
        threads.remove(thread.getUniqueId());
        thread.terminate();
        return thread;
    }

    public Thread unregister(UUID uniqueId) {
        Thread thread = getThread(uniqueId);

        if (thread == null) {
            return null;
        }

        threads.remove(uniqueId);
        thread.terminate();;

        return thread;
    }

    public Thread getThread(UUID uniqueId) {
        return threads.getOrDefault(uniqueId, null);
    }

    public Thread[] getThread(String name) {
        List<Thread> threadList = new ArrayList<>();

        for (Thread thread : threads.values()) {
            if (thread.getName().equals(name)) {
                threadList.add(thread);
            }
        }

        return (Thread[]) threadList.toArray();
    }

}
