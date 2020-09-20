var express = require('express');
var router = express.Router();
var Model = require('../../../models/index');
var Response = require('../../Response');
var statusCodes = require('../../statusCodes');
var { validateAdminToken } = require("../../../middlewares/validateToken");

/**
 * Beneficiary approve route
 * This endpoint allows to view pending beneficiary requests of any user
 * @path                             - /api/beneficiary/pending
 * @middleware                       - Checks admin authorization
 * @return                           - Status
 */
router.post('/', validateAdminToken, (req, res) => {
    var r = new Response();

    Model.beneficiaries.findAll({
        where: {
            approved: false
        },
        attributes: ["account_number", "beneficiary_account_number", "id"]
    }).then((data) => {
        r.status = statusCodes.SUCCESS;
        r.data = data;
        return res.json(r);
    }).catch((err) => {
        r.status = statusCodes.SERVER_ERROR;
        r.data = {
            "error": err.toString()
        };
        return res.json(r);
    });

});


module.exports = router;