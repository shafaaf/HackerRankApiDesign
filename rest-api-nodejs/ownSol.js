/*
 * jsonmock football API practice.
 *
 * Two endpoints, both paginated. Every response looks like:
 *   { page, per_page, total, total_pages, data: [ ...up to 10 rows... ] }
 *
 * -- football_matches ---------------------------------------------------------
 *   GET https://jsonmock.hackerrank.com/api/football_matches?year=2011&team1goals=1&team2goals=1
 *   {
 *     "page": 1,
 *     "per_page": 10,
 *     "total": 234,          // matches across ALL pages
 *     "total_pages": 24,
 *     "data": [
 *       {
 *         "competition": "UEFA Champions League",
 *         "year": 2011,
 *         "round": "GroupF",
 *         "team1": "Borussia Dortmund",
 *         "team2": "Arsenal",
 *         "team1goals": "1",   // NOTE: goals are STRINGS -> Number() before math
 *         "team2goals": "1"
 *       },
 *       ...
 *     ]
 *   }
 *   Filter params: year, competition, team1, team2, team1goals, team2goals, page
 *
 * -- football_competitions --------------------------------------------------
 *   GET https://jsonmock.hackerrank.com/api/football_competitions?year=2011&name=UEFA%20Champions%20League
 *   {
 *     "page": 1,
 *     "per_page": 10,
 *     "total": 1,
 *     "total_pages": 1,
 *     "data": [
 *       {
 *         "name": "UEFA Champions League",
 *         "country": "",
 *         "year": 2011,
 *         "winner": "Chelsea",
 *         "runnerup": "Bayern Munich"
 *       }
 *     ]
 *   }
 *   Filter params: year, name
 *
 * -- reading a response -----------------------------------------------------
 *   const res  = await fetch(url);   // Response object: res.status, res.ok ...
 *   const body = await res.json();   // the parsed object shown above in the example
 *   body.data                        // the array of rows
 *   body.data[0].winner              // a field on the first row
 *
 * Logging style: `label: ${x}` for primitives, ("label:", obj) for objects/arrays.
 */


/*
 * getDrawnMatches(year)
 *   Count matches that ended level. A draw means team1goals === team2goals,
 *   scores run 0..10, so ask the API 11 times ("team1goals=X & team2goals=X")
 *   and add up the `total` it reports for each. Requests fire in parallel and
 *   are collected with Promise.all.
 */
async function getDrawnMatches(year) {
    const promises = [];

    for (let i = 0; i <= 10; i++) {
        const url = `https://jsonmock.hackerrank.com/api/football_matches?year=${year}&team1goals=${i}&team2goals=${i}`;

        const myPromise = fetch(url)
            .then(res => {
                console.log(`res1 status is: ${res.status}`);
                return res.json();
            })
            .then(body => {
                console.log(`res2 total is: ${body.total}`);
                return body.total;
            });

        promises.push(myPromise);
    }

    const values = await Promise.all(promises); // array of 11 counts
    console.log("values are:", values);
    return values;
}

getDrawnMatches(2011)
    .then((data) => {
        console.log("getDrawnMatches data is:", data);
        const sum = data.reduce((acc, cur) => acc + cur, 0);
        console.log(`sum is: ${sum}`);
    })
    .catch((err) => console.log(err));


/*
 * getWinnerTotalGoals(competition, year)
 *   Total goals scored by the team that won the competition that year.
 *   1. football_competitions -> get the winner's name.
 *   2. football_matches team1=<winner> -> sum team1goals  (home goals).
 *   3. football_matches team2=<winner> -> sum team2goals  (away goals).
 *   answer = home + away. Goals are strings, so each is passed through Number().
 *
 *   Only page 1 of each match query is read. Fine while total_pages === 1;
 *   a team with >10 home or >10 away matches would need a page loop.
 */
async function getWinnerTotalGoals(competition, year) {
    // 1. who won
    const url = `https://jsonmock.hackerrank.com/api/football_competitions?year=${year}&name=${competition}`;
    console.log(`url is: ${url}`);
    const res1 = await fetch(url);
    console.log(`res1 status is: ${res1.status}`);
    console.log("res1 body:", res1);
    const res2 = await res1.json();
    console.log("res2 is:", res2);

    const winner = res2.data[0].winner;
    console.log(`winner is: ${winner}`);

    // 2. home goals: winner as team1
    const url2 = `https://jsonmock.hackerrank.com/api/football_matches?competition=${competition}&year=${year}&team1=${winner}`;
    console.log(`url2 is: ${url2}`);
    const resp1 = await fetch(url2);
    console.log(`resp1 status is: ${resp1.status}`);
    const resp2 = await resp1.json();
    console.log("resp2.data is:", resp2.data);

    let homeGoals = 0;
    for (let i = 0; i < resp2.data.length; i++) {
        homeGoals += Number(resp2.data[i].team1goals);
    }
    console.log(`homeGoals is: ${homeGoals}`);

    // 3. away goals: winner as team2
    const url3 = `https://jsonmock.hackerrank.com/api/football_matches?competition=${competition}&year=${year}&team2=${winner}`;
    console.log(`url3 is: ${url3}`);
    const respo1 = await fetch(url3);
    console.log(`respo1 status is: ${respo1.status}`);
    const respo2 = await respo1.json();
    console.log("respo2.data is:", respo2.data);

    let awayGoals = 0;
    for (let i = 0; i < respo2.data.length; i++) {
        awayGoals += Number(respo2.data[i].team2goals);
    }
    console.log(`awayGoals is: ${awayGoals}`);

    // 4. total
    const totalGoals = homeGoals + awayGoals;
    console.log(`totalGoals is: ${totalGoals}`);
    return totalGoals;
}

getWinnerTotalGoals("UEFA Champions League", 2011)
    .then((data) => {
        console.log(`data is: ${data}`);
    })
    .catch((err) => console.log(err));
