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
    	public static ArrayList<String> getStock() {

        ArrayList<String> stockList = new ArrayList<String>();
        String apiKey = "JM1N9CEPJO8R6R3G";
        String[] symbols = {"AAPL", "TSLA", "NVDA", "GOOG", "META"};
        
        for (String symbol : symbols) {
            try {
                String apiUrl = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + apiKey;
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
                    String stockName = globalQuote.optString("01. symbol", "N/A");
                    String currentPrice = globalQuote.optString("05. price", "N/A");
                    currentPrice = String.format("$%.2f", Double.valueOf(currentPrice));
                    String changePercent = globalQuote.optString("10. change percent", "N/A");
                    changePercent = String.format("%.2f%%", Double.valueOf(changePercent.substring(0, changePercent.length()-1)));

                    stockList.add(stockName + " " + currentPrice + " " + changePercent);
                    
                } else {
                    System.out.println("股票查詢失敗");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return stockList;
	}
}
