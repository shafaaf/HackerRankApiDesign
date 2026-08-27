# REST API Practice — HackerRank jsonmock

## Project Overview
Practice async JavaScript and REST API consumption using the HackerRank jsonmock football database.

## Files
- **ownSol.js** — Main solution file with two functions:
  - `getDrawnMatches(year)` — Count matches that ended in draws (level scores).
  - `getWinnerTotalGoals(competition, year)` — Total goals scored by a competition winner.

- **test.js** — Earlier buggy version (for reference/debugging).
- **hackerrankSample-1.js** — Verbose example with detailed logging.
- **index.js** — (see what's in there)

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

### Parsing a fetch response
```js
const res  = await fetch(url);      // Response object
const body = await res.json();      // parsed JSON
body.data[0]                        // first row
body.data[0].winner                 // a field
```

### String-to-number conversion
Goals come back as strings ("2", "5"), so always wrap in `Number()` before addition.

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
node ownSol.js
```

Both functions run immediately and log their results to console.

## Notes
- All console.log calls follow a consistent style: primitives use template literals,
  objects/arrays are logged as separate arguments to preserve formatting.
- Header block in ownSol.js includes sample JSON responses for reference.
