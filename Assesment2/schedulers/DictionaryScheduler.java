package com.akqa.aem.training.aem201.core.schedulers;

import com.akqa.aem.training.aem201.core.models.DictionaryModel;

import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;

import java.text.SimpleDateFormat;

import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Component(immediate = true, service = Runnable.class)
@Designate(ocd = DictionarySchedulerConfiguration.class)
public class DictionaryScheduler implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(DictionaryScheduler.class);

    private int schedulerId;

    @Reference
    private Scheduler scheduler;

    @Activate
    protected void activate(DictionarySchedulerConfiguration config) {
        schedulerId = config.schedulerName().hashCode();
        addScheduler(config);
    }

    @Deactivate
    protected void deactivate(DictionarySchedulerConfiguration config) {
        removeScheduler();
    }

    protected void removeScheduler() {
        scheduler.unschedule(String.valueOf(schedulerId));
    }

    protected void addScheduler(DictionarySchedulerConfiguration config) {
        ScheduleOptions scheduleOptions = scheduler.EXPR(config.cronExpression());
        scheduleOptions.name(String.valueOf(schedulerId));
        scheduler.schedule(this, scheduleOptions);
    }
    public static int randomIndex;
    public static String wordUpdatedTime;
    private Queue<Integer> queue = new ArrayDeque<>();

    @Override //runs every one minute
    public void run()  {
        do {
            randomIndex = (int)(Math.random()*DictionaryModel.dictionarySize);
        } while(queue.contains(randomIndex));

        // word will not be repeated if it has been showed in the last 10 enteries.
        queue.add(randomIndex);
        if(queue.size()>10) {
            queue.remove();
        }
        // to note that at what time the word was stored
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        wordUpdatedTime = formatter.format(date);
    }
}