# HackerRank REST API Practice — JavaScript & Java

Two complete implementations for practicing async REST API consumption.

## Overview

This repo contains solutions to the same HackerRank REST API challenge in both **JavaScript/Node.js** and **Java**, demonstrating:
- Async/await patterns (Promise.all vs CompletableFuture)
- REST API pagination and parallel requests
- JSON parsing and type conversion
- Structured logging

## The Challenge

Given the HackerRank jsonmock football database, implement two functions:

1. **Count drawn matches in a year** — Sum matches where both teams scored the same (0-0, 1-1, etc.)
2. **Total goals by competition winner** — Sum all goals scored by the team that won a competition

## Quick Start

### Option 1: Node.js (Fast iteration)
```bash
cd rest-api-nodejs
npm install
node ownSol.js
```

**Output:**
```
Total drawn matches in 2011: 1234
Total goals by 2011 UEFA Champions League winner: 104
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
- `ownSol.js` — Main solution (getDrawnMatches, getWinnerTotalGoals)
- `CLAUDE.md` — Full API reference and patterns
- `README.md` — Project quickstart

**Tech:** Node.js, fetch API, Promise.all, JSON.parse

### [`rest-api-java/`](./rest-api-java)
Java implementation with:
- `Solution.java` — Main solution (same two functions)
- `MatchResponse.java` & `CompetitionResponse.java` — Jackson DTOs
- `CLAUDE.md` — Full API reference and patterns
- `README.md` — Project quickstart

**Tech:** Java 11+, HttpClient, CompletableFuture, Jackson

## Learning Path

1. **Start with Node.js** — Faster to run, easier to experiment
   - See how `Promise.all` parallelizes requests
   - Learn pagination patterns
   - Test API responses quickly

2. **Move to Java** — Understand compiled language equivalents
   - See `CompletableFuture` for async/await-like patterns
   - Learn type safety with Jackson DTOs
   - Compare performance vs JavaScript

3. **Study both** — See how the same async problem is solved two ways
   - Different syntax, identical algorithms
   - Trade-offs in each language

## API Endpoints

Both implementations call:
- `GET /api/football_matches` — Match results with goals
- `GET /api/football_competitions` — Competition winners

See [CLAUDE.md](./CLAUDE.md) for full endpoint reference.

## Files in This Repo

```
rest-api-hacker-rank/
├── README.md                         # This file
├── CLAUDE.md                         # Technical reference (both languages)
│
├── rest-api-nodejs/
│   ├── CLAUDE.md                    # Node.js reference
│   ├── README.md                    # Node.js quickstart
│   ├── ownSol.js                    # Solution (main entry point)
│   ├── package.json
│   └── index.js, hackerrankSample-1.js  # Other versions/examples
│
└── rest-api-java/
    ├── CLAUDE.md                    # Java reference
    ├── README.md                    # Java quickstart
    ├── pom.xml                      # Maven config
    └── src/main/java/com/hackerrank/football/
        ├── Solution.java            # Main solution
        ├── MatchResponse.java       # DTO
        └── CompetitionResponse.java # DTO
```

## Key Learnings

- **Async patterns** — How different languages handle parallel requests
- **API pagination** — Iterating across pages to fetch large result sets
- **Type conversion** — Goals come as strings; convert before math
- **Structured logging** — Use consistent prefixes for debugging

## Next Steps

- [ ] Run the Node.js solution and inspect the output
- [ ] Run the Java solution and compare results
- [ ] Modify both to add new queries (e.g., filter by team)
- [ ] Benchmark performance between Node.js and Java
- [ ] Add error handling for failed requests

---

**See [`CLAUDE.md`](./CLAUDE.md) for technical reference on both implementations.**
