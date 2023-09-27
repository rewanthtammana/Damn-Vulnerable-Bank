// 사용자 로그인 처리
// 데이터베이스 모델(Model), 응답 객체(Response), HTTP 상태 코드(statusCodes), 암호화 및 복호화 미들웨어 등을 로드

var express = require('express');
var router = express.Router();
var Model = require('../../../models/index');
var Response = require('../../Response');
var statusCodes = require('../../statusCodes');
var { encryptResponse, decryptRequest } = require("../../../middlewares/crypt");
const jwt = require("jsonwebtoken");

/**
 * Login route
 * This endpoint allows the user to login
 * @path                             - /api/user/login
 * @middleware                       - Checks admin authorization
 * @param username                   - Username to login
 * @param password                   - Password to login
 * @return                           - JWT token
 */
 // HTTP POST 요청을 처리하는 라우트를 정의
 // /api/user/login 경로로 접근
router.post('/', decryptRequest, (req, res) => {
    var r = new Response();
    let username = req.body.username;
    let password = req.body.password;

    // 로그인 로직
    // 사용자가 제출한 사용자 이름(username)과 비밀번호(password)를 추출
    Model.users.findOne({
        where: {
            username: username,
            password: password
        }
    }).then((data) => {
        if(data) {
            // JWT 토큰을 생성
            const accessToken = jwt.sign({
            // 데이터베이스 모델을 사용하여 해당 사용자 정보를 검색
                username: data.username,
                is_admin: data.is_admin
            }, "secret");
            r.status = statusCodes.SUCCESS;
            r.data = {
                "accessToken": accessToken
            };
            return res.json(encryptResponse(r));
        } else {
        // 검색 결과에 따라 JWT 토큰을 생성하거나 인증 오류 메시지를 반환
            r.status = statusCodes.BAD_INPUT;
            r.data = {
                "message": "Incorrect username or password"
            }
            return res.json(encryptResponse(r));
        }
    }).catch((err) => {
        r.status = statusCodes.SERVER_ERROR;
        r.data = {
            "message": err.toString()
        };
        return res.json(encryptResponse(r));
    });
});

module.exports = router;