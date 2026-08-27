# HackerRank REST API Practice — Multi-Language
## Challenge: Total Goals by a Team

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

## Shared Learning Objective

Both implementations solve the same HackerRank challenge: **Total Goals by a Team**

**Challenge:** Given a team name and year, calculate the total number of goals scored by that team across all matches in that year. The team can play as either `team1` (home) or `team2` (away), so you must query both positions and sum the results.

**Example:**
```
Input:  team="Barcelona", year=2011
Output: 35
```

Barcelona played multiple matches in 2011 both as the home team (team1) and away team (team2), scoring 35 total goals combined.

## Function Signature

### JavaScript/Node.js
```js
async function getTotalGoals(team, year)
```

### Java
```java
static Integer getTotalGoals(String team, int year)
```

## API Reference

### football_matches Endpoint
```
GET https://jsonmock.hackerrank.com/api/football_matches
Query params: year, team1, team2, competition, page
Response: { page, per_page, total, total_pages, data: [...] }
```

**Response Fields:**
- `page` — Current page number (1-indexed)
- `per_page` — Number of results per page (typically 10)
- `total` — Total matches across all pages
- `total_pages` — Number of pages to fetch
- `data` — Array of match objects

**Match Fields (Relevant):**
- `team1` — Home team name
- `team2` — Away team name
- `team1goals` — Goals scored by team1 (string! must convert)
- `team2goals` — Goals scored by team2 (string! must convert)
- `year` — Year of the match
- `competition` — Name of the competition

## Key Implementation Pattern

Since the API distinguishes between `team1` (home) and `team2` (away), the solution requires:

1. **Query 1:** Fetch all matches where `team1=<team>` and `year=<year>`
   - Loop through all pages
   - Sum all `team1goals`

2. **Query 2:** Fetch all matches where `team2=<team>` and `year=<year>`
   - Loop through all pages
   - Sum all `team2goals`

3. **Result:** `homeGoals + awayGoals`

## Key Patterns (Language-Agnostic)

- **String-to-number conversion** — Goals come as strings ("2", "5"); convert before math
  - Node.js: `Number(goalString)`
  - Java: `Integer.parseInt(goalString)`

- **Pagination** — Loop from page 1 to `total_pages` for complete results
- **Dual queries** — Must query both `team1` and `team2` parameters to get all matches
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
| **Pagination** | while/do-while loop | for loop |
| **HTTP Client** | Built-in fetch | Java 11+ HttpClient |
| **Type Conversion** | Number() | Integer.parseInt() |

## Notes

- Both implementations follow the same async patterns and solve the identical problem
- Use Node.js for quick iteration and testing
- Use Java for learning compiled language patterns and enterprise tooling
- All logging uses standard output (console.log / System.out.println)
- Pages are 1-indexed (start from page 1, not 0)
- The `total_pages` field tells you exactly how many pages to fetch
