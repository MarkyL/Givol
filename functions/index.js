const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.contestsSchedule = functions.pubsub.schedule('every 1 minutes').onRun(async context => {
//exports.contestsSchedule = functions.https.onRequest((request, response) => {
    console.log('contestsSchedule triggered!!!')
    // Get all active contests
    const activeContests = await getActiveContests();

    //console.log('got the activeContests - ', activeContests)
});

/**
* Returns the list of all active contests.
*/
async function getActiveContests(contests = []) {
    admin.database().ref('/contests/active').on('value', function(snapshot) {
    if (snapshot !== null) {
        //const data = snapshot.val();
        snapshot.children.forEach((contest) => {
            //contest.val().business_id
            console.log("business_id2 = ", contest.val().business_id);
        })
        return snapshot.children;
    } else {
        console.log('snapshot is null');
    }
    return null;
    })
}
