package com.gpxparser;

import com.gpxparser.dto.SrtDataBlock;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: dschetinin <a href="mailto:schetinin.d@gmail.com"/>schetinin.d@gmail.com</a>
 * Date: 9/27/2016
 * Time: 4:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class SrtFileReaderLambda extends SrtFileReader {
    private static final Logger logger = LogManager.getLogger(SrtFileReaderLambda.class);

    protected Pattern emptyStringPattern = Pattern.compile("^\\n|\\r|\\r\\n|\\u0085|\\u2028|\\u2029$");

    public static void main (String [] args) {
        SrtFileReaderLambda fileReaderLambda = new SrtFileReaderLambda();
        SrtFileReader fileReader = new SrtFileReader();

//        fileReader.getPointListFromSrtFile(IN_FILENAME);
        fileReaderLambda.getPointListFromSrtFile(IN_FILENAME);
    }

    @Override
    protected List<SrtDataBlock> getPointListFromSrtFile(String filePath) {
        List<String> fileStringList = new ArrayList<>();
        LinkedList<SrtDataBlock> srtPointList = new LinkedList<SrtDataBlock>();

        try (FileInputStream fis = new FileInputStream(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
            // first build a list of a string objects
            String text = reader.readLine();
            while (text != null) {
                fileStringList.add(text);
                // read the next line
                text = reader.readLine();
            }

            // now let's use stream/lambdas to build another list
            fileStringList.stream().filter(line -> {
                Matcher matcher = emptyStringPattern.matcher(line);
                return !matcher.matches() && !"".equals(line);
            }).forEach(line -> {
                Matcher matcher = numberLinePattern.matcher(line);
                SrtDataBlock srtBlock = null;
                // determine whether new block is starting.
                // Create new container object and add it into list
                if (matcher.matches()) {
                    srtBlock = new SrtDataBlock();
                    srtBlock.setBlockNumber(Integer.parseInt(line));
                    srtPointList.addLast(srtBlock);
                }

                // extract and build GPS longitude, latitude and elevation data
                matcher = gpsLinePattern.matcher(line);
                if (matcher.matches()) {
                    Double longitude = Double.parseDouble(matcher.group(1));
                    Double latitude = Double.parseDouble(matcher.group(2));
                    Double gpsAltitude = 10 * Double.parseDouble(matcher.group(3));
                    Double elevation = Double.parseDouble(matcher.group(4));

                    srtBlock = srtPointList.getLast();
                    srtBlock.setLatitude(latitude);
                    srtBlock.setLongitude(longitude);
                    srtBlock.setGpsAltitude(gpsAltitude);
                    srtBlock.setElevation(elevation);
                }
                // extract and build date object from srt line
                matcher = dateLinePattern.matcher(line);
                if (matcher.matches()) {
                    int year = Integer.parseInt(matcher.group(1));
                    int month = Integer.parseInt(matcher.group(2)); // month is from 1 to 12 according to LocaDateTime API
                    int day = Integer.parseInt(matcher.group(3));
                    int hour = Integer.parseInt(matcher.group(4));
                    int minute = Integer.parseInt(matcher.group(5));
                    int second = Integer.parseInt(matcher.group(6));

                    LocalDateTime dateTime = LocalDateTime.of(year, Month.of(month), day, hour, minute, second);
                    Date date = new Date(dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

                    srtBlock = srtPointList.getLast();
                    srtBlock.setTime(date);
                }
            });

            srtPointList.forEach(element -> logger.debug(element));
            logger.info("Size of result LinkedList is : " + srtPointList.size());

        } catch (IOException e) {
            logger.error("Exception occured during execution : ", e);
        }
        return srtPointList;
    }
}
