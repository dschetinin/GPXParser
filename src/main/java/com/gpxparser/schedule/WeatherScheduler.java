package com.gpxparser.schedule;

import com.gpxparser.json.Example;
import com.gpxparser.json.Quote;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: dschetinin <a href="mailto:schetinin.d@gmail.com"/>schetinin.d@gmail.com</a>
 * Date: 10/27/2016
 * Time: 6:40 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
@PropertySource("classpath:gpxparser.properties")
public class WeatherScheduler {

    private static final Logger logger = LogManager.getLogger(WeatherScheduler.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Value("${application.web.url}")
    private String exampleUri;

    public void reportCurrentTime() {
        logger.info(String.format("The time is now %s", dateFormat.format(new Date())));
    }

    public void callSpringQuote() {
        RestTemplate restTemplate = new RestTemplate();
        Quote quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
        logger.info(quote.toString());
    }

    @Scheduled(fixedRateString = "${scheduler.fixedRate}")
    @Async
    public void callLocalExample() {
        RestTemplate restTemplate = new RestTemplate();
        Example example = restTemplate.getForObject(exampleUri + "/json/example", Example.class);
        logger.info(example.toString());
    }
}
