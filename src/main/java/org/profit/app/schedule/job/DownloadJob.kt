package org.profit.app.schedule.job

import org.profit.app.StockHall
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException

class DownloadJob : Job {

    @Throws(JobExecutionException::class)
    override fun execute(context: JobExecutionContext) {
        StockHall.download()
    }
}