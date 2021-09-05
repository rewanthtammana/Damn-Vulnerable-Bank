var express = require('express');
var pdf = require("pdf-creator-node");
var fs = require("fs");
var path = require("path");
var html = fs.readFileSync(path.join(__dirname, "./template.html"), "utf8");
var router = express.Router();
var Model = require('../../../models/index');
var Response = require('../../Response');
var statusCodes = require('../../statusCodes');
var { validateUserToken } = require("../../../middlewares/validateToken");
const { Op } = require("sequelize");
var { encryptResponse } = require("../../../middlewares/crypt");

/**
 * Transactions download route
 * This endpoint allows to view download transactions of authorized user
 * @path                             - /api/transactions/download
 * @middleware
 * @return                           - Status
 */
router.post('/', validateUserToken, (req, res) => {
    var r = new Response();
    let { account_number } = req;
    var user_data = {};

    // Retrieves user information
    Model.users.findOne({
        where: {
            account_number: req.account_number
        },
        attributes: ["balance", "account_number", "username", "is_admin"]
    }).then((user) => {
        if (user) {
            user_data = JSON.parse(JSON.stringify({
                user: user
            })).user
        } else {
            r.status = statusCodes.NOT_AUTHORIZED;
            r.data = {
                "message": "Not authorized"
            }
            return res.json(encryptResponse(r));
        }
    }).catch((err) => {
        r.status = statusCodes.SERVER_ERROR;
        r.data = {
            "message": err.toString()
        };
        res.json(encryptResponse(r));
    });

    // Retrives transaction information
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

        transactions_parsed = JSON.parse(JSON.stringify({
            transactions: transactions
        })).transactions

        var options = {
            format: "A3",
            orientation: "portrait",
            border: "10mm",
        };

        var document = {
            html: html,
            data: {
                transactions: transactions_parsed,
                user: user_data,
            },
            path: "./output.pdf",
            type: "buffer",
        };

        // Generates pdf as buffer
        pdf
            .create(document, options)
            .then((resx) => {
                r.data = resx.toString('utf8');
                return res.json(encryptResponse(r));
            })
            .catch((error) => {
                r.status = statusCodes.SERVER_ERROR;
                r.data = {
                    "message": err.toString()
                };
                return res.json(encryptResponse(r));
            });
    }).catch((err) => {
        r.status = statusCodes.SERVER_ERROR;
        r.data = {
            "message": err.toString()
        };
        return res.json(encryptResponse(r));
    });
});

module.exports = router;
