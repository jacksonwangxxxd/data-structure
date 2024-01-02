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
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class SearchController {

    @GetMapping("/search")
    public String search(@RequestParam String query) {
        String googleSearchUrl = "https://www.google.com/search?q=" + query;

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

                results.add(resultObject);
            }

            JSONArray resultsArray = new JSONArray(results);
            System.out.println(resultsArray);
            return resultsArray.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"error\": \"Error fetching search results\"}";
        }
    }
}
