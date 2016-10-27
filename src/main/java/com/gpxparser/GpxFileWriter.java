package com.gpxparser;

import com.gpxparser.dto.SrtDataBlock;
import com.gpxparser.jaxb.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
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

    public GpxType writeSrtPointToGpxFile(List<SrtDataBlock> srtList, String applicationRootUrl, String downloadFileName) throws IOException {
        GpxType gpxType = null;
        try {

            // create JAXB object factory to manage JAXB elements creation
            JAXBContext jc = JAXBContext.newInstance("com.gpxparser.jaxb");
            ObjectFactory objFactory = new ObjectFactory();

            gpxType = objFactory.createGpxType();
            gpxType.setCreator("SrtToGpx java conversion tool");
            gpxType.setVersion("1.1");

            MetadataType metadata = objFactory.createMetadataType();
            LinkType linkMetaData = objFactory.createLinkType();
            linkMetaData.setHref(applicationRootUrl);
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

            linkTrk.setHref(applicationRootUrl + "/xml/" + downloadFileName);
            linkTrk.setText("Get the GPX file from the link");

            trkType.getLink().add(linkTrk);
            trkType.getTrkseg().add(trkSeqType);
            gpxType.getTrk().add(trkType);

            logger.info("Conversion to GpxType JAXB object completed");
        } catch (DatatypeConfigurationException | JAXBException e) {
            e.printStackTrace();
        } finally {

        }
        return gpxType;
    }

}
