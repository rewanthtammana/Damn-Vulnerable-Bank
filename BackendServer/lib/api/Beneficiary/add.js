var express = require("express");
var router = express.Router();
var Model = require("../../../models/index");
var Response = require("../../Response");
var statusCodes = require("../../statusCodes");
var { validateUserToken } = require("../../../middlewares/validateToken");
var { encryptResponse, decryptRequest } = require("../../../middlewares/crypt");

/**
 * Beneficiary add route
 * @path                 - /api/beneficiary/add
 * @middleware
 * @param account_number - Beneficiary account number to be added to the list
 * @return               - Status
 */
router.post("/", [validateUserToken, decryptRequest], (req, res) => {
    var r = new Response();
    let beneficiary_account_number = req.body.account_number;
    let { account_number } = req;

    if (account_number == beneficiary_account_number) {
        r.status = statusCodes.BAD_INPUT;
        r.data = {
            "message": "Cannot add self account to beneficiary"
        };
        return res.json(encryptResponse(r));
    } else if (beneficiary_account_number == "") {
        r.status = statusCodes.BAD_INPUT;
        r.data = {
            "message": "Beneficiary account number cannot be empty"
        };
        return res.json(encryptResponse(r));
    }
    
    Model.beneficiaries.findAll({
        where: {
            account_number: account_number
        },
        attributes: ["beneficiary_account_number"]
    }).then((data) => {
        let arr = data.map((elem) => elem.beneficiary_account_number);
        if (isNaN(parseInt(beneficiary_account_number))) {
            r.status = statusCodes.BAD_INPUT;
            r.data = {
                "message": "Enter valid account number"
            };
            return res.json(encryptResponse(r));
        } else if (arr.includes(parseInt(beneficiary_account_number))) {
            r.status = statusCodes.BAD_INPUT;
            r.data = {
                "message": "Account already exists in beneficiary list"
            };
            return res.json(encryptResponse(r));
        } else {
            Model.beneficiaries.create({
                account_number: account_number,
                beneficiary_account_number: beneficiary_account_number
            }).then(() => {
                r.status = statusCodes.SUCCESS;
                r.data = {
                    "message": "Success"
                }
                return res.json(encryptResponse(r));
            }).catch((err) => {
                r.status = statusCodes.SERVER_ERROR;
                r.data = err;
                return res.json(encryptResponse(r));
            });
        }

    }).catch((err) => {
        r.status = statusCodes.SERVER_ERROR;
        r.data = err;
        return res.json(encryptResponse(r));
    });
});


module.exports = router;