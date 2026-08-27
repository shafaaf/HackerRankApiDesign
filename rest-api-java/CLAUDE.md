# REST API Practice — HackerRank jsonmock (Java)
## Challenge: Total Goals by a Team

Practice async Java and REST API consumption using the HackerRank jsonmock football database.

## Project Overview
Solve the HackerRank challenge: **Total Goals by a Team**

Given a team name and year, calculate the total number of goals scored by that team across all matches in that year. The team can play as either team1 (home) or team2 (away), so you must query both positions and sum the results.

## Files
- **Solution.java** — Main solution file with one function:
  - `getTotalGoals(team, year)` — Sum all goals scored by a team in a given year (home + away).

- **MatchResponse.java** — Jackson-annotated class for parsing football_matches responses.
- **CompetitionResponse.java** — Jackson-annotated class for parsing football_competitions responses (for reference).

## API Endpoint Reference

### football_matches
```
GET https://jsonmock.hackerrank.com/api/football_matches
Query params: year, team1, team2, competition, page
Response shape: { page, per_page, total, total_pages, data: [...up to 10 rows...] }

Each row:
  competition (String)
  year (int)
  round (String)
  team1 (String) — home team name
  team2 (String) — away team name
  team1goals (String!) — goals scored by team1, convert with Integer.parseInt()
  team2goals (String!) — goals scored by team2, convert with Integer.parseInt()
```

**Key Detail:** To get all matches a team played in, you must make **two separate queries**:
1. `team1=<teamName>&year=<year>` — matches where the team was the home team
2. `team2=<teamName>&year=<year>` — matches where the team was the away team

This is because team1 and team2 are separate query parameters, not a single "team" parameter.

## Challenge Requirements

**Function Signature:**
```java
Integer getTotalGoals(String team, int year)
```

**Input:**
- `team` (String) — The name of the team (e.g., "Barcelona")
- `year` (int) — The year of competition (e.g., 2011)

**Output:**
- Integer — Total goals scored by the team in all matches in that year

**Example:**
```java
getTotalGoals("Barcelona", 2011)  // Returns: 35
```

Explanation: Barcelona scored a total of 35 goals in all matches during 2011 (both as home and away team combined).

## Key Patterns

### Making a fetch request and parsing JSON
```java
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
```

### String-to-integer conversion
Goals come back as strings ("2", "5"), so always wrap in `Integer.parseInt()` before addition:
```java
int goals = Integer.parseInt(match.team1goals);  // "35" -> 35
totalGoals += goals;
```

### Pagination
Every response includes `total_pages`. For large result sets, loop:
```java
for (int page = 1; page <= matchResponse.total_pages; page++) {
    String url = String.format("...&page=%d", page);
    // fetch and process
}
```

## Algorithm

1. **Query team1 matches** — `GET /api/football_matches?year=<year>&team1=<team>&page=1`
   - Loop through all pages (use `total_pages`)
   - Sum all `team1goals` for each match

2. **Query team2 matches** — `GET /api/football_matches?year=<year>&team2=<team>&page=1`
   - Loop through all pages (use `total_pages`)
   - Sum all `team2goals` for each match

3. **Return combined total** — `homeGoals + awayGoals`

## Running

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.hackerrank.football.Solution"
```

Or simply:
```bash
mvn compile && mvn exec:java
```

## Sample Output

```
[getTotalGoals] START — team=Barcelona, year=2011
[team1] fetching matches where Barcelona is home team
[team1] page 1/2 — 10 matches, 12 goals total
[team1] page 2/2 — 8 matches, 11 goals total
[team1] homeGoals = 23

[team2] fetching matches where Barcelona is away team
[team2] page 1/1 — 6 matches, 12 goals total
[team2] awayGoals = 12

[getTotalGoals] RETURN — totalGoals = 35
Total goals by Barcelona in 2011: 35
```

## Notes

- All logging uses `System.out.println()` for consistency.
- HttpClient is thread-safe and reused as a static field.
- ObjectMapper (Jackson) is also reused and thread-safe.
- Goals are strings in the API; always convert with `Integer.parseInt()` before math.
- Pages are 1-indexed, not 0-indexed.
- The `total_pages` field in the response tells you how many pages to fetch.

## See Also

- [Root README](../../README.md) — Overview of both implementations
- [Root CLAUDE.md](../../CLAUDE.md) — Comparison of JavaScript vs Java patterns
- [Node.js implementation](../rest-api-nodejs) — Same logic in JavaScript
