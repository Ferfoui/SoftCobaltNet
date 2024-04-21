package fr.ferfoui.softcobalt.api.socket.serverside.thread;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerThreadFactory implements ThreadFactory {

    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final AtomicInteger threadNumber = new AtomicInteger(0);
    private final String namePrefix;

    /**
     * Constructs a new {@code Thread}.  Implementations may also initialize
     * priority, name, daemon status, {@code ThreadGroup}, etc.
     */
    public ServerThreadFactory() {
        namePrefix = "server-" + poolNumber.getAndIncrement() + "-thread-";
    }

    /**
     * Constructs a new {@code Thread}.  Implementations may also initialize
     * priority, name, daemon status, {@code ThreadGroup}, etc.
     *
     * @param runnable a runnable to be executed by new thread instance
     * @return constructed thread, or {@code null} if the request to
     * create a thread is rejected
     */
    @Override
    public Thread newThread(@NotNull Runnable runnable) {
        Thread thread = new Thread(
                null, runnable, namePrefix + threadNumber.getAndIncrement(), 0
        );

        if (thread.isDaemon())
            thread.setDaemon(false);

        if (thread.getPriority() != Thread.NORM_PRIORITY)
            thread.setPriority(Thread.NORM_PRIORITY);

        return thread;
    }
}
