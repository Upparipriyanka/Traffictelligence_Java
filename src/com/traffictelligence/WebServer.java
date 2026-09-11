package com.traffictelligence;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebServer {

    public static void main(String[] args) throws Exception {

        String filePath = Paths.get("data", "traffic volume.csv").toString();

        DataProcessor processor = new DataProcessor();
        List<TrafficData> data = processor.loadData(filePath);

        if (data.isEmpty()) {
            System.out.println("Dataset could not be loaded.");
            return;
        }

        System.out.println("Dataset loaded successfully!");
        System.out.println("Total records: " + data.size());

        TrafficPredictor predictor = new TrafficPredictor(data);

        HttpServer server = HttpServer.create(
                new InetSocketAddress(8080), 0
        );

        server.createContext("/", exchange -> showHomePage(exchange));

        server.createContext("/predict",
                exchange -> handlePrediction(exchange, predictor));

        server.setExecutor(null);
        server.start();

        System.out.println();
        System.out.println("==========================================");
        System.out.println("   TRAFFICTELLIGENCE WEB APPLICATION");
        System.out.println("==========================================");
        System.out.println("Server started successfully!");
        System.out.println("Open your browser and visit:");
        System.out.println("http://localhost:8080");
        System.out.println("==========================================");
    }

    private static void showHomePage(HttpExchange exchange)
            throws IOException {

        String html = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Traffictelligence</title>

                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #f4f6f8;
                            margin: 0;
                            padding: 0;
                        }

                        .container {
                            width: 600px;
                            margin: 40px auto;
                            background: white;
                            padding: 30px;
                            border-radius: 10px;
                            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
                        }

                        h1 {
                            text-align: center;
                            color: #222;
                        }

                        .subtitle {
                            text-align: center;
                            color: #666;
                            margin-bottom: 30px;
                        }

                        label {
                            display: block;
                            margin-top: 15px;
                            font-weight: bold;
                        }

                        input, select {
                            width: 100%;
                            padding: 10px;
                            margin-top: 6px;
                            box-sizing: border-box;
                            border: 1px solid #ccc;
                            border-radius: 5px;
                        }

                        button {
                            width: 100%;
                            margin-top: 25px;
                            padding: 12px;
                            background-color: #222;
                            color: white;
                            border: none;
                            border-radius: 5px;
                            font-size: 16px;
                            cursor: pointer;
                        }

                        button:hover {
                            background-color: #444;
                        }

                        .info {
                            margin-top: 25px;
                            padding: 15px;
                            background-color: #f0f0f0;
                            border-radius: 5px;
                            color: #555;
                        }
                    </style>
                </head>

                <body>

                    <div class="container">

                        <h1>Traffictelligence</h1>

                        <div class="subtitle">
                            Advanced Traffic Volume Estimation Using Machine Learning
                        </div>

                        <form action="/predict" method="post">

                            <label>Holiday</label>
                            <select name="holiday">
                                <option value="None">None</option>
                                <option value="Yes">Yes</option>
                            </select>

                            <label>Temperature</label>
                            <input type="number"
                                   name="temp"
                                   step="0.01"
                                   value="288.28"
                                   required>

                            <label>Rain</label>
                            <input type="number"
                                   name="rain"
                                   step="0.01"
                                   value="0"
                                   required>

                            <label>Snow</label>
                            <input type="number"
                                   name="snow"
                                   step="0.01"
                                   value="0"
                                   required>

                            <label>Weather</label>
                            <select name="weather">
                                <option value="Clouds">Clouds</option>
                                <option value="Clear">Clear</option>
                                <option value="Rain">Rain</option>
                                <option value="Snow">Snow</option>
                                <option value="Mist">Mist</option>
                            </select>

                            <label>Date</label>
                            <input type="text"
                                   name="date"
                                   value="02-10-2012"
                                   placeholder="dd-MM-yyyy"
                                   required>

                            <label>Time</label>
                            <input type="text"
                                   name="time"
                                   value="09:00:00"
                                   placeholder="HH:mm:ss"
                                   required>

                            <button type="submit">
                                Predict Traffic Volume
                            </button>

                        </form>

                        <div class="info">
                            Enter the traffic conditions and click
                            <b>Predict Traffic Volume</b> to estimate
                            the number of vehicles.
                        </div>

                    </div>

                </body>
                </html>
                """;

        sendResponse(exchange, html);
    }

    private static void handlePrediction(
            HttpExchange exchange,
            TrafficPredictor predictor) throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            sendResponse(exchange, "Invalid request method.");
            return;
        }

        String requestBody = new String(
                exchange.getRequestBody().readAllBytes(),
                StandardCharsets.UTF_8
        );

        Map<String, String> formData = parseFormData(requestBody);

        try {

            String holiday = formData.get("holiday");

            double temp = Double.parseDouble(
                    formData.get("temp")
            );

            double rain = Double.parseDouble(
                    formData.get("rain")
            );

            double snow = Double.parseDouble(
                    formData.get("snow")
            );

            String weather = formData.get("weather");
            String date = formData.get("date");
            String time = formData.get("time");

            PredictionResult result = predictor.predict(
                    holiday,
                    temp,
                    rain,
                    snow,
                    weather,
                    date,
                    time
            );

            String html = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <title>Prediction Result</title>

                        <style>
                            body {
                                font-family: Arial, sans-serif;
                                background-color: #f4f6f8;
                                text-align: center;
                                padding-top: 100px;
                            }

                            .result {
                                width: 500px;
                                margin: auto;
                                background: white;
                                padding: 40px;
                                border-radius: 10px;
                                box-shadow: 0 4px 12px rgba(0,0,0,0.15);
                            }

                            h1 {
                                color: #222;
                            }

                            .prediction {
                                font-size: 28px;
                                font-weight: bold;
                                margin: 30px 0;
                            }

                            a {
                                display: inline-block;
                                padding: 12px 20px;
                                background-color: #222;
                                color: white;
                                text-decoration: none;
                                border-radius: 5px;
                            }
                        </style>
                    </head>

                    <body>

                        <div class="result">

                            <h1>Prediction Result</h1>

                            <div class="prediction">
                                %s
                            </div>

                            <a href="/">
                                Make Another Prediction
                            </a>

                        </div>

                    </body>
                    </html>
                    """.formatted(result.toString());

            sendResponse(exchange, html);

        } catch (Exception e) {

            String errorPage = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <title>Error</title>
                    </head>

                    <body style="font-family:Arial; text-align:center; padding:80px;">

                        <h1>Prediction Error</h1>

                        <p>Please check the values you entered.</p>

                        <p>Error: %s</p>

                        <a href="/">Go Back</a>

                    </body>
                    </html>
                    """.formatted(e.getMessage());

            sendResponse(exchange, errorPage);
        }
    }

    private static Map<String, String> parseFormData(String body) {

        Map<String, String> data = new HashMap<>();

        String[] pairs = body.split("&");

        for (String pair : pairs) {

            String[] keyValue = pair.split("=", 2);

            if (keyValue.length == 2) {

                String key = URLDecoder.decode(
                        keyValue[0],
                        StandardCharsets.UTF_8
                );

                String value = URLDecoder.decode(
                        keyValue[1],
                        StandardCharsets.UTF_8
                );

                data.put(key, value);
            }
        }

        return data;
    }

    private static void sendResponse(
            HttpExchange exchange,
            String response) throws IOException {

        byte[] responseBytes =
                response.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set(
                "Content-Type",
                "text/html; charset=UTF-8"
        );

        exchange.sendResponseHeaders(
                200,
                responseBytes.length
        );

        try (OutputStream outputStream =
                     exchange.getResponseBody()) {

            outputStream.write(responseBytes);
        }
    }
}
