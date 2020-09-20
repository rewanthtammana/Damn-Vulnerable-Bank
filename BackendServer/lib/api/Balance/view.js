var express = require('express');
var router = express.Router();
var Model = require('../../../models/index');
var Response = require('../../Response');
var statusCodes = require('../../statusCodes');
var { validateUserToken } = require("../../../middlewares/validateToken");

/**
 * Balance view route
 * @path                 - /api/balance/view
 * @middleware
 * @return               - Status: balance and account number
 */
router.post('/', validateUserToken, (req, res) => {
    var r = new Response();
    // let account_number = req.params.id;
    console.log("Balance/view page = ", req.account_number);
    Model.users.findOne({
        where: {
            account_number: req.account_number
        },
        attributes: ["balance", "account_number"]
    }).then((user) => {
        if(user) {
            r.status = statusCodes.SUCCESS;
            r.data = user;
            return res.json(r);
        } else {
            r.status = statusCodes.NOT_AUTHORIZED;
            return res.json(r);
        }
    }).catch((err) => {
        r.status = statusCodes.SERVER_ERROR;
        r.data = {
            "error": err.toString()
        };
        res.json(r);
    });
});

module.exports = router;