package com.gpxparser.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpxparser.GpxFileWriter;
import com.gpxparser.dto.SrtDataBlock;
import com.gpxparser.jaxb.GpxType;
import com.gpxparser.jpa.GpxTracksHistoryEntity;
import com.gpxparser.json.Json;
import com.gpxparser.repository.GpxTracksDao;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * User: dschetinin <a href="mailto:schetinin.d@gmail.com"/>schetinin.d@gmail.com</a>
 * Date: 10/4/2016
 * Time: 3:20 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@SessionAttributes(names = {"gpxDataMap"}, types = {Map.class})
public class RestWSController {

    private static final Logger logger = LogManager.getLogger(RestWSController.class);

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonConverter;

    @Autowired
    private Environment environment;

    @Autowired
    private GpxTracksDao gpxTracksDao;

    @RequestMapping("/123")
    public String index() {
        return "DJI Phantom 3 professional converter.<br/>It converts Phantom .SRT files to tomographic .GPX files.<br/>Simply send a POST request to URL /converter and get a result back in .GPX";
    }

    // This method was developed as a prelude to DB data upload development
    @RequestMapping(value = "/jsonToXml/{fileName:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody GpxType jsonToXml(@PathVariable(name = "fileName") String fileName, @ModelAttribute("gpxDataMapJsonString") Map<String, String> gpxDataMapJsonString) {
        String jsonStr = gpxDataMapJsonString.get(fileName);
        ObjectMapper mapper = jacksonConverter.getObjectMapper();
        GpxType gpxType = null;

        try {
            gpxType = mapper.readValue(jsonStr, GpxType.class);
        } catch (IOException e) {
            logger.error("Unable to parse json string back to GpxType object because of exception :", e);
            try {
                logger.info("Configuring empty dummy object instead...");
                GpxFileWriter fileWriter = new GpxFileWriter();
                gpxType = fileWriter.writeSrtPointToGpxFile(new ArrayList<SrtDataBlock>(),
                        environment.getProperty("application.web.url"), environment.getProperty("application.web.defaultFile"));
            } catch (IOException e1) {
                logger.error("Unable to configure empty file, this is dead end :", e);
            }
        }
        return gpxType;
    }

    @RequestMapping(value = "/xml/{fileName:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody GpxType simpleXml(@PathVariable(name = "fileName") String fileName, @ModelAttribute("gpxDataMap") Map<String, GpxType> gpxDataMap) {
        return gpxDataMap.get(fileName);
    }

    @RequestMapping(value = "/json/{fileName:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody GpxType simpleJson(@PathVariable(name = "fileName") String fileName, @ModelAttribute("gpxDataMap") Map<String, GpxType> gpxDataMap) {
        return gpxDataMap.get(fileName);
    }

    @RequestMapping(value = "/json/example", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Json simpleJsonExample() {
        return new Json("{\"type\":\"object\", \"properties\":{\"foo\": {\"type\": \"string\"},\"bar\": {\"type\": \"integer\"},\"baz\": {\"type\": \"boolean\"}}}");
    }

    @RequestMapping(value = "/json/fromDB/{fileName:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Json loadJsonFromDB(@PathVariable(name = "fileName") String fileName) {
        GpxTracksHistoryEntity gpxTrack = gpxTracksDao.getByFileName(fileName);
        return new Json(gpxTrack.getGpxData());
    }
}
