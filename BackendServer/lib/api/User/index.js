var express = require('express');
var router = express.Router();

var login = require("./login");
var register = require("./register");
var profile = require("./profile");
var changePassword = require("./changePassword");

router.use('/login', login);
router.use('/register', register);
router.use('/profile', profile);
router.use("/change-password", changePassword);

module.exports = router;
