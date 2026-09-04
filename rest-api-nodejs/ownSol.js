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
 * Calculates the total number of goals scored by a team in a given year across all matches.
 *
 * A team can play in two positions: as team1 (home) or team2 (away), so this function
 * queries both positions separately and sums the results. The API returns paginated results
 * (up to 10 matches per page), so we loop through all pages to get complete results.
 *
 * @param {string} team - The name of the team (e.g., "Barcelona")
 * @param {number} year - The year to query (e.g., 2011)
 * @returns {Promise<number>} - Total goals scored by the team in that year
 *
 * Example: getTotalGoals("Barcelona", 2011) => 35
 */
async function getTotalGoals(team, year) {
    let homeGoals = 0;
    let awayGoals = 0;

    // ===== PART 1: Query matches where team is HOME (team1) =====
    // Fetch all pages of matches where the team played as the home team
    let page = 1;
    while (true) {
        const url = `https://jsonmock.hackerrank.com/api/football_matches?year=${year}&team1=${team}&page=${page}`;
        const res = await fetch(url);
        const body = await res.json();

        // Accumulate goals scored as home team from this page
        // Note: team1goals comes as a string, so convert to number before adding
        for (let match of body.data) {
            homeGoals += Number(match.team1goals);
        }

        // Exit loop once we've processed all pages
        if (page >= body.total_pages) break;
        page++;
    }

    // ===== PART 2: Query matches where team is AWAY (team2) =====
    // Fetch all pages of matches where the team played as the away team
    page = 1;
    while (true) {
        const url = `https://jsonmock.hackerrank.com/api/football_matches?year=${year}&team2=${team}&page=${page}`;
        const res = await fetch(url);
        const body = await res.json();

        // Accumulate goals scored as away team from this page
        // Note: team2goals comes as a string, so convert to number before adding
        for (let match of body.data) {
            awayGoals += Number(match.team2goals);
        }

        // Exit loop once we've processed all pages
        if (page >= body.total_pages) break;
        page++;
    }

    // Return the combined total of home and away goals
    return homeGoals + awayGoals;
}

/**
 * getNumDraws(year)
 *
 * Counts the total number of matches that ended in a draw (both teams scored equally)
 * for a given year.
 *
 * A draw occurs when both teams score the same number of goals (e.g., 0-0, 1-1, 2-2, etc.).
 * Since we can't query "all draws" in one request, we query each possible draw score
 * (0 through 10) and sum the results. The API's "total" field tells us how many matches
 * had that exact draw score, so we just add them up.
 *
 * @param {number} year - The year to query (e.g., 2011)
 * @returns {Promise<number>} - Total number of drawn matches in that year
 *
 * Example: getNumDraws(2011) => 516
 */
async function getNumDraws(year) {
    let totalDraws = 0;

    // Loop through all possible draw scores from 0-0 up to 10-10
    // For each score, query the API and add the total to our running count
    for (let i = 0; i <= 10; i++) {
        const url = `https://jsonmock.hackerrank.com/api/football_matches?year=${year}&team1goals=${i}&team2goals=${i}`;
        const res = await fetch(url);
        const body = await res.json();

        // The "total" field in the response tells us how many matches had this exact draw score
        // We don't need to loop through pages here because "total" is the complete count
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
