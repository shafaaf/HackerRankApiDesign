# HackerRank API Design — jsonmock Football (Node.js)
## Challenge: Total Goals by a Team

Node.js implementation of the HackerRank REST API challenge.

## Quick Start

```bash
npm install
node ownSol.js
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
- Goals are strings ("1", "35", etc.); convert with `Number()`

## Project Structure

```
rest-api-nodejs/
├── ownSol.js                 # Main solution (entry point)
├── CLAUDE.md                 # Full technical reference
├── README.md                 # This file
├── package.json
├── index.js                  # Alternative implementation
└── hackerrankSample-1.js     # Verbose example with detailed logging
```

## Key Implementation Details

- **HTTP Client:** Fetch API (built-in to Node.js 18+)
- **Parsing:** Native `res.json()` for JSON
- **Pagination:** Manual loop from page 1 to `total_pages`
- **Type Conversion:** `Number()` for goal strings
- **Async/Await:** Modern async patterns for clean code

## Sample Test Cases

### Test Case 1: Barcelona, 2011
```
Input:  team="Barcelona", year=2011
Output: 35
```

### Test Case 2: Another Team/Year
Edit the bottom of `ownSol.js` to test different teams and years.

## Running Tests

To test with different inputs, call the function at the bottom of `ownSol.js`:

```js
getTotalGoals("Barcelona", 2011)
    .then(result => console.log(`Total goals: ${result}`))
    .catch(err => console.error(err));
```

## See Also

- [CLAUDE.md](./CLAUDE.md) — Full API reference and implementation details
- [Root README](../README.md) — Overview of both implementations
- [Java version](../rest-api-java) — Same logic in Java for comparison
