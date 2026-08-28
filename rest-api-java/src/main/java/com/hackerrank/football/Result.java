package com.hackerrank.football;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Result {
    private static final String BASE_URL = "https://jsonmock.hackerrank.com/api";
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    /**
     * getTotalGoals(team, year)
     * Returns the total goals scored by a team in a given year.
     * Must query both team1 (home) and team2 (away) positions.
     */
    public static int getTotalGoals(String team, int year) throws Exception {
        int homeGoals = 0;
        int awayGoals = 0;

        // 1. Fetch all matches where team is team1 (home)
        int page = 1;
        while (true) {
            String url = String.format(
                "%s/football_matches?year=%d&team1=%s&page=%d",
                BASE_URL, year, urlEncode(team), page
            );

            HttpResponse<String> response = httpClient.send(
                HttpRequest.newBuilder().uri(new URI(url)).GET().build(),
                HttpResponse.BodyHandlers.ofString()
            );

            // Parse response using JsonParser
            JsonObject body = JsonParser
                .parseString(response.body())
                .getAsJsonObject();

            // Sum team1goals from this page
            JsonArray data = body.getAsJsonArray("data");
            for (JsonElement element : data) {
                JsonObject match = element.getAsJsonObject();
                homeGoals += Integer.parseInt(match.get("team1goals").getAsString());
            }

            // Check if there are more pages
            int totalPages = body.get("total_pages").getAsInt();
            if (page >= totalPages) break;
            page++;
        }

        // 2. Fetch all matches where team is team2 (away)
        page = 1;
        while (true) {
            String url = String.format(
                "%s/football_matches?year=%d&team2=%s&page=%d",
                BASE_URL, year, urlEncode(team), page
            );

            HttpResponse<String> response = httpClient.send(
                HttpRequest.newBuilder().uri(new URI(url)).GET().build(),
                HttpResponse.BodyHandlers.ofString()
            );

            // Parse response using JsonParser
            JsonObject body = JsonParser
                .parseString(response.body())
                .getAsJsonObject();

            // Sum team2goals from this page
            JsonArray data = body.getAsJsonArray("data");
            for (JsonElement element : data) {
                JsonObject match = element.getAsJsonObject();
                awayGoals += Integer.parseInt(match.get("team2goals").getAsString());
            }

            // Check if there are more pages
            int totalPages = body.get("total_pages").getAsInt();
            if (page >= totalPages) break;
            page++;
        }

        return homeGoals + awayGoals;
    }

    /**
     * getNumDraws(year)
     * Returns the count of matches that ended in a draw (same score both sides).
     * Queries for each possible draw score (0-0, 1-1, ..., 10-10).
     */
    public static int getNumDraws(int year) throws Exception {
        int totalDraws = 0;

        // Query for each possible draw score (0-0 through 10-10)
        for (int i = 0; i <= 10; i++) {
            String url = String.format(
                "%s/football_matches?year=%d&team1goals=%d&team2goals=%d",
                BASE_URL, year, i, i
            );

            HttpResponse<String> response = httpClient.send(
                HttpRequest.newBuilder().uri(new URI(url)).GET().build(),
                HttpResponse.BodyHandlers.ofString()
            );

            // Parse response using JsonParser
            JsonObject body = JsonParser
                .parseString(response.body())
                .getAsJsonObject();

            totalDraws += body.get("total").getAsInt();
        }

        return totalDraws;
    }

    // Helper to URL-encode strings (spaces become %20)
    private static String urlEncode(String s) {
        return s.replace(" ", "%20");
    }
}
