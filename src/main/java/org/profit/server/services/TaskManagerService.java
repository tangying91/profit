package org.profit.server.services;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class TaskManagerService extends ScheduledThreadPoolExecutor {

	public TaskManagerService(int corePoolSize) {
		super(corePoolSize);
	}
}
