package com.project.sportyshoes.controller;

import javax.servlet.http.HttpServletRequest;

public class Utility {
    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        System.out.println("Reset PW Utility class invoked");
        return siteURL.replace(request.getServletPath(), "");
    }
}
