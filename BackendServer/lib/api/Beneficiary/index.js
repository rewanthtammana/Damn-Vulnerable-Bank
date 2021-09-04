var express = require('express');
var router = express.Router();

var add = require("./add");
var approve = require("./approve");
var pending = require("./pending");
var view = require("./view");
var remove = require("./delete");

router.use("/add", add);
router.use("/delete", remove);
router.use("/approve", approve);
router.use("/pending", pending);
router.use("/view", view); // Give options on FE to see pending list as well

module.exports = router;
