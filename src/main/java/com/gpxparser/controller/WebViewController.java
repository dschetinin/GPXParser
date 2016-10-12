package com.gpxparser.controller;

import com.gpxparser.GpxFileWriter;
import com.gpxparser.SrtFileReaderLambda;
import com.gpxparser.dto.SrtDataBlock;
import com.gpxparser.jaxb.GpxType;
import com.gpxparser.model.FileUploadModel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
@SessionAttributes(names = {"gpxDataMap"}, types = {Map.class})
public class WebViewController {

    private static final Logger logger = LogManager.getLogger(WebViewController.class);

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
        return "intro";
    }

    @RequestMapping(value = "/uploadSrtFile", method = RequestMethod.POST)
    public String uploadSrtFile(@ModelAttribute FileUploadModel fileUploadModel, @ModelAttribute("gpxDataMap") Map<String, GpxType> gpxDataMap, Model model) {
        // validate upload
        MultipartFile multipartFile = fileUploadModel.getFile();
        String errorText = validateMultipartFile(multipartFile);

        if (errorText != null && errorText.length() > 0) {
            model.addAttribute("errorText", errorText);
            return "intro";
        }

        try {
            SrtFileReaderLambda fileReader = new SrtFileReaderLambda();
            GpxFileWriter fileWriter = new GpxFileWriter();
            List<SrtDataBlock> srtList = fileReader.getPointListFromSrtFile(multipartFile.getInputStream());
            GpxType gpxType = fileWriter.writeSrtPointToGpxFile(srtList);
            String gpxFileName = multipartFile.getOriginalFilename().substring(0, multipartFile.getOriginalFilename().lastIndexOf(".")) + ".GPX";

            gpxDataMap.put(gpxFileName, gpxType);
            model.addAttribute("gpxFileName", gpxFileName);
        } catch (IOException e) {
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
