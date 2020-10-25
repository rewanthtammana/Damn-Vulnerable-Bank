var express = require('express');
var router = express.Router();
var Model = require('../../../models/index');
var Response = require('../../Response');
var statusCodes = require('../../statusCodes');
var { validateUserToken } = require("../../../middlewares/validateToken");
const { Op } = require("sequelize");
var { encryptResponse } = require("../../../middlewares/crypt");

/**
 * Transactions viewing route
 * This endpoint allows to view all transactions of authorized user
 * @path                             - /api/transactions/view
 * @middleware
 * @return                           - Status
 */
router.post('/', validateUserToken, (req, res) => {
    var r = new Response();
    let { account_number } = req;
    Model.transactions.findAll({
        where: {
            [Op.or]: [
                { from_account: account_number },
                { to_account: account_number }
            ],
        },
        attributes: ["from_account", "to_account", "amount"]
    }).then((transactions) => {
        r.status = statusCodes.SUCCESS;
        r.data = transactions;
        return res.json(encryptResponse(r));
    }).catch((err) => {
        r.status = statusCodes.SERVER_ERROR;
        r.data = {
            "message": err.toString()
        };
        return res.json(encryptResponse(r));
    });
});

module.exports = router;