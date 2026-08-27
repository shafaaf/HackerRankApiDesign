package com.hackerrank.football;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class MatchResponse {
    public int page;
    public int per_page;
    public int total;
    public int total_pages;
    public List<Match> data;

    public static class Match {
        public String competition;
        public int year;
        public String round;
        public String team1;
        public String team2;
        public String team1goals;  // NOTE: goals are strings - convert with Integer.parseInt()
        public String team2goals;
    }
}
