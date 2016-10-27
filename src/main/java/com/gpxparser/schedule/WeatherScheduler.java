package com.gpxparser.schedule;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: dschetinin <a href="mailto:schetinin.d@gmail.com"/>schetinin.d@gmail.com</a>
 * Date: 10/27/2016
 * Time: 6:40 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class WeatherScheduler {

    private static final Logger logger = LogManager.getLogger(WeatherScheduler.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    @Async
    public void reportCurrentTime() {
        logger.info(String.format("The time is now %s", dateFormat.format(new Date())));
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
