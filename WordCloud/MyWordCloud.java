import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.image.AngleGenerator;
import com.kennycason.kumo.palette.ColorPalette;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MyWordCloud {

    /*
	輸入:搜尋框裡面的字串
 	輸出:在當前的資料夾新增 worldcloud.png
    */

	
    // 資料庫資訊
    static String url = "jdbc:mysql://127.0.0.16/word";
	static String username = "root";
	static String password = "root";

    // 產生的文字雲png輸出位置
    static String outputPath = "./wordcloud.png";

    static PreparedStatement pstmtInsertOrUpdate = null;


    /*  建立資料庫SQL語法
        create database `word`;
        use `word`;

        create table `count_search`(
	        `word` varchar(20) unique,
            `count` INT default 1
        );
     */

    public static void generateWordCloud(String inputText){
        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            String insertOrUpdateSQL = "INSERT INTO count_search (word, count) VALUES (?, 1) ON DUPLICATE KEY UPDATE count = count + 1";
            pstmtInsertOrUpdate = connection.prepareStatement(insertOrUpdateSQL);
            pstmtInsertOrUpdate.setString(1, inputText);
            pstmtInsertOrUpdate.executeUpdate();

            List<WordFrequency> wordFrequencies = getDataFromDatabase(connection);
            generateWordCloud(wordFrequencies);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static List<WordFrequency> getDataFromDatabase(Connection connection) throws SQLException {
        List<WordFrequency> wordFrequencies = new ArrayList<>();

        String query = "SELECT word, count FROM count_search";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                
                String word = resultSet.getString("word");
                int frequency = resultSet.getInt("count");

                wordFrequencies.add(new WordFrequency(word, frequency));
            }
        }

        return wordFrequencies;
    }

    private static void generateWordCloud(List<WordFrequency> wordFrequencies) {

        Dimension dimension = new Dimension(600, 600);
        WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setBackground(new CircleBackground(300));
        wordCloud.setBackgroundColor(Color.WHITE);
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
        wordCloud.setFontScalar(new LinearFontScalar(20, 100));
        wordCloud.setKumoFont(new KumoFont(new Font("SimSun", Font.PLAIN, 1)));
        wordCloud.setAngleGenerator(new AngleGenerator(0));
        wordCloud.build(wordFrequencies);

        try {
            wordCloud.writeToFile(outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
