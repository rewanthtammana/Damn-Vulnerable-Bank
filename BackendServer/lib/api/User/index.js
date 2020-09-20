const express = require('express');
const router = express.Router();

const login = require("./login");
const register = require("./register");
const user = require("./user");

router.use('/login', login);
router.use('/register', register);
// router.use('/', user);

module.exports = router;
