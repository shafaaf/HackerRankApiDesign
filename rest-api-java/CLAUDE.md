# REST API Practice — HackerRank jsonmock (Java)

## Project Overview
Practice async Java and REST API consumption using the HackerRank jsonmock football database.

## Files
- **Solution.java** — Main solution file with two functions:
  - `getDrawnMatches(year)` — Count matches that ended in draws (level scores).
  - `getWinnerTotalGoals(competition, year)` — Total goals scored by a competition winner.

- **MatchResponse.java** — Jackson-annotated class for parsing football_matches responses.
- **CompetitionResponse.java** — Jackson-annotated class for parsing football_competitions responses.

## API Endpoints

### football_matches
```
GET https://jsonmock.hackerrank.com/api/football_matches
Query params: year, competition, team1, team2, team1goals, team2goals, page
Response shape: { page, per_page, total, total_pages, data: [...up to 10 rows...] }

Each row:
  competition (String)
  year (int)
  round (String)
  team1, team2 (Strings)
  team1goals, team2goals (Strings! -> Integer.parseInt() before math)
```

### football_competitions
```
GET https://jsonmock.hackerrank.com/api/football_competitions
Query params: year, name, page
Response shape: { page, per_page, total, total_pages, data: [...up to 10 rows...] }

Each row:
  name (String)
  country (String)
  year (int)
  winner (String)
  runnerup (String)
```

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
Goals come back as strings ("2", "5"), so always wrap in `Integer.parseInt()` before addition.

### Parallel requests
Use `CompletableFuture.supplyAsync()` to fire requests in parallel and `CompletableFuture.allOf()` to wait for all:
```java
List<CompletableFuture<Integer>> futures = new ArrayList<>();

for (int i = 0; i <= 10; i++) {
    CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
        // make request, return result
        return result;
    });
    futures.add(future);
}

// Wait for all to complete
CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();

// Collect results
List<Integer> results = futures.stream()
    .map(CompletableFuture::join)
    .collect(Collectors.toList());
```

### Pagination
Every response includes `total_pages`. For large result sets, loop:
```java
for (int page = 1; page <= totalPages; page++) {
    String url = String.format("...&page=%d", page);
    // fetch and process
}
```

## Running
```bash
mvn compile
mvn exec:java
```

Or run the main class directly:
```bash
mvn exec:java -Dexec.mainClass="com.hackerrank.football.Solution"
```

## Dependencies
- **Jackson** — JSON parsing (`com.fasterxml.jackson.databind`)
- **SLF4J** — Logging (optional, included for convenience)
- **Java 11+** — Built-in HttpClient

## Notes
- All logging uses `System.out.println()` for consistency.
- HttpClient is thread-safe and reused as a static field.
- ObjectMapper (Jackson) is also reused and thread-safe.
- Response classes use public fields with Jackson defaults — no annotations needed.
- Goals are strings in the API, converted with `Integer.parseInt()` before math.
