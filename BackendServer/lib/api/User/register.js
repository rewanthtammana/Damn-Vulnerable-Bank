// 사용자 등록 처리

var express = require('express');
var router = express.Router();
var Model = require('../../../models/index');
var Response = require('../../Response');
var statusCodes = require('../../statusCodes');
var { encryptResponse, decryptRequest } = require("../../../middlewares/crypt");

/**
 * Registration route
 * This endpoint allows the user to register
 * Additionally this also creates a new account for this user
 * @path                             - /api/user/register
 * @middleware                       - Checks admin authorization
 * @param username                   - Username to login
 * @param password                   - Password to login
 * @return                           - Status
 */
// /api/user/register
router.post('/', decryptRequest, (req, res) => {
    var r = new Response();
    // 클라이언트에서 받은 사용자 이름(username)과 비밀번호(password)를 추출하고,
    // 새로운 계정 번호(account_number)를 무작위로 생성(random)
    let username = req.body.username;
    let password = req.body.password;
    let email = req.body.email;
    let account_number = Math.random() * 888888 + 111111;

    // 데이터베이스를 조회하여 이미 동일한 사용자 이름이 있는지 확인
    // 없으면 계정 생성, 있으면 오류 코드와 메세지 반환
    Model.users.findAll({
        where: {
            username: username
        }
    }).then((data) => {
        if(data == "") {
            Model.users.findOne({
                account_number: account_number
            }).then((data) => {
                // Regenerates new account number if account number exists
                if(data) {
                    account_number = Math.random() * 888888 + 111111;
                }

                Model.users.create({
                    username: username,
                    password: password,
                    email: email,
                    account_number: account_number
                }).then(() => {
                    r.status = statusCodes.SUCCESS;
                    r.data = {
                        "message": "Sucess"
                    }
                    res.json(encryptResponse(r));
                }).catch((err) => {
                    r.status = statusCodes.SERVER_ERROR;
                    r.data = {
                        "message": err.toString()
                    };
                    res.json(encryptResponse(r));
                });
            });
        } else {
            r.status = statusCodes.BAD_INPUT;
            r.data = {
                "message": "Username already taken"
            };
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
