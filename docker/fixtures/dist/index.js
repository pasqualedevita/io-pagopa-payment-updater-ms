"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
/* eslint-disable no-console */
const payment_updater_mongodb_1 = require("./payment-updater-mongodb");
const main = async () => {
    await ((0, payment_updater_mongodb_1.fillMongoDb)("mongodb://user:S3cret@mongodb:27017", "io-d-producer-mongodb"));
};
console.log("Setting up data....");
main()
    .then(_ => {
    console.log("Fixtures set up");
}, _reject => {
    console.log(`rejection:`);
    console.log(_reject);
    process.exit(1);
})
    .catch(_err => {
    console.log(`error:`);
    console.log(_err);
    process.exit(1);
});
//# sourceMappingURL=index.js.map