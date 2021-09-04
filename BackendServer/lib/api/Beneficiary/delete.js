var express = require("express");
var router = express.Router();
var Model = require("../../../models/index");
var Response = require("../../Response");
var statusCodes = require("../../statusCodes");
var { validateUserToken } = require("../../../middlewares/validateToken");
var { encryptResponse, decryptRequest } = require("../../../middlewares/crypt");

/**
 * Beneficiary delete route
 * @path                 - /api/beneficiary/delete
 * @middleware
 * @param account_number - Beneficiary account number to be deleted
 * @return               - Status
 */
router.post("/", [validateUserToken, decryptRequest], (req, res) => {
    var r = new Response();
    let beneficiary_account_number = req.body.account_number;
    let { account_number } = req;

    if (beneficiary_account_number == "") {
        r.status = statusCodes.BAD_INPUT;
        r.data = {
            "message": "Beneficiary account number cannot be empty"
        };
        return res.json(encryptResponse(r));
    }

    // Inject a vulnerability here to show whether a particular beneficiary account exists or not.
    Model.beneficiaries.destroy({
        where: {
            account_number: account_number,
            beneficiary_account_number: beneficiary_account_number
        }
    }).then(() => {
        r.status = statusCodes.SUCCESS;
        r.data = {
            "message": "Request processed successfully"
        }
        return res.json(encryptResponse(r));
    }).catch((err) => {
        r.status = statusCodes.SERVER_ERROR;
        r.data = err;
        return res.json(encryptResponse(r));
    });
});


module.exports = router;