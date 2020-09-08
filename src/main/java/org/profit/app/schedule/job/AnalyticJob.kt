package org.profit.app.schedule.job

import org.profit.app.realm.StockRobot
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException

class AnalyticJob : Job {

    @Throws(JobExecutionException::class)
    override fun execute(context: JobExecutionContext) {
        StockRobot.start()
    }
}