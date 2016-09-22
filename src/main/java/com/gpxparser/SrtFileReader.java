package com.gpxparser;

import com.gpxparser.dto.SrtDataBlock;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: Dima <a href="mailto:schetinin.d@gmail.com"/>schetinin.d@gmail.com</a>
 * Date: 9/10/2016
 * Time: 8:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class SrtFileReader {
    private static final Logger logger = LogManager.getLogger(SrtFileReader.class);

    private static String IN_FILENAME = "C:\\GIT_Projects\\GPXParser\\files\\DJI_0172.SRT";

    /*

    **** Subtitle text fragment format ****

    1
    00:00:01,000 --> 00:00:02,000
    HOME(30.4473,50.3637) 2016.02.25 16:12:59
    GPS(30.4451,50.3657,19) BAROMETER:88.1
    ISO:100 Shutter:500 EV: 0 Fnum:F2.8

    2
    00:00:02,000 --> 00:00:03,000
    HOME(30.4473,50.3637) 2016.02.25 16:13:00
    GPS(30.4451,50.3657,19) BAROMETER:88.2
    ISO:100 Shutter:500 EV: 0 Fnum:F2.8

    */

    private static Pattern numberLinePattern = Pattern.compile("^[\\d]+$");
    private static Pattern gpsLinePattern = Pattern.compile("^GPS\\((\\d{2}\\.\\d{4}),(\\d{2}\\.\\d{4}),(\\d{2}).*BAROMETER:(\\d{2}\\.\\d)$");
    private static Pattern dateLinePattern = Pattern.compile("^.*(\\d{4})\\.(\\d{2})\\.(\\d{2})\\s(\\d{2}):(\\d{2}):(\\d{2}).*$");


    public static void main (String [] args) {
        getPointListFromSrtFile(IN_FILENAME);
    }

    private static List<SrtDataBlock> getPointListFromSrtFile (String filePath) {
        LinkedList<SrtDataBlock> srtPointList = new LinkedList<SrtDataBlock>();

        try (FileInputStream fis = new FileInputStream(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {

            String line = reader.readLine();
            while (line != null) {
                // do action with line
                logger.info(line);
                Matcher matcher = numberLinePattern.matcher(line);
                SrtDataBlock srtBlock = null;
                // determine whether new block is starting.
                // Create new container object and add it into list
                if (matcher.matches()) {
                    srtBlock = new SrtDataBlock();
                    srtBlock.setBlockNumber(Integer.parseInt(line));
                    srtPointList.addLast(srtBlock);

                }
                // extract and build GPS longitude, latitude

                // extract and build GPS elevation data
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
                    int month = Integer.parseInt(matcher.group(2)) - 1; // Month is 0 based according to calendar documentation
                    int day = Integer.parseInt(matcher.group(3));
                    int hour = Integer.parseInt(matcher.group(4));
                    int minute = Integer.parseInt(matcher.group(5));
                    int second = Integer.parseInt(matcher.group(6));

                    Calendar cal = Calendar.getInstance(Locale.US);
                    cal.set(year, month, day, hour, minute, second);
                    srtBlock = srtPointList.getLast();
                    srtBlock.setTime(cal.getTime());
                }
                // read the next line
                line = reader.readLine();
            }
            logger.info("Size of result LinkedList is : " + srtPointList.size());

        } catch (IOException e) {
            logger.error("Exception occured during execution : ", e);
        }
        return srtPointList;
    }

}
