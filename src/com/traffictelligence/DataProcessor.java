package com.traffictelligence;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class DataProcessor {

    public List<TrafficData> loadData(String filePath) {

        List<TrafficData> dataList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {

                // Skip CSV header
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] values = line.split(",");

                if (values.length < 8) {
                    continue;
                }

                String holiday = values[0].trim();
                double temp = Double.parseDouble(values[1].trim());
                double rain = Double.parseDouble(values[2].trim());
                double snow = Double.parseDouble(values[3].trim());
                String weather = values[4].trim();
                String date = values[5].trim();
                String time = values[6].trim();
                double trafficVolume = Double.parseDouble(values[7].trim());

                TrafficData trafficData = new TrafficData(
                        holiday,
                        temp,
                        rain,
                        snow,
                        weather,
                        date,
                        time,
                        trafficVolume
                );

                dataList.add(trafficData);
            }

        } catch (Exception e) {
            System.out.println("Error while reading dataset: " + e.getMessage());
        }

        return dataList;
    }
}
