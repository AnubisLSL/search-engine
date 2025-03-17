package com.anubis.li.searchengine.core.task;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ScheduledFuture;

public interface ITask {

    String getName();

    void shutdown();

    boolean isRunning();

    ScheduledFuture createScheduled(ThreadPoolTaskScheduler threadPoolTaskScheduler);

    String getTaskPlan();
}
