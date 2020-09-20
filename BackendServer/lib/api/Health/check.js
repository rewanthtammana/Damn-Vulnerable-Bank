var express = require('express');
var router = express.Router();
var Model = require('../../../models/index');
var Response = require('../../Response');
var statusCodes = require('../../statusCodes');

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