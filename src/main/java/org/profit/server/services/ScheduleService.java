package org.profit.server.services;

import org.profit.config.AppContext;
import org.profit.util.PathUtils;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Properties;

/**
 * @author Kyia
 */
@Service(AppContext.SCHEDULE_SERVICE)
public class ScheduleService {

	private static final Logger LOG = LoggerFactory.getLogger(ScheduleService.class);

	private SchedulerFactory sf;
	private Scheduler scheduler;

	private static final String SCHEDULE_PROPS = "schedule.properties";
	private static final String JOB_XML = "jobs.xml";

	public ScheduleService() {

	}

	public void init() {
		// Get properties, need dynamic set jobs.xml
		Properties props = new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(PathUtils.getConfPath() + SCHEDULE_PROPS));
			props.load(in);

			props.setProperty("org.quartz.plugin.jobInitializer.fileNames", PathUtils.getConfPath() + File.separator + JOB_XML);
		} catch (IOException e) {
			LOG.error("Schedule properties file load failed.", e);
		}

		try {
			sf = new StdSchedulerFactory(props);

			scheduler = sf.getScheduler();
			scheduler.start();
		} catch (SchedulerException se) {
			LOG.error("Schedule service start failed.", se);
		}

		LOG.info("Schedule service started");
	}

	public void destroy() {
		try {
			scheduler.shutdown(true);
		} catch (SchedulerException se) {
			LOG.error("Schedule service stop failed.", se);
		}
	}
}
