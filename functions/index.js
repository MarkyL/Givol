const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
//const promisePool = require('es6-promise-pool');
//const PromisePool = promisePool.PromisePool;

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
 exports.helloWorld = functions.https.onRequest((request, response) => {
   functions.logger.info("Hello logs, Mark testing!", {structuredData: true});
   response.send("Hello from Firebase!");
 });

 // /**
  // * Run once a day at 21:00, to initiate the contests that have ended.
  // */
 // exports.runContests = functions.pubsub.schedule('every day 21:00').onRun(async contest => {
    // Fetch all contests
    // const activeContests = await getActiveContests();

 // });

 // /**
  // * Returns the list of all active contests
  // */
  // async function getActiveContests(activeContests = []) {
    // const result = await admin.auth().
  // }

// Listens for new contests added to /contests/active and creates an
// uppercase version of the contest to /contests/active/uppercase
exports.makeUppercase = functions.database.ref('/contests/active')
    .onCreate((snapshot, context) => {
      functions.logger.info("makeUppercase trigerred!!!", {structuredData: true});
      // Grab the current value of what was written to the Realtime Database.
      const original = snapshot.val();

      console.log('Uppercasing', original);
      const uppercase = "lets first check this is working" //original.business_name.toUpperCase();
      // You must return a Promise when performing asynchronous tasks inside a Functions such as
      // writing to the Firebase Realtime Database.
      // Setting an "uppercase" sibling in the Realtime Database returns a Promise.
      return snapshot.ref.parent.child('markTest').set(uppercase);
    });

    
