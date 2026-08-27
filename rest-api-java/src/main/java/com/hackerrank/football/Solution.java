package com.hackerrank.football;

import java.io.*;

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        // Support both file output (HackerRank) and console output (local testing)
        BufferedWriter bufferedWriter;
        String outputPath = System.getenv("OUTPUT_PATH");

        if (outputPath != null) {
            bufferedWriter = new BufferedWriter(new FileWriter(outputPath));
        } else {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));
        }

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
