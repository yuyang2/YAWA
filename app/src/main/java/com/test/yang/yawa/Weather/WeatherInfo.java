package com.test.yang.yawa.Weather;

public class WeatherInfo {
    private final String date;
    private final String city;
    private final String minTemp;
    private final String maxTemp;
    private final String description;
    private final String icon;

    public WeatherInfo(final String date, final String city, final String minTemp, final String maxTemp, final String description, final String icon) {
        this.date = date;
        this.city = city;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.description = description;
        this.icon = icon;
    }

    public String getDate() {
        return date;
    }

    public String getCity() {
        return city;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

}
