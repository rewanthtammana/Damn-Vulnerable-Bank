var express = require('express');
var router = express.Router();

var view = require("./view");

router.use('/view', view);

module.exports = router;
