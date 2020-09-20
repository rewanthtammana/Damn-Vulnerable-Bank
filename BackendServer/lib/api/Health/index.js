var express = require('express');
var router = express.Router();

var check = require('./check');

router.use('/check', check);

module.exports = router;
