# HackerRank API Design — jsonmock Football (Java)
## Challenge: Total Goals by a Team

Java implementation of the HackerRank REST API challenge.

## Quick Start

### Prerequisites
- Java 11+
- Maven 3.6+

### Build & Run
```bash
mvn clean compile
mvn exec:java
```

## Challenge Overview

**Total Goals by a Team**

Given a team name and year, calculate the total number of goals scored by that team in all matches during that year.

### Example
```
Input: team="Barcelona", year=2011
Output: 35
```

Barcelona scored 35 total goals in 2011 (combining all matches where they played as home or away team).

## How It Works

The `getTotalGoals(team, year)` function:

1. **Query home matches** — Fetch all matches where the team played as `team1` (home)
   - Loop through all pages to get the complete list
   - Sum the `team1goals` from each match

2. **Query away matches** — Fetch all matches where the team played as `team2` (away)
   - Loop through all pages to get the complete list
   - Sum the `team2goals` from each match

3. **Return total** — `homeGoals + awayGoals`

## API Response Format

### football_matches endpoint
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

**Key Points:**
- `total_pages` — tells you how many pages to fetch
- `data` — array of up to 10 matches per page
- Goals are strings ("1", "35", etc.); convert with `Integer.parseInt()`

## Project Structure
```
rest-api-java/
├── pom.xml                               # Maven configuration
├── CLAUDE.md                             # Full technical reference
├── README.md                             # This file
├── .gitignore
└── src/main/java/com/hackerrank/football/
    ├── Solution.java                     # Main solution
    ├── MatchResponse.java                # DTO for football_matches
    └── CompetitionResponse.java          # DTO reference
```

## Key Implementation Details

- **HTTP Client:** Java 11's built-in `HttpClient`
- **JSON Parsing:** Jackson (`ObjectMapper`)
- **Pagination:** Manual loop from page 1 to `total_pages`
- **Type Conversion:** `Integer.parseInt()` for goal strings
- **Parallel Requests:** Not needed for this challenge (sequential queries are fine)

## Sample Test Cases

### Test Case 1: Barcelona, 2011
```
Input:  team="Barcelona", year=2011
Output: 35
```

### Test Case 2: Another Team/Year
Modify the main() method in Solution.java to test different teams and years.

## Dependencies

- **Jackson** (`com.fasterxml.jackson.databind`) — JSON parsing
- **Java 11+** — Built-in HttpClient and streams API

No external dependencies are required beyond Jackson.

## Running Tests

To test with different inputs, edit the `main()` method in `Solution.java`:

```java
public static void main(String[] args) {
    try {
        Integer totalGoals = getTotalGoals("Barcelona", 2011);
        System.out.println("Total goals: " + totalGoals);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

## See Also

- [CLAUDE.md](./CLAUDE.md) — Full API reference and implementation details
- [Root README](../README.md) — Overview of both implementations
- [Node.js version](../rest-api-nodejs) — Same logic in JavaScript for comparison
