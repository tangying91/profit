package org.profit.server.services

import java.util.concurrent.ScheduledThreadPoolExecutor

class TaskManagerService(corePoolSize: Int) : ScheduledThreadPoolExecutor(corePoolSize)