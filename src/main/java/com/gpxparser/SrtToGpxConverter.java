package com.gpxparser;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication
public class SrtToGpxConverter {

    private static final Logger logger = LogManager.getLogger(SrtToGpxConverter.class);

    private static String FILES_FOLDER = "C:\\GIT_Projects\\GPXParser\\files";

    private static String IN_FILENAME = "DJI_0179.SRT";
//    private static String IN_FILENAME = "C:\\GIT_Projects\\GPXParser\\files\\runtastic_20160109_1147_Беговые лыжи.gpx";
//    private static String IN_FILENAME = "D:\\GIT_Projects\\GPXParser\\files\\runtastic_20160109_1147_Беговые_лыжи.gpx";
//    private static String OUT_FILENAME = "C:\\GIT_Projects\\GPXParser\\files\\12jan2016_out_3.gpx";
    private static String OUT_FILENAME = "DJI_0179.gpx";


    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(SrtToGpxConverter.class, args);

        // validate log4j java VM option on input
        String log4jProperty = System.getProperty("log4j.configuration");
        if (log4jProperty == null || log4jProperty.length() <= 0) {
            System.out.println("log4j.configuration system property not found, using default one");
            Properties props = new Properties();
            try (InputStream in = SrtToGpxConverter.class.getClassLoader().getResourceAsStream("log4j.properties")) {
                props.load(in);
            } catch (IOException e) {
                System.err.println("Can not load configuration file ");
                e.printStackTrace(System.err);
            }
            LogManager.resetConfiguration();
            PropertyConfigurator.configure(props);
        }
        // run the converter itself
/*
        try {

            SrtFileReaderLambda fileReaderLambda = new SrtFileReaderLambda();
            List<SrtDataBlock> srtList = fileReaderLambda.getPointListFromSrtFile(FILES_FOLDER + File.separator + IN_FILENAME);

            GpxFileWriter fileWriter = new GpxFileWriter();
            fileWriter.writeSrtPointToGpxFile(srtList, FILES_FOLDER + File.separator + OUT_FILENAME);

        } catch (IOException e) {
            logger.error("Exception occured in main() : ", e);
        } finally {

        }

        Jaxb2Marshaller marshaller = (Jaxb2Marshaller) ctx.getBean("jaxbMarshaller");
        ObjectFactory objFactory = new ObjectFactory();
        GpxType gpx = objFactory.createGpxType();
        gpx.setCreator("Spring MVC");
        gpx.setVersion("1.1");
        QName qName = new QName("http://www.topografix.com/GPX/1/1","gpx");
//        marshaller.marshal(new JAXBElement(qName, GpxType.class, gpx), new StreamResult(System.out) );
        marshaller.marshal(gpx, new StreamResult(System.out) );
*/
    }
}
