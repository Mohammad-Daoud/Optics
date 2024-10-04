package com.project.optics.configurations;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class) // This catches all exceptions
    public ModelAndView handleException(Exception ex, Model model) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", ex.getMessage()); // Pass the exception message to the view
        mav.setViewName("error"); // This is the error page
        return mav;
    }
}
