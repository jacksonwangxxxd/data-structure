import java.util.ArrayList;

public class KeywordList {
    private ArrayList<Keyword> list;

    public KeywordList() {
        this.list = new ArrayList<Keyword>();
    }

    public void add(Keyword keyword) {
        list.add(keyword);
    }

    // 遍歷所有關鍵字，找到與輸入字串具有最長 LCS 的關鍵字
    public void find(String s) {
        int maxValue = -1;
        Keyword LCS = null;

        for (Keyword k : list) {
            int lcs = findLCS(k.name, s);
            if (lcs > maxValue) {
                maxValue = lcs;
                LCS = k;
            }
        }
        // 回傳最符合的關鍵字
        System.out.println(s + ": " + LCS.toString());
    }

    public int findLCS(String x, String y) {
        int m = x.length();
        int n = y.length();

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0 || j == 0) {
                    dp[i][j] = 0;
                }
                else if (x.charAt(i - 1) == y.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                }
                else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[m][n];
    }

    private void printMatrix(int[][] matrix) {
        // 輸出二維數組的方法，用於調試
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + " ");
                if (j == matrix[0].length - 1)
                    System.out.print("\n");
            }
        }
    }
}
