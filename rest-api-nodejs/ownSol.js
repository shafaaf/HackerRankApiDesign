/*
 * jsonmock football API practice.
 *
 * Two functions:
 * 1. getTotalGoals(team, year) - total goals scored by team in given year
 * 2. getNumDraws(year) - count of matches that ended in draws
 */

/*
 * getTotalGoals(team, year)
 *   Sum all goals scored by a team in a given year.
 *   Team can play as team1 (home) or team2 (away), so query both.
 */
async function getTotalGoals(team, year) {
    let homeGoals = 0;
    let awayGoals = 0;

    // 1. Fetch all matches where team is team1 (home) - handle pagination
    let page = 1;
    while (true) {
        const url = `https://jsonmock.hackerrank.com/api/football_matches?year=${year}&team1=${team}&page=${page}`;

        const res = await fetch(url);
        const body = await res.json();

        // Sum team1goals from this page
        for (let match of body.data) {
            homeGoals += Number(match.team1goals);
        }

        // Check if there are more pages
        if (page >= body.total_pages) {
            break;
        }
        page++;
    }

    // 2. Fetch all matches where team is team2 (away) - handle pagination
    page = 1;
    while (true) {
        const url = `https://jsonmock.hackerrank.com/api/football_matches?year=${year}&team2=${team}&page=${page}`;

        const res = await fetch(url);
        const body = await res.json();

        // Sum team2goals from this page
        for (let match of body.data) {
            awayGoals += Number(match.team2goals);
        }

        // Check if there are more pages
        if (page >= body.total_pages) {
            break;
        }
        page++;
    }

    return homeGoals + awayGoals;
}

/*
 * getNumDraws(year)
 *   Count matches that ended in a draw (same score both sides).
 *   Query for each possible draw score (0-0, 1-1, ..., 10-10).
 */
async function getNumDraws(year) {
    let totalDraws = 0;

    // Query for each possible draw score (0-0 through 10-10)
    for (let i = 0; i <= 10; i++) {
        const url = `https://jsonmock.hackerrank.com/api/football_matches?year=${year}&team1goals=${i}&team2goals=${i}`;

        const res = await fetch(url);
        const body = await res.json();

        totalDraws += body.total;
    }

    return totalDraws;
}

// Test both functions
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
    .catch((err) => console.log(err));
