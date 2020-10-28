var express = require('express');
var router = express.Router();
var Model = require('../../../models/index');
var Response = require('../../Response');
var statusCodes = require('../../statusCodes');
var { validateAdminToken } = require("../../../middlewares/validateToken");
var { encryptResponse, decryptRequest } = require("../../../middlewares/crypt");

/**
 * Beneficiary approve route
 * This endpoint allows to approve beneficiary requests of any user from this endpoint
 * @path                             - /api/beneficiary/approve
 * @middleware                       - Checks admin authorization
 * @param id                         - ID to be approved
 * @return                           - Status
 */
router.post('/', [validateAdminToken, decryptRequest], (req, res) => {
    var r = new Response();
    let { id } = req.body;

    Model.beneficiaries.findOne({
        where: {
            id: id,
            approved: false
        }
    }).then((data) => {
        if (data) {
            Model.users.findOne({
                where: {
                    account_number: data.beneficiary_account_number
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
                        r.data = {
                            "message": "Sucess"
                        }
                        return res.json(encryptResponse(r));
                    });
                } else {
                    r.status = statusCodes.BAD_INPUT;
                    r.data = {
                        "message": "Beneficiary with given account number doesn't exist"
                    };
                    return res.json(encryptResponse(r));
                }
            });

        } else {
            r.status = statusCodes.BAD_INPUT;
            r.data = {
                "message": "Pending beneficiary with given details not found"
            }
            return res.json(encryptResponse(r));
        }
    }).catch((err) => {
        console.log()
        r.status = statusCodes.SERVER_ERROR;
        r.data = {
            "message": err.toString()
        };
        res.json(encryptResponse(r));
    });
});


module.exports = router;
