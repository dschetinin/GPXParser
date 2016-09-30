package com.gpxparser;

import com.gpxparser.dto.SrtDataBlock;
import com.gpxparser.resources.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class SrtToGpxConverter {

    private static final Logger logger = LogManager.getLogger(SrtToGpxConverter.class);

    private static String FILES_FOLDER = "C:\\GIT_Projects\\GPXParser\\files";

    private static String IN_FILENAME = "DJI_0179.SRT";
//    private static String IN_FILENAME = "C:\\GIT_Projects\\GPXParser\\files\\runtastic_20160109_1147_Беговые лыжи.gpx";
//    private static String IN_FILENAME = "D:\\GIT_Projects\\GPXParser\\files\\runtastic_20160109_1147_Беговые_лыжи.gpx";
//    private static String OUT_FILENAME = "C:\\GIT_Projects\\GPXParser\\files\\12jan2016_out_3.gpx";
    private static String OUT_FILENAME = "DJI_0179.gpx";


    public static void main(String[] args) {
        try (FileOutputStream out = new FileOutputStream(FILES_FOLDER + File.separator + OUT_FILENAME)) {

            logger.info("Reading data from input file : " + IN_FILENAME);
            SrtFileReaderLambda fileReaderLambda = new SrtFileReaderLambda();
            List<SrtDataBlock> srtList = fileReaderLambda.getPointListFromSrtFile(FILES_FOLDER + File.separator + IN_FILENAME);

            // create JAXB object factory to manage JAXB elements creation
            JAXBContext jc = JAXBContext.newInstance("com.gpxparser.resources");
            ObjectFactory objFactory = new ObjectFactory();

            GpxType gpxType = objFactory.createGpxType();
            gpxType.setCreator("SrtToGpx java conversion tool");
            gpxType.setVersion("1.1");

            MetadataType metadata = objFactory.createMetadataType();
            LinkType linkMetaData = objFactory.createLinkType();
            linkMetaData.setHref("http://use.our.converter");
            linkMetaData.setText("Srt to GPX converter");
            metadata.getLink().add(linkMetaData);

            gpxType.setMetadata(metadata);

            TrkType trkType = objFactory.createTrkType();
            LinkType linkTrk = objFactory.createLinkType();
            TrksegType trkSeqType = objFactory.createTrksegType();

            for (SrtDataBlock srtBlock : srtList) {
                WptType wptType = objFactory.createWptType();
                wptType.setLat(srtBlock.getLatitude());
                wptType.setLon(srtBlock.getLongitude());
                wptType.setEle(srtBlock.getElevation());
                GregorianCalendar c = new GregorianCalendar(Locale.getDefault());
                c.setTime(srtBlock.getTime());
                XMLGregorianCalendar xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
                wptType.setTime(xmlCal);

//                ExtensionsType extensions = objFactory.createExtensionsType();
//                wptType.setExtensions(extensions);
                trkSeqType.getTrkpt().add(wptType);
            }

            linkTrk.setHref("http://use.our.converter/download.link");
            linkTrk.setText("Get the GPX file from the link");

            trkType.getLink().add(linkTrk);
            trkType.getTrkseg().add(trkSeqType);
            gpxType.getTrk().add(trkType);

            // create marshaller and save GPX data to XML document
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://www.topografix.com/GPX/1/1 " +
                    "http://www.topografix.com/GPX/1/1/gpx.xsd http://www.garmin.com/xmlschemas/GpxExtensions/v3 " +
                    "http://www.garmin.com/xmlschemas/GpxExtensionsv3.xsd " +
                    "http://www.garmin.com/xmlschemas/TrackPointExtension/v1 http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd");
//            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            QName qName = new QName("http://www.topografix.com/GPX/1/1","gpx");

            marshaller.marshal(new JAXBElement(qName, GpxType.class, gpxType), out);

            logger.info("File '" + OUT_FILENAME + "' has been saved.");
        } catch (DatatypeConfigurationException | JAXBException | IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    private static void loadGpxData() {

    }
}
