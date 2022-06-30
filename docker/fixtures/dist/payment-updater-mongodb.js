"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.fillMongoDb = void 0;
const logger_1 = require("./utils/logger");
const TE = require("fp-ts/TaskEither");
const E = require("fp-ts/Either");
const function_1 = require("fp-ts/lib/function");
const mongodb_1 = require("mongodb");
const fillMongoDb = async (mongodbUri, // "mongodb://user:S3cret@mongodb:27017"
mongodbName // io-d-producer-mongodb
) => {
    (0, logger_1.log)("filling MongoDB for payment-updater");
    await (0, function_1.pipe)(TE.tryCatch(() => mongodb_1.MongoClient.connect(mongodbUri), E.toError), TE.map(db => db.db(mongodbName)), TE.chain(dbo => TE.tryCatch(() => dbo.createCollection("payment"), E.toError)), TE.map(payment => payment.insertMany([
        {
            "_id": new mongodb_1.ObjectId("1"),
            "readFlag": false,
            "paidFlag": false,
            "insertionDate": {
                "$date": 1655989914400
            },
            "maxReadMessageSend": 0,
            "maxPaidMessageSend": 0,
            "senderServiceId": "Reminder",
            "senderUserId": "undefined",
            "timeToLiveSeconds": 100,
            "createdAt": 0,
            "isPending": true,
            "content_subject": "subject",
            "content_type": "PAYMENT",
            "content_paymentData_amount": 500.84,
            "content_paymentData_noticeNumber": "A60200",
            "content_paymentData_invalidAfterDueDate": false,
            "content_paymentData_payeeFiscalCode": "ALSDK54654asd",
            "timestamp": 0,
            "fiscal_code": "BBBPPP77J99A888A",
            "content_paymentData_dueDate": {
                "$date": 1654819200000
            },
            "_class": "it.gov.pagopa.paymentupdater.model.Payment"
        }, {
            "_id": new mongodb_1.ObjectId("2"),
            "readFlag": true,
            "paidFlag": true,
            "insertionDate": {
                "$date": 1655989914872
            },
            "maxReadMessageSend": 0,
            "maxPaidMessageSend": 0,
            "senderServiceId": "Reminder",
            "senderUserId": "undefined",
            "timeToLiveSeconds": 100,
            "createdAt": 0,
            "isPending": true,
            "content_subject": "subject",
            "content_type": "PAYMENT",
            "content_paymentData_amount": 500.76,
            "content_paymentData_noticeNumber": "A61200",
            "content_paymentData_invalidAfterDueDate": false,
            "content_paymentData_payeeFiscalCode": "ALSDK54654asd",
            "timestamp": 0,
            "fiscal_code": "BBBPPP77J99A888A",
            "content_paymentData_dueDate": {
                "$date": 1654819200000
            },
            "_class": "it.gov.pagopa.paymentupdater.model.Payment"
        }
    ])))();
};
exports.fillMongoDb = fillMongoDb;
//# sourceMappingURL=payment-updater-mongodb.js.map