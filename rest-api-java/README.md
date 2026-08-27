# REST API Practice — HackerRank Football (Java)

Java implementation of REST API consumption practice using HackerRank's jsonmock football database.

## Quick Start

### Prerequisites
- Java 11+
- Maven 3.6+

### Build & Run
```bash
# Download dependencies and compile
mvn clean compile

# Run the main class
mvn exec:java
```

## Project Structure
```
rest-api-java/
├── pom.xml                               # Maven configuration
├── CLAUDE.md                             # Project documentation
├── README.md                             # This file
├── .gitignore
└── src/main/java/com/hackerrank/football/
    ├── Solution.java                     # Main solution with two functions
    ├── MatchResponse.java                # DTO for football_matches endpoint
    └── CompetitionResponse.java          # DTO for football_competitions endpoint
```

## Functions

### getDrawnMatches(int year)
Counts all football matches that ended in a draw (team1goals == team2goals) for a given year.

**Flow:**
1. Fire 11 parallel HTTP requests (one for each score: 0-0, 1-1, 2-2, ..., 10-10)
2. Uses `CompletableFuture` for async execution (equivalent to JavaScript Promise.all)
3. Sums up the `total` count from each response
4. Returns the total number of drawn matches

**Example:**
```java
Integer drawnMatches = getDrawnMatches(2011);
// Output: 234 matches ended in draws in 2011
```

### getWinnerTotalGoals(String competition, int year)
Calculates the total goals scored by the team that won a competition in a given year.

**Flow:**
1. Query `football_competitions` to find the winning team
2. Query `football_matches` with `team1=winner` to sum home goals
3. Query `football_matches` with `team2=winner` to sum away goals
4. Return combined total

**Example:**
```java
Integer totalGoals = getWinnerTotalGoals("UEFA Champions League", 2011);
// Output: 123 goals scored by 2011 UEFA Champions League winner
```

## API Responses

Both endpoints return paginated results. Each page contains up to 10 rows.

### football_matches response
```json
{
  "page": 1,
  "per_page": 10,
  "total": 234,
  "total_pages": 24,
  "data": [
    {
      "competition": "UEFA Champions League",
      "year": 2011,
      "round": "GroupF",
      "team1": "Borussia Dortmund",
      "team2": "Arsenal",
      "team1goals": "1",
      "team2goals": "1"
    }
  ]
}
```

### football_competitions response
```json
{
  "page": 1,
  "per_page": 10,
  "total": 1,
  "total_pages": 1,
  "data": [
    {
      "name": "UEFA Champions League",
      "country": "",
      "year": 2011,
      "winner": "Chelsea",
      "runnerup": "Bayern Munich"
    }
  ]
}
```

## Key Implementation Details

- **HTTP Client:** Java 11's built-in `HttpClient`
- **JSON Parsing:** Jackson (`ObjectMapper`)
- **Async Execution:** `CompletableFuture` for parallel requests
- **Goals Conversion:** Goals come as strings in JSON; converted with `Integer.parseInt()`

## Sample Output

```
[script] calling getDrawnMatches(2011)

[getDrawnMatches] START — year=2011
[loop] goal=0 — sending request
[loop] goal=1 — sending request
...
[response] goal=0 — HTTP 200
[response] goal=1 — HTTP 200
...
[json parsed] goal=0 — total=234
[json parsed] goal=1 — total=110
...
[Promise.all resolved] totals array = [234, 110, ...]
[sum] goal 0-0: +234 => running total 234
[sum] goal 1-1: +110 => running total 344
...
Total drawn matches in 2011: 1234

=============================================================================

[script] calling getWinnerTotalGoals("UEFA Champions League", 2011)

[getWinnerTotalGoals] START — competition=UEFA Champions League, year=2011
[1] winner = Chelsea
[2] homeGoals = 56
[3] awayGoals = 48
[4] totalGoals = 104

Total goals by winner of UEFA Champions League 2011: 104
```

## Notes

- All requests use `GET` with query parameters
- Response classes use Jackson annotations for automatic JSON deserialization
- Console logging follows a structured format with `[section]` tags for clarity
- The implementation handles pagination-aware responses but only reads page 1 for simplicity

## See Also

- [CLAUDE.md](./CLAUDE.md) — Full API reference and patterns
- [Root README](../README.md) — Overview of both implementations
- [Node.js version](../rest-api-nodejs) — Same logic in JavaScript for comparison
