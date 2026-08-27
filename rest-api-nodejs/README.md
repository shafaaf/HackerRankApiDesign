# HackerRank API Design — jsonmock Football (Node.js)

Practice async JavaScript and REST API consumption using the HackerRank jsonmock football database.

## Quick Start

```bash
npm install
node ownSol.js
```

## What's Included

**ownSol.js** — Two working functions:

1. **getDrawnMatches(year)** — Count matches that ended in draws (level scores 0-0, 1-1, ..., 10-10) by firing 11 parallel API requests and summing the counts.

2. **getWinnerTotalGoals(competition, year)** — Find the competition winner, then sum all goals they scored as team1 (home) and team2 (away).

## API Endpoints

Both endpoints paginate responses and return `{ page, per_page, total, total_pages, data: [...] }`.

### football_matches
Rows: `competition`, `year`, `round`, `team1`, `team2`, `team1goals`, `team2goals` (strings!)  
Query: `year`, `competition`, `team1`, `team2`, `team1goals`, `team2goals`, `page`

**Sample URL:**
```
https://jsonmock.hackerrank.com/api/football_matches?year=2011&team1goals=1&team2goals=1
```

**Sample Response:**
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

### football_competitions
Rows: `name`, `country`, `year`, `winner`, `runnerup`  
Query: `year`, `name`, `page`

**Sample URL:**
```
https://jsonmock.hackerrank.com/api/football_competitions?year=2011&name=UEFA%20Champions%20League
```

**Sample Response:**
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

## Key Learnings

- **Async/await** with `fetch()` and `Promise.all()` for parallel requests.
- **Type conversion**: goals come back as strings; use `Number()` before math.
- **Pagination**: loop over `page` up to `total_pages` for large result sets.
- **Logging style**: template literals for primitives, separate args for objects/arrays.

## Project Structure

```
rest-api-nodejs/
├── ownSol.js                 # Main solution (entry point)
├── CLAUDE.md                 # Full API reference
├── README.md                 # This file
├── package.json
├── index.js                  # Alternative implementation
└── hackerrankSample-1.js     # Verbose example with detailed logging
```

## See Also

- [CLAUDE.md](./CLAUDE.md) — Full API reference and patterns
- [Root README](../README.md) — Overview of both implementations
- [Java version](../rest-api-java) — Same logic in compiled Java for comparison
