package com.hackerrank.football;

import java.util.List;

public class CompetitionResponse {
    public int page;
    public int per_page;
    public int total;
    public int total_pages;
    public List<Competition> data;

    public static class Competition {
        public String name;
        public String country;
        public int year;
        public String winner;
        public String runnerup;
    }
}
