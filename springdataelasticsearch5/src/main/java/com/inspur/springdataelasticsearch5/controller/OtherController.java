package com.inspur.springdataelasticsearch5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class OtherController {

    @RequestMapping("/page")
    public String index(Model model, HttpServletResponse response) {
        return "index";
    }
    @RequestMapping("/uploadpage")
    public String upload(Model model, HttpServletResponse response) {
        return "upload";
    }
    @RequestMapping("/searchpage")
    public String search(Model model, HttpServletResponse response) {
        return "search";
    }
}
