const Model = require("../models/index");
const Response = require('../lib/Response');
const statusCodes = require("../lib/statusCodes");
const jwt = require("jsonwebtoken");
var { encryptResponse } = require("../middlewares/crypt");

/**
 * User token validation middleware
 * This middleware validates user JWT token, extracts the associated
 * account number of user and adds it to the request object
 * @header authorization             - JWT token
 * @return                           - Calls the next function on success
 */
const validateUserToken = function(req, res, next) {
  var r = new Response();

  const authHeader = req.headers["authorization"];
  const token = authHeader && authHeader.split(" ")[1];

  if (token == null) {
      r.status = statusCodes.NOT_AUTHORIZED;
      return res.json(encryptResponse(r));
  }

  jwt.verify(token, "secret", (err, data) => {
      if (err) {
          r.status = statusCodes.FORBIDDEN;
          r.data = {
              "message": err.toString()
          }
          return res.json(encryptResponse(r));
      }
      
      Model.users.findOne({
          where: {
              username: data.username
          },
          attributes: ["account_number"]
      }).then((data) => {
          req.account_number = data.account_number;
          next();
      }).catch((err) => {
        r.status = statusCodes.SERVER_ERROR;
        r.data = {
            "message": err.toString()
        };
        return res.json(encryptResponse(r));
    });
  });
};

/**
 * Admin token validation middleware
 * This middleware validates admin JWT token, extracts the associated
 * account number of admin and adds it to the request object along with
 * is_admin flag
 * @header authorization             - JWT token
 * @return                           - Calls the next function on success
 */
const validateAdminToken = function(req, res, next) {
    var r = new Response();
  
    const authHeader = req.headers["authorization"];
    const token = authHeader && authHeader.split(" ")[1];
  
    if (token == null) {
        r.status = statusCodes.NOT_AUTHORIZED;
        return res.json(encryptResponse(r));
    }
  
    jwt.verify(token, "secret", (err, data) => {
        if (err) {
            r.status = statusCodes.FORBIDDEN;
            return res.json(encryptResponse(r));
        }
        
        Model.users.findOne({
            where: {
                username: data.username
            },
            attributes: ["account_number", "is_admin"]
        }).then((data) => {
            req.account_number = data.account_number;
            if (!data.is_admin) {
                r.status = statusCodes.FORBIDDEN;
                r.data = {
                    "message": "Exclusive endpoint for admins only"
                };
                return res.json(encryptResponse(r));
            } else {
                next();
            }
        }).catch((err) => {
            r.status = statusCodes.SERVER_ERROR;
            r.data = {
                "message": err.toString()
            };
            return res.json(encryptResponse(r));
        });
    });
};

module.exports =  {
    validateUserToken,
    validateAdminToken
}
