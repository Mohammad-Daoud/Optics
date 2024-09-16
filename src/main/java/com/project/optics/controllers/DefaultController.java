package com.project.optics.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultController {

    @RequestMapping("/")
    public String defaultRequest(){
        return "redirect:/clients";
    }

}
