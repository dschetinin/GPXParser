package com.gpxparser;

import com.gpxparser.jpa.GpxTracksHistoryEntity;
import com.gpxparser.repository.GpxTracksDao;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * User: dschetinin <a href="mailto:schetinin.d@gmail.com"/>schetinin.d@gmail.com</a>
 *  Read articles below:
 *
 *  http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html
 *  http://docs.spring.io/spring-batch/trunk/reference/html/testing.html
 *  https://www.mkyong.com/unittest/junit-spring-integration-example/
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(value = {"classpath:test.properties"})
public class DaoTest {

    private static final Logger logger = LogManager.getLogger(DaoTest.class);

    @Autowired
    private Environment env;

    @Autowired
    private GpxTracksDao gpxTracksDao;

    @Test
    public void test_ml_always_return_true() {
        System.out.println(env.getProperty("test.property1"));
        System.out.println("asd");
    }

    @Test
    public void testGetAllDesc() {
        List<GpxTracksHistoryEntity> tracks = gpxTracksDao.getAllDesc();
//        for (GpxTracksHistoryEntity track : tracks) {
//            logger.info(track);
//        }
        logger.info("Printing full list of tracks ordered in descending order:");
        tracks.stream().forEach(logger::info);

        logger.info("Printing list of track file names:");
        tracks.stream().sorted((o1, o2) -> { return o2.getDateCreated().compareTo(o1.getDateCreated());}).map(t -> t.getFileName()).forEach(logger::info);

    }
}
