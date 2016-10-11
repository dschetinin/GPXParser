package com.gpxparser.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * User: dschetinin <a href="mailto:schetinin.d@gmail.com"/>schetinin.d@gmail.com</a>
 * Date: 10/11/2016
 * Time: 6:07 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class WebViewController {

    @RequestMapping("/intro")
    public String intro(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("text", "DJI Phantom 3 professional converter.<br/>It converts Phantom .SRT files to tomographic .GPX files.<br/>Simply send a POST request to URL /converter and get a result back in .GPX");
        return "intro";
    }

    @RequestMapping("/hello")
    public String hello(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "hello";
    }

}
