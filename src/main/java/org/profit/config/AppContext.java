package org.profit.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Kyia
 */
public class AppContext implements ApplicationContextAware {

	// App context
	public static final String APP_CONTEXT = "appContext";

	// Servers
	public static final String SERVER_MANAGER = "serverManager";
	public static final String HTTP_SERVER = "httpServer";

	// Services
	public static final String SCHEDULE_SERVICE = "scheduleService";
	public static final String TASK_SERVICE = "taskService";

	// Cache
	public static final String CACHE_STORE = "cacheStore";

	// The spring application context.
	private static ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		AppContext.applicationContext = applicationContext;
	}

	/**
	 * This method is used to retrieve a bean by its name. Note that this may
	 * result in new bean creation if the scope is set to "prototype" in the
	 * bean configuration file.
	 *
	 * @param beanName The name of the bean as configured in the spring configuration file.
	 * @return The bean if it is existing or null.
	 */
	public static Object getBean(String beanName) {
		if (beanName == null) {
			return null;
		}
		return applicationContext.getBean(beanName);
	}

	/**
	 * Called from the main method once the application is initialized.
	 */
	public void initialized() {

	}
}

