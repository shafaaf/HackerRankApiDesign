# HackerRank REST API Practice — JavaScript & Java
## Challenge: Total Goals by a Team

Two complete implementations for practicing async REST API consumption and pagination.

## Overview

This repo contains solutions to the HackerRank challenge **Total Goals by a Team** in both **JavaScript/Node.js** and **Java**, demonstrating:
- Async/await patterns (Promise.all vs CompletableFuture)
- REST API pagination and querying
- String-to-number type conversion
- Structured logging

## The Challenge

Given a team name and year, calculate the total number of goals scored by that team across all matches in that year.

**Key Insight:** A team can play matches as either `team1` (home) or `team2` (away), so you must query both and sum the results.

**Example:**
```
Input:  team="Barcelona", year=2011
Output: 35

Barcelona scored 35 total goals in 2011 (both as home and away team combined).
```

## Quick Start

### Option 1: Node.js (Fast iteration)
```bash
cd rest-api-nodejs
npm install
node ownSol.js
```

**Output:**
```
Total goals by Barcelona in 2011: 35
```

### Option 2: Java (Learn compiled patterns)
```bash
cd rest-api-java
mvn clean compile
mvn exec:java
```

**Output:** Same results, compiled and executed by Java runtime.

## What Each Folder Contains

### [`rest-api-nodejs/`](./rest-api-nodejs)
JavaScript async/await implementation with:
- `ownSol.js` — Main solution (getTotalGoals function)
- `CLAUDE.md` — Full API reference and patterns
- `README.md` — Project quickstart

**Tech:** Node.js, fetch API, Promise.all, async/await, JSON.parse

### [`rest-api-java/`](./rest-api-java)
Java implementation with:
- `Solution.java` — Main solution (same getTotalGoals function)
- `MatchResponse.java` — Jackson DTO for API responses
- `CLAUDE.md` — Full API reference and patterns
- `README.md` — Project quickstart

**Tech:** Java 11+, HttpClient, CompletableFuture, Jackson

## Learning Path

1. **Understand the problem**
   - A team plays matches as both `team1` (home) and `team2` (away)
   - Goals are returned as strings; must convert to numbers
   - API returns paginated results; must loop through all pages

2. **Start with Node.js** — Faster to experiment
   - See how pagination works
   - Test API responses quickly
   - Learn async/await pattern

3. **Move to Java** — Understand compiled equivalents
   - See `CompletableFuture` for async patterns
   - Learn type safety with DTOs
   - Compare performance vs JavaScript

4. **Compare both** — Same problem, different tools
   - Different syntax, identical algorithms
   - Trade-offs in each language

## API Endpoint Reference

### football_matches
```
GET https://jsonmock.hackerrank.com/api/football_matches
Query params: year, team1, team2, competition, page

Response: {
  "page": 1,
  "per_page": 10,
  "total": 234,
  "total_pages": 24,
  "data": [
    {
      "team1": "Barcelona",
      "team2": "Arsenal",
      "team1goals": "2",       // NOTE: string!
      "team2goals": "1",       // convert to number before math
      "year": 2011,
      ...
    }
  ]
}
```

**Key Details:**
- Results are paginated (max 10 per page)
- `total_pages` tells you how many pages exist
- `team1goals` and `team2goals` are **strings** — must convert
- To query a team, make **two requests**: one with `team1=<team>`, one with `team2=<team>`

## Files in This Repo

```
rest-api-hacker-rank/
├── README.md                         # This file (entry point)
├── CLAUDE.md                         # Technical reference (both languages)
│
├── rest-api-nodejs/
│   ├── CLAUDE.md                    # Node.js reference
│   ├── README.md                    # Node.js quickstart
│   ├── ownSol.js                    # Solution (entry point)
│   ├── package.json
│   └── other files (alternatives/examples)
│
└── rest-api-java/
    ├── CLAUDE.md                    # Java reference
    ├── README.md                    # Java quickstart
    ├── pom.xml                      # Maven config
    └── src/main/java/com/hackerrank/football/
        ├── Solution.java            # Main solution
        ├── MatchResponse.java       # DTO
        └── CompetitionResponse.java # DTO reference
```

## Key Learnings

- **Pagination** — Loop from page 1 to `total_pages` to fetch all results
- **Dual queries** — Query both `team1=<team>` and `team2=<team>` positions
- **Type conversion** — Goals come as strings; convert before math
- **Async patterns** — How different languages handle concurrent requests
- **Structured logging** — Use consistent prefixes for debugging

## Next Steps

- [ ] Run Node.js solution and inspect the output
- [ ] Run Java solution and compare results
- [ ] Modify to test different teams and years
- [ ] Add error handling for failed API requests
- [ ] Benchmark performance between Node.js and Java

---

**See [`CLAUDE.md`](./CLAUDE.md) for technical reference on both implementations.**
