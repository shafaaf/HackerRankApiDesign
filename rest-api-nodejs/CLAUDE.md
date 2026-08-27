# REST API Practice — HackerRank jsonmock (Node.js)
## Challenge: Total Goals by a Team

Practice async JavaScript and REST API consumption using the HackerRank jsonmock football database.

## Project Overview
Solve the HackerRank challenge: **Total Goals by a Team**

Given a team name and year, calculate the total number of goals scored by that team across all matches in that year. The team can play as either team1 (home) or team2 (away), so you must query both positions and sum the results.

## Files
- **ownSol.js** — Main solution file with one function:
  - `getTotalGoals(team, year)` — Sum all goals scored by a team in a given year (home + away).

- **index.js** — Alternative/reference implementation
- **hackerrankSample-1.js** — Verbose example with detailed logging
- **package.json** — Dependencies (minimal; uses built-in fetch)

## API Endpoint Reference

### football_matches
```
GET https://jsonmock.hackerrank.com/api/football_matches
Query params: year, team1, team2, competition, page
Response shape: { page, per_page, total, total_pages, data: [...up to 10 rows...] }

Each row:
  competition (string)
  year (number)
  round (string)
  team1 (string) — home team name
  team2 (string) — away team name
  team1goals (string!) — goals scored by team1, convert with Number()
  team2goals (string!) — goals scored by team2, convert with Number()
```

**Key Detail:** To get all matches a team played in, you must make **two separate queries**:
1. `team1=<teamName>&year=<year>` — matches where the team was the home team
2. `team2=<teamName>&year=<year>` — matches where the team was the away team

This is because team1 and team2 are separate query parameters, not a single "team" parameter.

## Challenge Requirements

**Function Signature:**
```js
async function getTotalGoals(team, year)
```

**Input:**
- `team` (string) — The name of the team (e.g., "Barcelona")
- `year` (number) — The year of competition (e.g., 2011)

**Output:**
- Number — Total goals scored by the team in all matches in that year

**Example:**
```js
await getTotalGoals("Barcelona", 2011)  // Returns: 35
```

Explanation: Barcelona scored a total of 35 goals in all matches during 2011 (both as home and away team combined).

## Key Patterns

### Making a fetch request and parsing JSON
```js
const res = await fetch(url);
const body = await res.json();
body.data                        // the array of matches
body.total_pages                 // number of pages
body.data[0].team1goals          // goals as string ("35")
```

### String-to-number conversion
Goals come back as strings ("2", "5"), so always wrap in `Number()` before addition:
```js
let totalGoals = 0;
for (let match of data) {
    totalGoals += Number(match.team1goals);  // "35" -> 35
}
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
npm install
node ownSol.js
```

The function runs immediately and logs the result to console.

## Function Reference

### getTotalGoals(team, year)
**Purpose:** Sum all goals scored by a team in a given year (both home and away).

**Algorithm:**
1. Fetch all matches where `team1=<team>` and `year=<year>` (all pages)
2. Sum all `team1goals` from those matches
3. Fetch all matches where `team2=<team>` and `year=<year>` (all pages)
4. Sum all `team2goals` from those matches
5. Return `homeGoals + awayGoals`

**Returns:** Number (total goals by team)

**Example:**
```js
const totalGoals = await getTotalGoals("Barcelona", 2011);
console.log(totalGoals);  // 35
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

- All console.log calls follow a consistent style: primitives use template literals, objects/arrays are logged as separate arguments to preserve formatting.
- The fetch API is built-in to Node.js 18+; for earlier versions, use a polyfill or the `node-fetch` package.
- Goals come as strings; always convert with `Number()` before math.
- Pages are 1-indexed, not 0-indexed.
- The `total_pages` field in the response tells you how many pages to fetch.

## See Also

- [Root README](../../README.md) — Overview of both implementations
- [Root CLAUDE.md](../../CLAUDE.md) — Comparison of JavaScript vs Java patterns
- [Java implementation](../rest-api-java) — Same logic in compiled Java
