var express = require('express');
var router = express.Router();
var Model = require('../../../models/index');
var Response = require('../../Response');
var statusCodes = require('../../statusCodes');

/**
 * User profile route
 * This endpoint allows the user to see profile
 * @path                             - /api/user/profile
 * @middleware
 * @return                           - Status: Balance, Account number
 */
router.post('/profile', (req, res) => {
    // TODO
});

/**
 * Change password route
 * This endpoint allows the user to change password
 * @path                             - /api/user/change-password
 * @middleware
 * @param password                   - Previous password
 * @param new_password               - New password
 * @return                           - Status
 */
router.post('/change-password', (req, res) => {
    // TODO
});

module.exports = router;
