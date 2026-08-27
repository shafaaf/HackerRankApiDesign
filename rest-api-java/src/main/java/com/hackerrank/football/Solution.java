import java.io.*;

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String team = bufferedReader.readLine();
        int year = Integer.parseInt(bufferedReader.readLine().trim());

        try {
            int result = Result.getTotalGoals(team, year);
            bufferedWriter.write(String.valueOf(result));
            bufferedWriter.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        }

        bufferedReader.close();
        bufferedWriter.close();
    }
}
