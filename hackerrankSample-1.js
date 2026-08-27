// ---------------------------------------------------------------------------
// Verbose version: logs at every step so you can watch the async flow.
// ---------------------------------------------------------------------------

async function getDrawnMatches(year) {
    console.log(`\n[getDrawnMatches] START — year=${year}`);

    let goals = [];   // will hold 11 pending Promises (goal 0..10)
    let ans = 0;

    // -- 1. Fire all requests in the loop (they run in parallel) --------------
    for (let goal = 0; goal <= 10; goal++) {
        const url = `https://jsonmock.hackerrank.com/api/football_matches?year=${year}&team1goals=${goal}&team2goals=${goal}`;
        console.log(`[loop] goal=${goal} — sending request`);

        const myPromise = fetch(url)
            .then(res => {
                console.log(`[response] goal=${goal} — HTTP ${res.status}`);
                return res.json();               // res.json() also returns a Promise
            })
            .then(data => {
                console.log(`[json parsed] goal=${goal} — total=${data.total}, data.length=${data.data.length}`);
                return data.total;
            });

        goals.push(myPromise);
    }

    console.log(`[loop done] ${goals.length} requests in flight, now awaiting all of them...`);

    // -- 2. Wait for every request to finish --------------------------------
    const totals = await Promise.all(goals);
    console.log(`[Promise.all resolved] totals array =`, totals);

    // -- 3. Add them up ---------------------------------------------------------
    totals.forEach((total, goal) => {
        ans += total;
        console.log(`[sum] goal ${goal}-${goal}: +${total}  => running total ${ans}`);
    });

    console.log(`[getDrawnMatches] RETURN — ans=${ans}`);
    return ans;
}

// ---------------------------------------------------------------------------
console.log("[script] calling getDrawnMatches(2011)");

getDrawnMatches(2011)
    .then((answer) => {
        console.log(`[script] .then callback — answer=${answer}`);
        console.log(`\nTotal drawn matches in 2011: ${answer}`);
    });

console.log("[script] this line runs BEFORE the .then callback (async!)");
