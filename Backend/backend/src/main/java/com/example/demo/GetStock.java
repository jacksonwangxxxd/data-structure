package com.example.demo;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class GetStock {

    @GetMapping("/getStock")
    public static String[] getStock() {
        String apiKey = "3NL508Z2BKD9KVXD";
        String[] symbols = { "AAPL", "TSLA", "NVDA", "GOOG", "META", "MSFT" };
        // String[] symbols_TW = {"2330.TW", "2454.TW", "2317.TW", "2412.TW", "2382.TW",
        // "2881.TW"};

        for (String symbol : symbols) {
            try {
                String apiUrl = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey="
                        + apiKey;
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                connection.disconnect();

                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONObject globalQuote = jsonResponse.optJSONObject("Global Quote");

                if (globalQuote != null && !globalQuote.isEmpty()) {
                    String name = globalQuote.optString("01. symbol", "N/A");
                    String price = globalQuote.optString("05. price", "N/A");
                    String change = globalQuote.optString("10. change percent", "N/A");
                    change = change.substring(0, change.length() - 3);
                    change += "%";

                    String[] result = { name, price, change };
                    return result;

                } else {
                    System.out.println("股票查詢失敗");

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
