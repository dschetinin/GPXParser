package com.gpxparser;

import com.gpxparser.resources.GpxType;
import com.gpxparser.resources.TrkType;
import com.gpxparser.resources.TrksegType;
import com.gpxparser.resources.WptType;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class SrtToGpxConverter {

    private static final Logger logger = LogManager.getLogger(SrtToGpxConverter.class);

    private static String IN_FILENAME = "C:\\GIT_Projects\\GPXParser\\files\\DJI_0172.SRT";
//    private static String IN_FILENAME = "D:\\GIT_Projects\\GPXParser\\files\\runtastic_20160109_1147_Беговые_лыжи.gpx";
//    private static String OUT_FILENAME = "C:\\GIT_Projects\\GPXParser\\files\\12jan2016_out_3.gpx";
    private static String OUT_FILENAME = "C:\\GIT_Projects\\GPXParser\\files\\runtastic_20160109_1147_Беговые лыжи.gpx";


    public static void main(String[] args) {
        try (FileInputStream in = new FileInputStream(IN_FILENAME)) {

            logger.info("Reading data from input file : " + IN_FILENAME);
/*
            SrtFileReaderLambda fileReaderLambda = new SrtFileReaderLambda();
            List<SrtDataBlock> srtList = fileReaderLambda.getPointListFromSrtFile(IN_FILENAME);

            ArrayList<Waypoint> trackPoints = new ArrayList<>(
                    srtList.stream().map(srtElement -> {
                            Waypoint waypoint = new Waypoint();
                            waypoint.setLongitude(srtElement.getLongitude());
                            waypoint.setLatitude(srtElement.getLatitude());
                            waypoint.setElevation(srtElement.getElevation());
                            waypoint.setTime(srtElement.getTime());
                            return waypoint;
                        }).collect(Collectors.toList()));
*/


            GpxType gpx = null;
            JAXBContext jc = JAXBContext.newInstance("com.gpxparser.resources");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            JAXBElement<GpxType> root = (JAXBElement<GpxType>)unmarshaller
                    .unmarshal(new File(OUT_FILENAME));
            gpx = root.getValue();
            List<TrkType> tracks = gpx.getTrk();

            // TODO: replace this with lambdas
            for (TrkType track : tracks) {
                for (TrksegType trkSeg : track.getTrkseg()) {
                    for (WptType waypoint : trkSeg.getTrkpt()) {
                        logger.debug(waypoint.getLat() + " " + waypoint.getLon());
                    }
                }
            }

            tracks.size();

/*
            GPXParser p = new GPXParser();
            GPX gpx = p.parseGPX(in);
            GPX gpx = new GPX();
            Track track = new Track();
            track.setName("Converted file " + OUT_FILENAME );
            track.setTrackPoints(trackPoints);
            gpx.addTrack(track);
            p.writeGPX(gpx, out);
*/

/*
            for (Track track : gpx.getTracks()) {
                for (Waypoint waypoint : track.getTrackPoints()) {
                    logger.info(" Longitude " + waypoint.getLongitude() + " latitude : " + waypoint.getLatitude());
                }
            }
            p.writeGPX(gpx, out);
*/
            logger.info("File '" + OUT_FILENAME + "' has been saved.");
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    private static void loadGpxData() {

    }
}
