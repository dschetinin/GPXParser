package com.gpxparser;

import com.gpxparser.config.CommonConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * User: dschetinin <a href="mailto:schetinin.d@gmail.com"/>schetinin.d@gmail.com</a>
 * Date: 11/30/2016
 * Time: 3:55 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {CommonConfig.class})
public class DaoTest {

    // http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html
    // http://docs.spring.io/spring-batch/trunk/reference/html/testing.html
    // https://www.mkyong.com/unittest/junit-spring-integration-example/
    @Test
    public void test_ml_always_return_true() {
        System.out.println("asd");
        System.out.println("asd");
    }
}
