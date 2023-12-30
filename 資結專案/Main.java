import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Scanner sc = new Scanner(System.in);
        // String inputText = sc.nextLine();

        File file = new File("input.txt");

        try (Scanner fileSc = new Scanner(file)) {
            KeywordList keywordList = new KeywordList();

            System.out.println(fileSc.hasNext());
            System.out.println(fileSc.hasNextLine());

            System.out.println("Next token: " + fileSc.next());

            while (fileSc.hasNext()) {
                System.out.println("in");
                String name = fileSc.next();
                float weight = fileSc.nextFloat();
                Keyword k = new Keyword(name, weight);
                keywordList.add(k);
                System.out.println("Name: " + name + ", Weight: " + weight);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
            // sc.close();
    }
}