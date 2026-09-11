package com.traffictelligence;

public class TrafficData {

    private String holiday;
    private double temp;
    private double rain;
    private double snow;
    private String weather;
    private String date;
    private String time;
    private double trafficVolume;

    public TrafficData(String holiday, double temp, double rain, double snow,
                       String weather, String date, String time,
                       double trafficVolume) {

        this.holiday = holiday;
        this.temp = temp;
        this.rain = rain;
        this.snow = snow;
        this.weather = weather;
        this.date = date;
        this.time = time;
        this.trafficVolume = trafficVolume;
    }

    public String getHoliday() {
        return holiday;
    }

    public double getTemp() {
        return temp;
    }

    public double getRain() {
        return rain;
    }

    public double getSnow() {
        return snow;
    }

    public String getWeather() {
        return weather;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public double getTrafficVolume() {
        return trafficVolume;
    }
}
