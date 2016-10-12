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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * User: dschetinin <a href="mailto:schetinin.d@gmail.com"/>schetinin.d@gmail.com</a>
 * Date: 10/11/2016
 * Time: 6:07 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class WebViewController {

    private static final Logger logger = LogManager.getLogger(WebViewController.class);

    @RequestMapping("/intro")
    public String intro(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("fileUploadModel", new FileUploadModel());
        model.addAttribute("errorText", "");
        return "intro";
    }

    @RequestMapping(value = "/uploadSrtFile", method = RequestMethod.POST)
    public String uploadSrtFile(@ModelAttribute FileUploadModel fileUploadModel, Model model) {
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
            gpxType.getCreator();
        } catch (IOException e) {
            logger.error("Exception occured in controller ", e);
            errorText = "IOException occured while processing the file.";
        }


        if (errorText != null && errorText.length() > 0) {
            model.addAttribute("errorText", errorText);
            return "intro";
        }
        model.addAttribute("text", "File was successfully uploaded");
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

    @RequestMapping("/hello")
    public String hello(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "hello";
    }

}
