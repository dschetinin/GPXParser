package com.gpxparser;

import com.gpxparser.dto.SrtDataBlock;
import com.gpxparser.jaxb.*;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * User: dschetinin <a href="mailto:schetinin.d@gmail.com"/>schetinin.d@gmail.com</a>
 * Date: 10/3/2016
 * Time: 5:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class GpxFileWriter {
    private static final Logger logger = LogManager.getLogger(GpxFileWriter.class);

    protected void writeSrtPointToGpxFile(List<SrtDataBlock> srtList, String filePath) throws IOException {
        try (FileOutputStream out = new FileOutputStream(filePath)) {

            // create JAXB object factory to manage JAXB elements creation
            JAXBContext jc = JAXBContext.newInstance("com.gpxparser.jaxb");
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

            logger.info("File '" + filePath + "' has been saved.");
        } catch (DatatypeConfigurationException | JAXBException | IOException e) {
            e.printStackTrace();
        } finally {

        }

    }

}
