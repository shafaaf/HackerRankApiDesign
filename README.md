# HackerRank API Design — jsonmock Football

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

### football_competitions
Rows: `name`, `country`, `year`, `winner`, `runnerup`  
Query: `year`, `name`, `page`

## Key Learnings

- **Async/await** with `fetch()` and `Promise.all()` for parallel requests.
- **Type conversion**: goals come back as strings; use `Number()` before math.
- **Pagination**: loop over `page` up to `total_pages` for large result sets.
- **Logging style**: template literals for primitives, separate args for objects/arrays.

See `CLAUDE.md` for full API reference and parsing patterns.
