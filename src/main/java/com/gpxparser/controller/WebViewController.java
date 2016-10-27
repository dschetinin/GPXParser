package com.gpxparser.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpxparser.GpxFileWriter;
import com.gpxparser.SrtFileReaderLambda;
import com.gpxparser.dto.SrtDataBlock;
import com.gpxparser.jaxb.GpxType;
import com.gpxparser.model.FileUploadModel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: dschetinin <a href="mailto:schetinin.d@gmail.com"/>schetinin.d@gmail.com</a>
 * Date: 10/11/2016
 * Time: 6:07 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@SessionAttributes(names = {"gpxDataMap", "gpxDataMapJsonString"}, types = {Map.class, String.class})
public class WebViewController {

    private static final Logger logger = LogManager.getLogger(WebViewController.class);

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonConverter;

    @Autowired
    private Environment environment;

    @RequestMapping("/")
    public String index() {
        return "redirect:/intro";
    }

    @RequestMapping("/intro")
    public String intro(Model model, HttpServletRequest request) {
        model.addAttribute("fileUploadModel", new FileUploadModel());
        model.addAttribute("errorText", "");
        HttpSession session = request.getSession();
        if (session != null && session.getAttribute("gpxDataMap") == null) {
            session.setAttribute("gpxDataMap", new HashMap<String, GpxType>());
        }
        if (session != null && session.getAttribute("gpxDataMapJsonString") == null) {
            session.setAttribute("gpxDataMapJsonString", new HashMap<String, String>());
        }
        return "intro";
    }

    @RequestMapping(value = "/uploadSrtFile", method = RequestMethod.POST)
    public String uploadSrtFile(@ModelAttribute FileUploadModel fileUploadModel,
                                @ModelAttribute("gpxDataMap") Map<String, GpxType> gpxDataMap,
                                @ModelAttribute("gpxDataMapJsonString") Map<String, String> gpxDataMapJsonString,
                                Model model) {
        // validate upload
        MultipartFile multipartFile = fileUploadModel.getFile();
        String errorText = validateMultipartFile(multipartFile);

        if (errorText != null && errorText.length() > 0) {
            model.addAttribute("errorText", errorText);
            return "intro";
        }
        String originalFileName = multipartFile.getOriginalFilename();

        try {
            String gpxFileName = originalFileName.substring(0, originalFileName.lastIndexOf(".")) + ".GPX";
            String xmlFileName = originalFileName.substring(0, originalFileName.lastIndexOf(".")) + ".XML";
            String applicationRootUrl = environment.getProperty("application.web.url");

            // Parse .SRT file and create .GPX
            SrtFileReaderLambda fileReader = new SrtFileReaderLambda();
            GpxFileWriter fileWriter = new GpxFileWriter();
            List<SrtDataBlock> srtList = fileReader.getPointListFromSrtFile(multipartFile.getInputStream());
            GpxType gpxType = fileWriter.writeSrtPointToGpxFile(srtList, applicationRootUrl, xmlFileName);

            // Put .GPX file int session
            gpxDataMap.put(gpxFileName, gpxType);
            model.addAttribute("gpxFileName", gpxFileName);

            // This part was developed as a prelude to DB data upload development
            // see RestWSController#jsonToXml method
            ObjectMapper mapper = jacksonConverter.getObjectMapper();
            // Put json string into a session object
            gpxDataMapJsonString.put(gpxFileName, mapper.writeValueAsString(gpxType));
            logger.warn(String.format("Converted JSON string : %s", gpxDataMapJsonString.get(gpxFileName)));
        } catch (IOException | BeansException e) {
            logger.error("Exception occured in controller ", e);
            errorText = "IOException occured while processing the file.";
        }

        if (errorText != null && errorText.length() > 0) {
            model.addAttribute("errorText", errorText);
            return "intro";
        }
        model.addAttribute("text", "File was successfully uploaded and parsed.");
        return "successUpload";
    }

    @RequestMapping("/status")
    public String status(Model model, HttpServletRequest request) {
        int totalFilesProcessed = 0;

        HttpSession session = request.getSession();
        if (session != null && session.getAttribute("gpxDataMap") != null) {
            Map<String, GpxType> gpxDataMap = (Map<String, GpxType>) session.getAttribute("gpxDataMap");
            if (gpxDataMap.size() > 0) {
                totalFilesProcessed = gpxDataMap.size();
                model.addAttribute("fileNames", gpxDataMap.keySet());
            }
        }
        model.addAttribute("totalFilesProcessed", Integer.toString(totalFilesProcessed));
        return "status";
    }


    private String validateMultipartFile (MultipartFile multipartFile) {
        String errorText = null;
        try {
            if (multipartFile == null || multipartFile.getBytes().length == 0) {
                errorText = "Unable to process empty files, please upload non-empty .SRT file";
            } else if (!"SRT".equalsIgnoreCase(multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1, multipartFile.getOriginalFilename().length()))) {
                errorText = "Unable to process empty non .SRT files, please upload non-empty .SRT file";
            }

        } catch (IOException e) {
            logger.error("Exception occured in controller ", e);
            errorText = "IOException occured while submitting the file.";
        }
        return errorText;
    }
}
