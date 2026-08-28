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

        // ====================================================================
        // CHALLENGE 1: Total Goals by a Team
        // Uncomment the following block for Challenge 1
        // ====================================================================
        String team = bufferedReader.readLine();
        int year = Integer.parseInt(bufferedReader.readLine().trim());

        try {
            int result = Result.getTotalGoals(team, year);
            bufferedWriter.write(String.valueOf(result));
            bufferedWriter.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ====================================================================
        // CHALLENGE 2: Number of Drawn Matches
        // Uncomment the following block for Challenge 2
        // Comment out Challenge 1 block above
        // ====================================================================
        /*
        int year = Integer.parseInt(bufferedReader.readLine().trim());

        try {
            int result = Result.getNumDraws(year);
            bufferedWriter.write(String.valueOf(result));
            bufferedWriter.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        */

        bufferedReader.close();
        bufferedWriter.close();
    }
}
