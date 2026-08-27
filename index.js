const axios = require("axios");
console.log("DEBUG1");

async function testFunction() {
    console.log("Program started");

    const url = "https://jsonmock.hackerrank.com/api/countries?name=Canada";

    // const response = await axios.get(url, { // https://jsonmock.hackerrank.com/api/countries?name=Canada
    //     params: {
    //         name: "Canada"
    //     }
    // });

    const response = await fetch(url);
    console.log("response is: ");
    console.log(response);

    console.log("\n\n\n\nresponse status:");
    console.log(response.status);

    const responseJson = await response.json();
    console.log("responseJson is: ", responseJson);

    console.log("Total pages:");
    console.log(responseJson.total_pages);

    const data = responseJson.data;
    console.log("data:");
    console.log(data);



    if (data.length > 0) {
        const firstCountry = data[0];

        console.log("First country:");
        console.log(firstCountry);

        console.log("Country name:");
        console.log(firstCountry.name);

        console.log("Capital:");
        console.log(firstCountry.capital);

        console.log(`Returning ${firstCountry.name}`);
        throw new Error("oppa1");
        return firstCountry.name;
    }
}

console.log("DEBUG2");

testFunction()
    .then(data => {
        console.log("We were successful!");
        console.log("data is: " + data);
    })
    .catch(error => {
        console.error("Something failed:");
        console.log(error);
        if (error.response) {
            console.error("HTTP status:", error.response.status);
            console.error("Response:", error.response.data);
        } else {
            console.error("printing now");
            console.error(error.message);
        }
});

console.log("DEBUG3");
console.log("DEBUG4");
