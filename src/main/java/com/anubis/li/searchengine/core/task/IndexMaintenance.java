package com.anubis.li.searchengine.core.task;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ScheduledFuture;

public class IndexMaintenance implements Runnable, ITask {
    ScheduledFuture scheduledFuture;
    long delay = 1000*60*60*12;
    @Override
    public String getName() {
        return "";
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void run() {
        LuceneService.getAllIndex().forEach(index -> {
            try {
                index.forceMerge();
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }
    @Override
    public boolean isRunning() {
        return true;
    }

    @Override
    public ScheduledFuture createScheduled(ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        Date startTime = new Date( System.currentTimeMillis()+delay);
        scheduledFuture =  threadPoolTaskScheduler.scheduleWithFixedDelay(this,startTime,delay);
        return scheduledFuture;
    }

    @Override
    public String getTaskPlan() {
        return "每12小时";
    }

}
