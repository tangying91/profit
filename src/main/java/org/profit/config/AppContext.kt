package org.profit.config

import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

class AppContext : ApplicationContextAware {

    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        Companion.applicationContext = applicationContext
    }

    /**
     * Called from the main method once the application is initialized.
     */
    fun initialized() {}

    companion object {
        // App context
        const val APP_CONTEXT = "appContext"
        // Servers
        const val SERVER_MANAGER = "serverManager"
        const val HTTP_SERVER = "httpServer"
        // Services
        const val SCHEDULE_SERVICE = "scheduleService"
        const val TASK_SERVICE = "taskService"
        // Cache
        const val CACHE_STORE = "cacheStore"
        // The spring application context.
        private var applicationContext: ApplicationContext? = null

        /**
         * This method is used to retrieve a bean by its name. Note that this may
         * result in new bean creation if the scope is set to "prototype" in the
         * bean configuration file.
         *
         * @param beanName The name of the bean as configured in the spring configuration file.
         * @return The bean if it is existing or null.
         */
        fun getBean(beanName: String?): Any? {
            return if (beanName == null) {
                null
            } else applicationContext!!.getBean(beanName)
        }
    }
}