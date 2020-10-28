var express = require('express');
var router = express.Router();
var Model = require('../../../models/index');
var Response = require('../../Response');
var statusCodes = require('../../statusCodes');
var { validateUserToken } = require("../../../middlewares/validateToken");
var { encryptResponse, decryptRequest } = require("../../../middlewares/crypt");

/**
 * Change password route
 * This endpoint allows the user to change password
 * @path                             - /api/user/change-password
 * @middleware
 * @param password                   - Previous password
 * @param new_password               - New password
 * @return                           - Status
 */
router.post('/', [validateUserToken, decryptRequest], (req, res) => {
    var r = new Response();
    let current_password = req.body.password;
    let new_password = req.body.new_password;
    Model.users.findOne({
        where: {
            account_number: req.account_number
        },
        attributes: ["account_number", "password"]
    }).then((user) => {        
        if(user) {
            if (current_password == new_password) {
                r.status = statusCodes.BAD_INPUT;
                r.data = {
                    "message": "Current password and new password cannot be same"
                };
                return res.json(encryptResponse(r));
            } else if (user.password == current_password) {
                Model.users.update({
                    password: new_password
                }, {
                    where: {
                        account_number: user.account_number
                    }
                }).then(() => {
                    r.status = statusCodes.SUCCESS;
                    r.data = {
                        "message": "Password changed successfully"
                    }
                    return res.json(encryptResponse(r));
                });
            } else {
                r.status = statusCodes.BAD_INPUT;
                r.data = {
                    "message": "Provided password doesn't match with current password"
                }
                return res.json(encryptResponse(r));
            }
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
});

module.exports = router;
