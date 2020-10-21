const express = require("express");
const app = express();
const bodyParser = require("body-parser");
const index = require('./routes/index');

app.use(bodyParser.text({ type: 'text/*' }))

app.use('/api', index);

module.exports = app;
