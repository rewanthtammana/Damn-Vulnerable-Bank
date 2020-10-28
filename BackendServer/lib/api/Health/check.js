var express = require('express');
var router = express.Router();
var Model = require('../../../models/index');
var Response = require('../../Response');
var statusCodes = require('../../statusCodes');
var { encryptResponse } = require("../../../middlewares/crypt");

/**
 * Health checks route
 * This endpoint allows to check server status and database connection from the application
 * @path                             - /api/health/check
 * @return                           - Status
 */
router.get('/', function(req, res) {
  var r = new Response();
  Model.users.findAll().then(function(data) {
    r.status = statusCodes.SUCCESS;
    r.data = {
      "message": "Sucess"
    }
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