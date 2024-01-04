package com.example.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class SearchController {

    @GetMapping("/search")
    public String search(@RequestParam String query) {
        String[] keywords = {
                "即時行情", "技術分析", "官方網站"
        };

        for (String k : keywords) {
            String googleSearchUrl = "https://www.google.com/search?q=" + query + k;

            try {
                Document doc = Jsoup.connect(googleSearchUrl).get();
                Elements searchResults = doc.select("div.g");

                List<JSONObject> results = new ArrayList<>();

                for (Element result : searchResults) {
                    String title = result.select("h3").text();
                    String link = result.select("a[href]").attr("href");

                    JSONObject resultObject = new JSONObject();
                    resultObject.put("title", title);
                    resultObject.put("link", link);

                    WordCounter counter = new WordCounter(link);
                    resultObject.put("score", counter.countKeyword(query));

                    results.add(resultObject);
                }

                JSONArray resultsArray = new JSONArray(results);
                List<JSONObject> jsonObjectList = resultsArray.toList().stream()
                        .map(obj -> (JSONObject) obj)
                        .collect(Collectors.toList());

                // 使用 Comparator 根據 "score" 進行排序
                jsonObjectList.sort(Comparator.comparing(obj -> obj.getString("score")));

                System.out.println(jsonObjectList);
                return resultsArray.getJSONObject(0).toString();
            } catch (IOException e) {
                e.printStackTrace();
                return "{\"error\": \"Error fetching search results\"}";
            }

        }

        return null;
    }
}
