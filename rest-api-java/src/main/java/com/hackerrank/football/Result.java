package com.hackerrank.football;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Result {
    private static final String BASE_URL = "https://jsonmock.hackerrank.com/api";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

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

            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
            );

            MatchResponse matchResponse = mapper.readValue(
                response.body(),
                MatchResponse.class
            );

            // Sum team1goals from this page
            for (MatchResponse.Match match : matchResponse.data) {
                homeGoals += Integer.parseInt(match.team1goals);
            }

            // Check if there are more pages
            if (page >= matchResponse.total_pages) {
                break;
            }
            page++;
        }

        // 2. Fetch all matches where team is team2 (away)
        page = 1;
        while (true) {
            String url = String.format(
                "%s/football_matches?year=%d&team2=%s&page=%d",
                BASE_URL, year, urlEncode(team), page
            );

            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
            );

            MatchResponse matchResponse = mapper.readValue(
                response.body(),
                MatchResponse.class
            );

            // Sum team2goals from this page
            for (MatchResponse.Match match : matchResponse.data) {
                awayGoals += Integer.parseInt(match.team2goals);
            }

            // Check if there are more pages
            if (page >= matchResponse.total_pages) {
                break;
            }
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

            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
            );

            MatchResponse matchResponse = mapper.readValue(
                response.body(),
                MatchResponse.class
            );

            totalDraws += matchResponse.total;
        }

        return totalDraws;
    }

    // Helper to URL-encode strings (spaces become %20)
    private static String urlEncode(String s) {
        return s.replace(" ", "%20");
    }
}
