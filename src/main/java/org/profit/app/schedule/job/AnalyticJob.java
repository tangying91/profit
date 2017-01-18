package org.profit.app.schedule.job;

import org.profit.app.realm.StockRobot;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author TangYing
 *
 * 单线程循环下载
 */
public class AnalyticJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        StockRobot.run();
    }
}
