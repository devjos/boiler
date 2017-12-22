var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var bodyParser = require('body-parser');
var compress = require('compression');
var session = require('express-session');

var indexRoute = require('./routes/index');
var logRoute = require('./routes/log');
var settingsRoute = require('./routes/settings');

var app = express();

// uncomment after placing your favicon in /public
app.use(favicon(path.join(__dirname, 'public', 'img', 'favicon.ico')));
app.use(logger('dev'));
app.use(compress());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(express.static(path.join(__dirname, 'public')));

app.use(session({
  secret: 'ssshhhhh',
  resave: false,
  saveUninitialized: false
}));

app.use('/', indexRoute);
app.use('/log', logRoute);
app.use('/settings', settingsRoute);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

// error handlers
// production error handler
// no stacktraces leaked to user
app.use(function(err, req, res, next) {
  res.status(err.status || 500);
  res.send(err.message);
  console.log(err.stack);
});


module.exports = app;
