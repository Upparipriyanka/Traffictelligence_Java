package com.traffictelligence;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("==========================================");
        System.out.println("   TRAFFICTELLIGENCE");
        System.out.println("   Traffic Volume Estimation System");
        System.out.println("==========================================");

        String filePath = "data/traffic volume.csv";

        DataProcessor processor = new DataProcessor();

        System.out.println("\nLoading traffic dataset...");

        List<TrafficData> data = processor.loadData(filePath);

        if (data.isEmpty()) {
            System.out.println("Unable to load the dataset.");
            System.out.println("Please check the file path:");
            System.out.println(filePath);
            return;
        }

        System.out.println("Dataset loaded successfully!");
        System.out.println("Total records: " + data.size());

        TrafficPredictor predictor = new TrafficPredictor(data);

        Scanner scanner = new Scanner(System.in);

        System.out.println("\nEnter traffic information for prediction");

        System.out.print("Holiday (None/Yes): ");
        String holiday = scanner.nextLine();

        System.out.print("Temperature: ");
        double temp = scanner.nextDouble();

        System.out.print("Rain: ");
        double rain = scanner.nextDouble();

        System.out.print("Snow: ");
        double snow = scanner.nextDouble();

        scanner.nextLine();

        System.out.print("Weather (Clouds/Clear/Rain/Snow/Mist): ");
        String weather = scanner.nextLine();

        System.out.print("Date (dd-MM-yyyy): ");
        String date = scanner.nextLine();

        System.out.print("Time (HH:mm:ss): ");
        String time = scanner.nextLine();

        PredictionResult result = predictor.predict(
                holiday,
                temp,
                rain,
                snow,
                weather,
                date,
                time
        );

        System.out.println("\n==========================================");
        System.out.println("           PREDICTION RESULT");
        System.out.println("==========================================");

        System.out.println(result);

        System.out.println("==========================================");

        scanner.close();
    }
}
