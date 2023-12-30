import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GoogleQuery 
{
    public String inputKeyword;
    public String url;
    public String content;

    public GoogleQuery(String inputKeyword)
    {
        this.inputKeyword = inputKeyword;
        try 
        {
            String encodeKeyword=java.net.URLEncoder.encode(inputKeyword,"utf-8");
            this.url = "https://www.google.com/search?q="+encodeKeyword+"&oe=utf8&num=20";
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    // 從網頁抓取內容
    private String fetchContent() throws IOException
    {
        String retVal = "";

        URL u = new URL(url);
        URLConnection conn = u.openConnection();
        // 設置HTTP標頭
        conn.setRequestProperty("User-agent", "Chrome/107.0.5304.107");
        InputStream in = conn.getInputStream();

        InputStreamReader inReader = new InputStreamReader(in, "utf-8");
        BufferedReader bufReader = new BufferedReader(inReader);
        String line = null;

        while((line = bufReader.readLine()) != null)
        {
            retVal += line;
        }
        return retVal;
    }

    // 進行Google搜尋並解析結果
    public HashMap<String, String> query() throws IOException
    {
        if(content == null)
        {
            content = fetchContent();
        }

        HashMap<String, String> retVal = new HashMap<String, String>();
        
        // 使用Jsoup解析HTML字串
        Document doc = Jsoup.parse(content);
        
        // 選擇特定的元素（標籤），這裡選擇所有的 <div> 標籤
        Elements lis = doc.select("div");
        // 再進一步篩選擷取 class 為 "kCrYT" 的 <div> 元素
        lis = lis.select(".kCrYT");
        
        for(Element li : lis)
        {
            try 
            {
                // 取得<a>標籤中的href屬性，去掉"/url?q="部分，即是搜尋結果的URL
                String citeUrl = li.select("a").get(0).attr("href").replace("/url?q=", "");
                // 取得<a>標籤中class為"vvjwJb"的元素的文字內容，即是搜尋結果的標題
                String title = li.select("a").get(0).select(".vvjwJb").text();
                
                if(title.equals("")) 
                {
                    continue;
                }
                
                System.out.println("Title: " + title + " , url: " + citeUrl);
                System.out.println("\n" + "-------------------------------------------" + "\n");
                
                // 將標題和URL放入HashMap中
                retVal.put(title, citeUrl);

            } catch (IndexOutOfBoundsException e) 
            {
//              e.printStackTrace();
            }
        }
        // 回傳(標題, URL)的HashMap
        return retVal;
    }
}
