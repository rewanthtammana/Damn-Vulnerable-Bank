var express = require('express');
var router = express.Router();

var view = require("./view");
var download = require("./download");

router.use('/view', view);
router.use('/download', download);

module.exports = router;
