# HackerRank REST API Practice — Multi-Language

Practice async REST API consumption using the HackerRank jsonmock football database. Two complete implementations: JavaScript/Node.js and Java.

## Project Structure

```
rest-api-hacker-rank/
├── rest-api-nodejs/         # JavaScript/Node.js implementation
│   ├── CLAUDE.md           # Project guide (JavaScript)
│   ├── README.md           # Quick start
│   ├── ownSol.js           # Main solution
│   ├── package.json
│   └── ...
│
├── rest-api-java/          # Java implementation
│   ├── CLAUDE.md           # Project guide (Java)
│   ├── README.md           # Quick start
│   ├── pom.xml
│   └── src/main/java/...
│
├── CLAUDE.md               # This file
└── README.md               # Entry point
```

## Shared Learning Objectives

Both implementations solve the same two problems using the HackerRank jsonmock API:

1. **getDrawnMatches(year)** — Count all matches ending in draws (same score both sides)
   - Uses parallel async requests (11 simultaneous calls, one per possible score: 0-0 to 10-10)
   - Sums the `total` field from each response
   
2. **getWinnerTotalGoals(competition, year)** — Total goals by competition winner
   - Fetch the winning team from `football_competitions`
   - Sum goals when winner plays as team1 (home) and team2 (away)

## API Reference

### football_matches
```
GET https://jsonmock.hackerrank.com/api/football_matches
Query: year, competition, team1, team2, team1goals, team2goals, page
Response: { page, per_page, total, total_pages, data: [...] }

Row fields: competition, year, round, team1, team2, team1goals, team2goals (strings!)
```

### football_competitions
```
GET https://jsonmock.hackerrank.com/api/football_competitions
Query: year, name, page
Response: { page, per_page, total, total_pages, data: [...] }

Row fields: name, country, year, winner, runnerup
```

## Key Patterns (Language-Agnostic)

- **String-to-number conversion** — Goals come back as strings ("2", "5"); always convert before math
- **Pagination** — Loop over pages 1 to `total_pages` for large result sets
- **Parallel requests** — Use async/await + Promise.all (JS) or CompletableFuture (Java)
- **Structured logging** — Use consistent prefixes like `[section]` for readability

## Quick Start

### Node.js
```bash
cd rest-api-nodejs
npm install
node ownSol.js
```

### Java
```bash
cd rest-api-java
mvn clean compile
mvn exec:java
```

## Implementation Comparison

| Aspect | Node.js | Java |
|--------|---------|------|
| **Runtime** | async/await + fetch | CompletableFuture + HttpClient |
| **JSON Parsing** | Native JSON.parse | Jackson ObjectMapper |
| **Parallelism** | Promise.all | CompletableFuture.allOf |
| **HTTP Client** | Built-in fetch | Java 11+ HttpClient |
| **Type Conversion** | Number() | Integer.parseInt() |

## Notes

- Both implementations follow the same async patterns and solve identical problems
- Use Node.js for quick iteration and testing
- Use Java for learning compiled language patterns and enterprise tooling
- All logging uses standard output (console.log / System.out.println)
- Response DTOs include field parsing and type conversions
