package com.gpxparser.controller;

import com.gpxparser.jaxb.GpxType;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping("/123")
    public String index() {
        return "DJI Phantom 3 professional converter.<br/>It converts Phantom .SRT files to tomographic .GPX files.<br/>Simply send a POST request to URL /converter and get a result back in .GPX";
    }

    @RequestMapping(value = "/hellao" , method = RequestMethod.GET)
    public String hello(@RequestParam(name = "name", required = false) String name, Model model) {
        model.addAttribute("name", name);
        return "hello";
    }

    @RequestMapping(value = "/xml/{fileName:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody GpxType simpleXml(@PathVariable(name = "fileName") String fileName, @ModelAttribute("gpxDataMap") Map<String, GpxType> gpxDataMap) {
        return gpxDataMap.get(fileName);
    }
}
