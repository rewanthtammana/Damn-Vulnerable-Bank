const express = require("express");
const router = express.Router();

const healthRouter = require("../lib/api/Health");
const transactionsRouter = require("../lib/api/Transactions")
const balanceRouter = require("../lib/api/Balance");
const beneficiaryRouter = require("../lib/api/Beneficiary");
const userRouter = require("../lib/api/User");

router.use("/balance", balanceRouter);
router.use("/transactions", transactionsRouter)
router.use("/health", healthRouter);
router.use("/beneficiary", beneficiaryRouter);
router.use("/user", userRouter);

module.exports = router;
