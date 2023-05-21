package com.therealazimbek.spring.currencybot.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrentRateFromApi {

    public static double getCurrentRate() {
        double currentRate = 0.0;
        try {
            URL url = new URL("https://api.currencyapi.com/v3/latest?apikey=H84Y26ceQP67BJs4APAzzpShKI6GxuRX8XATMYja&currencies=KZT");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                int value = response.indexOf("value");
                String substring = response.substring(value, response.length() - 1);

                String pattern = "-?\\d+(\\.\\d+)?";
                Pattern regex = Pattern.compile(pattern);
                Matcher matcher = regex.matcher(substring);


                while (matcher.find()) {
                    String match = matcher.group();
                    System.out.println("Up-to-date current rate: " + Double.parseDouble(match));
                    currentRate = Double.parseDouble(match);
                }

            } else {
                System.out.println("Failed to retrieve the exchange rate. Response code: " + responseCode);
            }

            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return currentRate;
    }
}
