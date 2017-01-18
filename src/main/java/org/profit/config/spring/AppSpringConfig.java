package org.profit.config.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:beans/server-beans.xml")
public class AppSpringConfig {

}
