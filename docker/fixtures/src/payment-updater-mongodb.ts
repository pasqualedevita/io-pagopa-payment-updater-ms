import { log } from "./utils/logger";
import * as TE from "fp-ts/TaskEither";
import * as E from "fp-ts/Either";
import { pipe } from "fp-ts/lib/function";
import { MongoClient, ObjectId } from "mongodb";

export const fillMongoDb = async (
    mongodbUri: string, // "mongodb://user:S3cret@mongodb:27017"
    mongodbName: string // io-d-producer-mongodb
  ): Promise<void> => {
    log("filling MongoDB for payment-updater");

    await pipe(
        TE.tryCatch(() => MongoClient.connect(mongodbUri), E.toError),
        TE.map(db => db.db(mongodbName)),
        TE.chain(dbo => TE.tryCatch(() => dbo.createCollection("payment"), E.toError)),
        TE.map(payment => payment.insertMany([
            {
            "_id" : new ObjectId(60200),
            "readFlag" : false,
            "paidFlag" : false,
            "insertionDate" : {
                "$date" : 1655989914400
            },
            "maxReadMessageSend" : 0,
            "maxPaidMessageSend" : 0,
            "senderServiceId" : "Reminder",
            "senderUserId" : "undefined",
            "timeToLiveSeconds" : 100,
            "createdAt" : 0,
            "isPending" : true,
            "content_subject" : "subject",
            "content_type" : "PAYMENT",
            "content_paymentData_amount" : 500.84,
            "content_paymentData_noticeNumber" : "A60200",
            "content_paymentData_invalidAfterDueDate" : false,
            "content_paymentData_payeeFiscalCode" : "ALSDK54654asd",
            "timestamp" : 0,
            "fiscal_code" : "BBBPPP77J99A888A",
            "content_paymentData_dueDate" : {
                "$date" : 1654819200000
            },
            "_class" : "it.gov.pagopa.paymentupdater.model.Payment"
            },{
            "_id" : new ObjectId(60201),
            "readFlag" : true,
            "paidFlag" : true,
            "insertionDate" : {
                "$date" : 1655989914872
            },
            "maxReadMessageSend" : 0,
            "maxPaidMessageSend" : 0,
            "senderServiceId" : "Reminder",
            "senderUserId" : "undefined",
            "timeToLiveSeconds" : 100,
            "createdAt" : 0,
            "isPending" : true,
            "content_subject" : "subject",
            "content_type" : "PAYMENT",
            "content_paymentData_amount" : 500.76,
            "content_paymentData_noticeNumber" : "A61200",
            "content_paymentData_invalidAfterDueDate" : false,
            "content_paymentData_payeeFiscalCode" : "ALSDK54654asd",
            "timestamp" : 0,
            "fiscal_code" : "BBBPPP77J99A888A",
            "content_paymentData_dueDate" : {
                "$date" : 1654819200000
            },
            "_class" : "it.gov.pagopa.paymentupdater.model.Payment"
            }
        ])),
        TE.map(i =>{console.log("fillMOngoDB completed!"); return i;}),
        TE.mapLeft(e => {console.log("fillMongoDB error! "+e.message); throw e;})
    )();
  };