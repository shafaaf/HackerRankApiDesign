/**
 * HackerRank REST API Challenge: Total Goals by a Team
 *
 * Two functions to solve the challenge:
 * 1. getTotalGoals(team, year) - Returns total goals scored by a team in a given year
 * 2. getNumDraws(year) - Returns count of matches that ended in draws for a given year
 */

/**
 * getTotalGoals(team, year)
 *
 * Returns the total number of goals scored by a team in a given year.
 *
 * The team can play as:
 * - team1 (home team)
 * - team2 (away team)
 *
 * So we need to query both positions and sum the results.
 * Results are paginated, so we loop through all pages.
 */
async function getTotalGoals(team, year) {
    let homeGoals = 0;
    let awayGoals = 0;

    // Query 1: Fetch all matches where team is team1 (home) with pagination
    let page = 1;
    while (true) {
        const url = `https://jsonmock.hackerrank.com/api/football_matches?year=${year}&team1=${team}&page=${page}`;
        const res = await fetch(url);
        const body = await res.json();

        // Sum team1goals from this page
        for (let match of body.data) {
            homeGoals += Number(match.team1goals);
        }

        // Move to next page or exit loop
        if (page >= body.total_pages) break;
        page++;
    }

    // Query 2: Fetch all matches where team is team2 (away) with pagination
    page = 1;
    while (true) {
        const url = `https://jsonmock.hackerrank.com/api/football_matches?year=${year}&team2=${team}&page=${page}`;
        const res = await fetch(url);
        const body = await res.json();

        // Sum team2goals from this page
        for (let match of body.data) {
            awayGoals += Number(match.team2goals);
        }

        // Move to next page or exit loop
        if (page >= body.total_pages) break;
        page++;
    }

    return homeGoals + awayGoals;
}

/**
 * getNumDraws(year)
 *
 * Returns the total count of matches that ended in a draw (same score both sides).
 *
 * A draw means: team1goals === team2goals
 * Possible scores: 0-0, 1-1, 2-2, ..., 10-10
 *
 * Query the API for each possible draw score and sum the results.
 */
async function getNumDraws(year) {
    let totalDraws = 0;

    // Query for each possible draw score (0-0 through 10-10)
    for (let i = 0; i <= 10; i++) {
        const url = `https://jsonmock.hackerrank.com/api/football_matches?year=${year}&team1goals=${i}&team2goals=${i}`;
        const res = await fetch(url);
        const body = await res.json();

        // Add total from this score to running total
        totalDraws += body.total;
    }

    return totalDraws;
}

// ============================================================================
// Test both functions with sample data
// ============================================================================

console.log('[script] calling getTotalGoals("Barcelona", 2011)');
getTotalGoals('Barcelona', 2011)
    .then((data) => {
        console.log(`\nTotal goals by Barcelona in 2011: ${data}`);
        console.log('\n=============================================================================\n');

        console.log('[script] calling getNumDraws(2011)');
        return getNumDraws(2011);
    })
    .then((data) => {
        console.log(`\nTotal drawn matches in 2011: ${data}`);
    })
    .catch((err) => console.error('Error:', err));
