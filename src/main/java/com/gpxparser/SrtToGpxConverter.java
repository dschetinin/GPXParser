package com.gpxparser;

import org.alternativevision.gpx.GPXParser;
import org.alternativevision.gpx.beans.GPX;
import org.alternativevision.gpx.beans.Track;
import org.alternativevision.gpx.beans.Waypoint;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SrtToGpxConverter {

    private static final Logger logger = LogManager.getLogger(SrtToGpxConverter.class);

    private static String IN_FILENAME = "D:\\GIT_Projects\\GPXParser\\files\\12jan2016_editplus.gpx";
//    private static String IN_FILENAME = "D:\\GIT_Projects\\GPXParser\\files\\runtastic_20160109_1147_Беговые лыжи.gpx";
    private static String OUT_FILENAME = "D:\\GIT_Projects\\GPXParser\\files\\12jan2016_out.gpx";


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
    private static String SRT_DATE_FORMAT = "YYYY.MM.DD HH:MM:SS";  // 2016.02.25 16:13:01

    public static void main(String[] args) {
        try (FileInputStream in = new FileInputStream(IN_FILENAME);
             FileOutputStream out = new FileOutputStream(OUT_FILENAME)) {
            GPXParser p = new GPXParser();
            logger.info("Reading data from input file : " + IN_FILENAME);
            GPX gpx = p.parseGPX(in);

            for (Track track : gpx.getTracks()) {
                for (Waypoint waypoint : track.getTrackPoints()) {
                    logger.info(" Longitude " + waypoint.getLongitude() + " latitude : " + waypoint.getLatitude());
                }
            }
            p.writeGPX(gpx, out);
            logger.info("File '" + OUT_FILENAME + "' has been saved.");
        } catch (ParserConfigurationException | SAXException | TransformerException | IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    private static void loadGpxData() {

    }
}
