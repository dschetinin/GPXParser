package com.gpxparser.config;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * User: dschetinin <a href="mailto:schetinin.d@gmail.com"/>schetinin.d@gmail.com</a>
 * Date: 10/28/2016
 * Time: 1:14 PM
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@PropertySource(value = {"classpath:gpxparser.properties"}, ignoreResourceNotFound = true)
public class CommonConfig {

    private static final Logger logger = LogManager.getLogger(SchedulerConfig.class);

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
