package com.hackerrank.football;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("[script] calling getTotalGoals(\"Barcelona\", 2011)");
            int totalGoals = Result.getTotalGoals("Barcelona", 2011);
            System.out.println("\nTotal goals by Barcelona in 2011: " + totalGoals);

            System.out.println("\n" +
                "=============================================================================\n");

            System.out.println("[script] calling getNumDraws(2011)");
            int numDraws = Result.getNumDraws(2011);
            System.out.println("\nTotal drawn matches in 2011: " + numDraws);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
