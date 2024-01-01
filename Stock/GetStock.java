import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetStock {

    public static void main(String[] args) {
    	// 到 https://www.alphavantage.co/ 點選 GET FREE API KEY 取得金鑰
        String apiKey = "3NL508Z2BKD9KVXD";
        // 放進想查詢的股票名稱
        String[] symbols = {"AAPL", "TSLA", "NVDA", "GOOG", "META", "MSFT", "2330.TW", "2454.TW", "2317.TW", "2412.TW", "2382.TW", "2881.TW"};
//        String[] symbols_TW = {"2330.TW", "2454.TW", "2317.TW", "2412.TW", "2382.TW", "2881.TW"};
        
        getStock(apiKey, symbols);

    }

	public static void getStock(String apiKey, String[] symbols) {
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
                    String changePercent = globalQuote.optString("10. change percent", "N/A");
                    changePercent.substring(0, changePercent.length());

//                    System.out.println("股票名稱: " + stockName);
//                    System.out.println("當前價格: " + currentPrice);
//                    System.out.println("當日漲跌幅: " + changePercent);
//                    System.out.println("------------------------");
                    
                    System.out.printf("股票名稱: %s\n", stockName);
                    System.out.printf("當前價格: %.2f\n", Double.valueOf(currentPrice));
                    System.out.printf("當日漲跌幅: %.2f%\n", Double.valueOf(changePercent.substring(0, changePercent.length())));
                    
                } else {
                    System.out.println("股票查詢失敗");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}
}
