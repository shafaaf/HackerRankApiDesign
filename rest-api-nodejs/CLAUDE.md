# REST API Practice — HackerRank jsonmock (Node.js)

Practice async JavaScript and REST API consumption using the HackerRank jsonmock football database.

## Project Overview

Two working functions that solve the same problem in two different ways:

1. **getDrawnMatches(year)** — Count matches that ended in draws (level scores)
2. **getWinnerTotalGoals(competition, year)** — Total goals scored by competition winner

## Files

- **ownSol.js** — Main solution file with both functions (entry point)
- **index.js** — Alternative/reference implementation
- **hackerrankSample-1.js** — Verbose example with detailed logging
- **package.json** — Dependencies (minimal; uses built-in fetch)

## API Endpoints

### football_matches
```
GET https://jsonmock.hackerrank.com/api/football_matches
Query params: year, competition, team1, team2, team1goals, team2goals, page
Response shape: { page, per_page, total, total_pages, data: [...up to 10 rows...] }

Each row:
  competition (string)
  year (number)
  round (string)
  team1, team2 (strings)
  team1goals, team2goals (strings! -> Number() before math)
```

### football_competitions
```
GET https://jsonmock.hackerrank.com/api/football_competitions
Query params: year, name, page
Response shape: { page, per_page, total, total_pages, data: [...up to 10 rows...] }

Each row:
  name (string)
  country (string)
  year (number)
  winner (string)
  runnerup (string)
```

## Key Patterns

### Making a fetch request and parsing JSON
```js
const res = await fetch(url);
const body = await res.json();
body.data[0]        // first row
body.total          // total matches/competitions
body.total_pages    // number of pages
```

### String-to-number conversion
Goals come back as strings ("2", "5"), so always wrap in `Number()` before addition:
```js
const goal1 = Number(row.team1goals);  // "2" -> 2
const goal2 = Number(row.team2goals);  // "5" -> 5
const total = goal1 + goal2;           // 7
```

### Parallel requests
Use `Promise.all()` to fire requests in parallel and wait for all to complete:
```js
const promises = [];
for (let i = 0; i <= 10; i++) {
    promises.push(
        fetch(`...&team1goals=${i}&team2goals=${i}`)
            .then(res => res.json())
    );
}

const results = await Promise.all(promises);
const totals = results.map(r => r.total);
const sum = totals.reduce((a, b) => a + b, 0);
```

### Pagination
Every response includes `total_pages`. For large result sets, loop:
```js
let page = 1;
do {
    const { data, total_pages } = await (await fetch(`...&page=${page}`)).json();
    // process data
    page++;
} while (page <= total_pages);
```

## Running

```bash
npm install
node ownSol.js
```

Both functions run immediately and log their results to console.

## Function Reference

### getDrawnMatches(year)
**Purpose:** Count all matches in a year where both teams scored the same.

**Algorithm:**
1. Fire 11 parallel requests (one for each possible draw score: 0-0, 1-1, ..., 10-10)
2. Each request: `GET /api/football_matches?year={year}&team1goals={i}&team2goals={i}`
3. Extract the `total` field from each response
4. Sum all totals

**Returns:** Number (total drawn matches)

**Example:**
```js
const drawnMatches = await getDrawnMatches(2011);
console.log(drawnMatches);  // e.g., 1234
```

### getWinnerTotalGoals(competition, year)
**Purpose:** Total goals scored by the team that won a competition in a given year.

**Algorithm:**
1. Query `football_competitions` to find the winning team
2. Query `football_matches` with `team1=winner` to sum home goals (pagination)
3. Query `football_matches` with `team2=winner` to sum away goals (pagination)
4. Return combined total

**Returns:** Number (total goals by winner)

**Example:**
```js
const totalGoals = await getWinnerTotalGoals("UEFA Champions League", 2011);
console.log(totalGoals);  // e.g., 104
```

## Sample Output

```
[script] calling getDrawnMatches(2011)
[getDrawnMatches] START — year=2011
Total drawn matches in 2011: 1234

[script] calling getWinnerTotalGoals("UEFA Champions League", 2011)
[getWinnerTotalGoals] START — competition=UEFA Champions League, year=2011
[1] winner = Chelsea
[2] homeGoals = 56
[3] awayGoals = 48
[4] totalGoals = 104
Total goals by winner of UEFA Champions League 2011: 104
```

## Notes

- All console.log calls follow a consistent style: primitives use template literals, objects/arrays are logged as separate arguments to preserve formatting.
- The fetch API is built-in to Node.js 18+; for earlier versions, use a polyfill or the `node-fetch` package.
- Header block in ownSol.js includes sample JSON responses for reference.
- Both functions use async/await with proper error handling.

## See Also

- [Root README](../../README.md) — Overview of both implementations
- [Root CLAUDE.md](../../CLAUDE.md) — Comparison of JavaScript vs Java patterns
- [Java implementation](../rest-api-java) — Same logic in compiled Java
