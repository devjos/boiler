var express = require('express');
var router = express.Router();
var path = require('path');
var fs = require('fs');
var properties = require ("properties");

var settingsFile = "/etc/boiler/boiler.config.in";

var settingKeys = ['TARGET_TEMPERATURE', 'LEGIONELLEN_TEMPERATURE', 'EMPTY_TEMPERATURE']

//send the settings
router.get('/', function(req, res, next) {

  properties.parse (settingsFile, { path: true }, function (error, obj){
    if (error) {
      res.status(500).send('Cannot get settings');
      return console.error (error);
    }

    res.send(obj);
  });
})

router.post('/', restrict, function(req, res, next) {
  var settings = req.body;

  var isValid = true;
  settingKeys.forEach( function (key){
    var temp = settings[key];
    if ( ! isNumber(temp) || temp < 1 || temp > 99 ){
      isValid = false;
    }
  })

  if ( isValid ){
    //save back to file
    properties.stringify(settings, {path: settingsFile}, function(error, string){
      if ( error ){
        res.sendStatus(400);
        return console.error (error);
      }
      res.sendStatus(200);
    });

  }
  else res.sendStatus(400);

})

function isNumber(obj) { return !isNaN(parseFloat(obj)) }

function restrict(req, res, next) {
  if (req.session.user) {
    next();
  } else {
    req.session.error = 'Access denied!';
    res.sendStatus(403);
  }
}

module.exports = router;
