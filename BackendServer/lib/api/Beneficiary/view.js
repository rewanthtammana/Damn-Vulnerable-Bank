var express = require('express');
var router = express.Router();
var Model = require('../../../models/index');
var Response = require('../../Response');
var statusCodes = require('../../statusCodes');
var { validateUserToken } = require("../../../middlewares/validateToken");
var { encryptResponse } = require("../../../middlewares/crypt");

/**
 * Beneficiary view route
 * This endpoint allows to view beneficiary requests of authorized user
 * @path                             - /api/beneficiary/view
 * @middleware
 * @return                           - Status
 */
router.post('/', validateUserToken, (req, res) => {
    var r = new Response();
    let { account_number } = req;
    
    Model.beneficiaries.findAll({
        where: {
            account_number: account_number,
        },
        attributes: ["beneficiary_account_number", "approved"]
    }).then((data) => {
        r.status = statusCodes.SUCCESS;
        r.data = data;
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