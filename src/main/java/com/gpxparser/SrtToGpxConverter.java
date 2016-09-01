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
    private static String OUT_FILENAME = "D:\\GIT_Projects\\GPXParser\\files\\12jan2016_out.gpx";

    public static void main(String[] args) {
        try {
            GPXParser p = new GPXParser();
            FileInputStream in = new FileInputStream(IN_FILENAME);
            FileOutputStream out = new FileOutputStream(OUT_FILENAME);
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
