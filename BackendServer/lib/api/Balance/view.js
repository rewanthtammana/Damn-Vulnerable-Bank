var express = require('express');
var router = express.Router();
var Model = require('../../../models/index');
var Response = require('../../Response');
var statusCodes = require('../../statusCodes');
var { validateUserToken } = require("../../../middlewares/validateToken");
var { encryptResponse } = require("../../../middlewares/crypt");

/**
 * Balance view route
 * @path                 - /api/balance/view
 * @middleware
 * @return               - Status: balance and account number
 */
router.post('/', validateUserToken, (req, res) => {
    var r = new Response();

    Model.users.findOne({
        where: {
            account_number: req.account_number
        },
        attributes: ["balance", "account_number"]
    }).then((data) => {
        if(data) {
            r.status = statusCodes.SUCCESS;
            r.data = data;
            return res.json(encryptResponse(r));
        } else {
            r.status = statusCodes.NOT_AUTHORIZED;
            r.data = {
                "message": "Not authorized"
            }
            return res.json(encryptResponse(r));;
        }
    }).catch((err) => {
        r.status = statusCodes.SERVER_ERROR;
        r.data = {
            "message": err.toString()
        };
        res.json(encryptResponse(r));;
    });
});

module.exports = router;