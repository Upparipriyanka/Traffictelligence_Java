package com.traffictelligence;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TrafficPredictor {

    private List<TrafficData> trainingData;
    private int k = 5;

    public TrafficPredictor(List<TrafficData> trainingData) {
        this.trainingData = trainingData;
    }

    public PredictionResult predict(
            String holiday,
            double temp,
            double rain,
            double snow,
            String weather,
            String date,
            String time) {

        List<DistanceRecord> distances = new ArrayList<>();

        for (TrafficData data : trainingData) {

            double distance = calculateDistance(
                    holiday, temp, rain, snow, weather, date, time,
                    data
            );

            distances.add(
                    new DistanceRecord(distance, data.getTrafficVolume())
            );
        }

        distances.sort(Comparator.comparingDouble(DistanceRecord::getDistance));

        int numberOfNeighbors = Math.min(k, distances.size());

        double totalTraffic = 0;

        for (int i = 0; i < numberOfNeighbors; i++) {
            totalTraffic += distances.get(i).getTrafficVolume();
        }

        double prediction = totalTraffic / numberOfNeighbors;

        return new PredictionResult(prediction);
    }

    private double calculateDistance(
            String holiday,
            double temp,
            double rain,
            double snow,
            String weather,
            String date,
            String time,
            TrafficData data) {

        double distance = 0;

        distance += Math.pow(
                encodeHoliday(holiday) - encodeHoliday(data.getHoliday()), 2);

        distance += Math.pow(temp - data.getTemp(), 2);

        distance += Math.pow(rain - data.getRain(), 2);

        distance += Math.pow(snow - data.getSnow(), 2);

        distance += Math.pow(
                encodeWeather(weather) - encodeWeather(data.getWeather()), 2);

        distance += Math.pow(
                encodeDate(date) - encodeDate(data.getDate()), 2);

        distance += Math.pow(
                encodeTime(time) - encodeTime(data.getTime()), 2);

        return Math.sqrt(distance);
    }

    private int encodeHoliday(String holiday) {
        if (holiday == null || holiday.equalsIgnoreCase("None")) {
            return 0;
        }
        return 1;
    }

    private int encodeWeather(String weather) {

        if (weather == null) {
            return 0;
        }

        String value = weather.toLowerCase();

        if (value.contains("clear")) {
            return 1;
        } else if (value.contains("cloud")) {
            return 2;
        } else if (value.contains("rain")) {
            return 3;
        } else if (value.contains("snow")) {
            return 4;
        } else if (value.contains("mist") || value.contains("fog")) {
            return 5;
        }

        return 0;
    }

    private int encodeDate(String date) {
        if (date == null || date.length() < 10) {
            return 0;
        }

        try {
            String[] parts = date.split("-");

            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);

            return year * 10000 + month * 100 + day;

        } catch (Exception e) {
            return 0;
        }
    }

    private int encodeTime(String time) {

        if (time == null || time.length() < 5) {
            return 0;
        }

        try {
            String[] parts = time.split(":");

            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);

            return hour * 60 + minute;

        } catch (Exception e) {
            return 0;
        }
    }

    private static class DistanceRecord {

        private double distance;
        private double trafficVolume;

        public DistanceRecord(double distance, double trafficVolume) {
            this.distance = distance;
            this.trafficVolume = trafficVolume;
        }

        public double getDistance() {
            return distance;
        }

        public double getTrafficVolume() {
            return trafficVolume;
        }
    }
}
