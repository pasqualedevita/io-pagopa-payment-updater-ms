/* eslint-disable no-console */
import { fillMongoDb } from "./payment-updater-mongodb";

const main = async (): Promise<void> => {
  await(fillMongoDb(
    "mongodb://localhost:C2y6yDjf5/R+ob0N8A7Cgv30VRDJIWEHLM+4QDU5DE2nQ9nDuVTqobD4b8mGGyPMbIZnqyMsEcaGQy67XIw/Jw==@localhost:10255/admin?ssl=true",
    "io-d-producer-mongodb"
  ))
};

console.log("Setting up data....");

main()
  .then(
    _ => {
      console.log("Fixtures set up");
    },
    _reject => {
      console.log(`rejection:`);
      console.log(_reject);
      process.exit(1);
    }
  )
  .catch(_err => {
    console.log(`error:`);
    console.log(_err);
    process.exit(1);
  });
