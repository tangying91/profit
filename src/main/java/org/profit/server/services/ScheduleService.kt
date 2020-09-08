package org.profit.server.services

import org.profit.config.AppContext
import org.profit.util.PathUtils
import org.quartz.Scheduler
import org.quartz.SchedulerException
import org.quartz.SchedulerFactory
import org.quartz.impl.StdSchedulerFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

import java.io.*
import java.util.Properties

@Service(AppContext.SCHEDULE_SERVICE)
class ScheduleService {

    companion object {
        private val LOG = LoggerFactory.getLogger(ScheduleService::class.java)

        private const val SCHEDULE_PROPS = "schedule.properties"
        private const val JOB_XML = "jobs.xml"
    }

    private var sf: SchedulerFactory? = null
    private var scheduler: Scheduler? = null

    fun init() {
        // Get properties, need dynamic set jobs.xml
        val props = Properties()
        try {
            val `in` = BufferedInputStream(FileInputStream(PathUtils.confPath + SCHEDULE_PROPS))
            props.load(`in`)

            props.setProperty("org.quartz.plugin.jobInitializer.fileNames", PathUtils.confPath + File.separator + JOB_XML)
        } catch (e: IOException) {
            LOG.error("Schedule properties file load failed.", e)
        }

        try {
            sf = StdSchedulerFactory(props)

            scheduler = sf!!.scheduler
            scheduler!!.start()
        } catch (se: SchedulerException) {
            LOG.error("Schedule service start failed.", se)
        }

        LOG.info("Schedule service started")
    }

    fun destroy() {
        try {
            scheduler!!.shutdown(true)
        } catch (se: SchedulerException) {
            LOG.error("Schedule service stop failed.", se)
        }

    }
}
