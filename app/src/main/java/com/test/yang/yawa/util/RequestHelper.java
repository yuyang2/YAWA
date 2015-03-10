package com.test.yang.yawa.util;

public class RequestHelper {
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?q=%s&mode=json&units=metric&cnt=7";
    private static final String ICON_URL = "http://openweathermap.org/img/w/%s.png";

    public static String getApiUrl() {
        return API_URL;
    }

    public static String getIconUrl() {
        return ICON_URL;
    }
}
