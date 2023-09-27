// 사용자 프로필 정보를 반환

var express = require('express');
var router = express.Router();
var Model = require('../../../models/index');
var Response = require('../../Response');
var statusCodes = require('../../statusCodes');
var { validateUserToken } = require("../../../middlewares/validateToken");
var { encryptResponse, decryptRequest } = require("../../../middlewares/crypt");

/**
 * User profile route
 * This endpoint allows the user to see profile
 * @path                             - /api/user/profile
 * @middleware
 * @return                           - Status: Balance, Account number, username, is_admin
 */
// /api/user/profile
// validateUserToken 미들웨어를 실행한 후에 콜백 함수가 실행
// ==> 사용자의 토큰을 검증하여 인증된 사용자만 프로필 정보에 접근할 수 있도록 한다.
router.post('/', validateUserToken, (req, res) => {
    var r = new Response();
    // 사용자 프로필 정보 조회
    // 사용자의 계좌 번호(account_number)를 기준으로 정보를 검색
    Model.users.findOne({
        where: {
            account_number: req.account_number
        },
        attributes: ["balance", "account_number", "username", "is_admin"]
    }).then((user) => {
        // 응답 처리
        if(user) {
            r.status = statusCodes.SUCCESS;
            r.data = user;
            return res.json(encryptResponse(r));
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
