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
     *
     * Returns the total goals scored by a team in a given year.
     *
     * Logic:
     * 1. Query football_matches endpoint with team1=<team> and year=<year>
     *    - This gets all matches where the team was the HOME team (team1)
     *    - Results are paginated, so loop through all pages
     *    - Sum up all team1goals from each match
     *
     * 2. Query football_matches endpoint with team2=<team> and year=<year>
     *    - This gets all matches where the team was the AWAY team (team2)
     *    - Results are paginated, so loop through all pages
     *    - Sum up all team2goals from each match
     *
     * 3. Return homeGoals + awayGoals for the final answer
     *
     * Why two queries?
     * The API treats team1 and team2 as separate parameters. A team's total
     * goals = goals when home (team1) + goals when away (team2).
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
     *
     * Returns the count of matches that ended in a draw for a given year.
     *
     * A draw = team1goals === team2goals (same score on both sides)
     *
     * Logic:
     * 1. Possible draw scores range from 0-0 to 10-10 (11 different scores)
     *
     * 2. For each possible draw score i (where i = 0 to 10):
     *    - Query football_matches with team1goals=i and team2goals=i
     *    - This returns all matches that ended with that exact draw score
     *    - Extract the "total" field = number of matches with this score
     *    - Add to running totalDraws
     *
     * 3. Return the sum of all draws across all score combinations
     *
     * Why this approach?
     * The API allows us to filter by both team1goals and team2goals together.
     * By querying for each possible draw score and summing the results, we
     * get the total number of drawn matches for that year.
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
