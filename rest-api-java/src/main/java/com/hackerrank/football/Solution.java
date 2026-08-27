package com.hackerrank.football;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * jsonmock football API practice.
 *
 * Two endpoints, both paginated. Every response looks like:
 *   { page, per_page, total, total_pages, data: [ ...up to 10 rows... ] }
 *
 * -- football_matches ---------------------------------------------------------
 *   GET https://jsonmock.hackerrank.com/api/football_matches?year=2011&team1goals=1&team2goals=1
 *   Filter params: year, competition, team1, team2, team1goals, team2goals, page
 *
 * -- football_competitions --------------------------------------------------
 *   GET https://jsonmock.hackerrank.com/api/football_competitions?year=2011&name=UEFA%20Champions%20League
 *   Filter params: year, name
 *
 * Reading a response:
 *   - Make an HTTP request with HttpClient
 *   - Parse JSON with ObjectMapper (Jackson)
 *   - Access response fields: response.total, response.data[0].winner, etc.
 */
public class Solution {
    private static final String BASE_URL = "https://jsonmock.hackerrank.com/api";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * getDrawnMatches(year)
     *   Count matches that ended level. A draw means team1goals === team2goals.
     *   Scores run 0..10, so make 11 requests in parallel ("team1goals=X & team2goals=X")
     *   and sum the `total` from each response using CompletableFuture.allOf().
     */
    static Integer getDrawnMatches(int year) {
        System.out.println("\n[getDrawnMatches] START — year=" + year);

        List<CompletableFuture<Integer>> futures = new ArrayList<>();

        // Fire all 11 requests in parallel (goals 0..10)
        for (int i = 0; i <= 10; i++) {
            final int goal = i;
            String url = String.format(
                "%s/football_matches?year=%d&team1goals=%d&team2goals=%d",
                BASE_URL, year, goal, goal
            );

            CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
                try {
                    System.out.println("[loop] goal=" + goal + " — sending request");
                    HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI(url))
                        .GET()
                        .build();

                    HttpResponse<String> response = httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                    );

                    System.out.println("[response] goal=" + goal + " — HTTP " + response.statusCode());

                    MatchResponse matchResponse = mapper.readValue(
                        response.body(),
                        MatchResponse.class
                    );

                    System.out.println("[json parsed] goal=" + goal + " — total=" + matchResponse.total);
                    return matchResponse.total;

                } catch (Exception e) {
                    System.err.println("Error fetching goal=" + goal + ": " + e.getMessage());
                    throw new RuntimeException(e);
                }
            });

            futures.add(future);
        }

        System.out.println("[loop done] " + futures.size() + " requests in flight, awaiting all...");

        // Wait for all requests to complete
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
            futures.toArray(new CompletableFuture[0])
        );

        try {
            allFutures.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Collect results and sum
        List<Integer> totals = futures.stream()
            .map(CompletableFuture::join)
            .collect(Collectors.toList());

        System.out.println("[Promise.all resolved] totals array = " + totals);

        int sum = 0;
        for (int i = 0; i < totals.size(); i++) {
            sum += totals.get(i);
            System.out.println("[sum] goal " + i + "-" + i + ": +" + totals.get(i) + " => running total " + sum);
        }

        System.out.println("[getDrawnMatches] RETURN — sum=" + sum);
        return sum;
    }

    /**
     * getWinnerTotalGoals(competition, year)
     *   Total goals scored by the team that won the competition that year.
     *   1. football_competitions -> get the winner's name.
     *   2. football_matches team1=<winner> -> sum team1goals (home goals).
     *   3. football_matches team2=<winner> -> sum team2goals (away goals).
     *   answer = home + away. Goals are strings, so convert with Integer.parseInt().
     *
     *   Only page 1 of each match query is read. Fine while total_pages === 1.
     */
    static Integer getWinnerTotalGoals(String competition, int year) {
        System.out.println("\n[getWinnerTotalGoals] START — competition=" + competition + ", year=" + year);

        try {
            // 1. who won
            String url1 = String.format(
                "%s/football_competitions?year=%d&name=%s",
                BASE_URL, year, encodeURL(competition)
            );
            System.out.println("[1] url1 = " + url1);

            HttpRequest request1 = HttpRequest.newBuilder()
                .uri(new URI(url1))
                .GET()
                .build();

            HttpResponse<String> response1 = httpClient.send(
                request1,
                HttpResponse.BodyHandlers.ofString()
            );

            System.out.println("[1] HTTP " + response1.statusCode());

            CompetitionResponse competitionResponse = mapper.readValue(
                response1.body(),
                CompetitionResponse.class
            );

            String winner = competitionResponse.data.get(0).winner;
            System.out.println("[1] winner = " + winner);

            // 2. home goals: winner as team1
            String url2 = String.format(
                "%s/football_matches?competition=%s&year=%d&team1=%s",
                BASE_URL, encodeURL(competition), year, encodeURL(winner)
            );
            System.out.println("[2] url2 = " + url2);

            HttpRequest request2 = HttpRequest.newBuilder()
                .uri(new URI(url2))
                .GET()
                .build();

            HttpResponse<String> response2 = httpClient.send(
                request2,
                HttpResponse.BodyHandlers.ofString()
            );

            System.out.println("[2] HTTP " + response2.statusCode());

            MatchResponse matchResponse1 = mapper.readValue(
                response2.body(),
                MatchResponse.class
            );

            int homeGoals = 0;
            for (MatchResponse.Match match : matchResponse1.data) {
                homeGoals += Integer.parseInt(match.team1goals);
            }
            System.out.println("[2] homeGoals = " + homeGoals);

            // 3. away goals: winner as team2
            String url3 = String.format(
                "%s/football_matches?competition=%s&year=%d&team2=%s",
                BASE_URL, encodeURL(competition), year, encodeURL(winner)
            );
            System.out.println("[3] url3 = " + url3);

            HttpRequest request3 = HttpRequest.newBuilder()
                .uri(new URI(url3))
                .GET()
                .build();

            HttpResponse<String> response3 = httpClient.send(
                request3,
                HttpResponse.BodyHandlers.ofString()
            );

            System.out.println("[3] HTTP " + response3.statusCode());

            MatchResponse matchResponse2 = mapper.readValue(
                response3.body(),
                MatchResponse.class
            );

            int awayGoals = 0;
            for (MatchResponse.Match match : matchResponse2.data) {
                awayGoals += Integer.parseInt(match.team2goals);
            }
            System.out.println("[3] awayGoals = " + awayGoals);

            // 4. total
            int totalGoals = homeGoals + awayGoals;
            System.out.println("[4] totalGoals = " + totalGoals);

            return totalGoals;

        } catch (Exception e) {
            System.err.println("Error in getWinnerTotalGoals: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // Helper to URL-encode strings (spaces become %20)
    private static String encodeURL(String s) {
        return s.replace(" ", "%20");
    }

    public static void main(String[] args) {
        System.out.println("[script] calling getDrawnMatches(2011)");

        try {
            Integer drawnMatches = getDrawnMatches(2011);
            System.out.println("\n[script] .then callback — answer=" + drawnMatches);
            System.out.println("Total drawn matches in 2011: " + drawnMatches);

            System.out.println("\n" +
                "=============================================================================\n");

            System.out.println("[script] calling getWinnerTotalGoals(\"UEFA Champions League\", 2011)");
            Integer totalGoals = getWinnerTotalGoals("UEFA Champions League", 2011);
            System.out.println("\n[script] .then callback — answer=" + totalGoals);
            System.out.println("Total goals by winner of UEFA Champions League 2011: " + totalGoals);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
