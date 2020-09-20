var express = require('express');
var router = express.Router();
var Model = require('../../../models/index');
var Response = require('../../Response');
var statusCodes = require('../../statusCodes');
var { validateAdminToken } = require("../../../middlewares/validateToken");

/**
 * Beneficiary approve route
 * This endpoint allows to approve beneficiary requests of any user from this endpoint
 * @path                             - /api/beneficiary/approve
 * @middleware                       - Checks admin authorization
 * @param id                         - ID to be approved
 * @return                           - Status
 */
router.post('/', validateAdminToken, (req, res) => {
    var r = new Response();
    let { id } = req.body;

    Model.beneficiaries.findOne({
        where: {
            id: id,
            approved: false
        }
    }).then((data) => {
        if (data) {
            Model.beneficiaries.update({
                approved: true
            }, {
                where: {
                    id: id
                }
            }).then(() => {
                r.status = statusCodes.SUCCESS;
                return res.json(r);
            });
        } else {
            r.status = statusCodes.BAD_INPUT;
            r.data = {
                "message": "Pending beneficiary with given details not found"
            }
            return res.json(r);
        }
    }).catch((err) => {
        console.log()
        r.status = statusCodes.SERVER_ERROR;
        r.data = {
            "error": err.toString()
        };
        res.json(r);
    });
});


module.exports = router;