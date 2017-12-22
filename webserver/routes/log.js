var express = require('express');
var router = express.Router();
var path = require('path');
var fs = require('fs');
var moment = require('moment');

var powerFile = "/var/log/boiler/power.log";
var temperatureFile = "/var/log/temperature/temp.log";
var fillLevelFile = "/var/log/boiler/fillLevel.log";
var costsFile = "/var/log/boiler/costs.log";

var DailyController = function(path) {
  this.path = path;
  this.getFullPath = function (requestDate){
    var today = moment();
    var filePath = this.path;
    if ( requestDate.isBefore(today, 'day') ){
      filePath += '.' + requestDate.format().substr(0, 10);
    }
    return filePath;
  }
};

var MonthlyController = function(path) {
  this.path = path;
  this.getFullPath = function (requestDate){
    var today = moment();
    var filePath = this.path;
    if ( requestDate.isBefore(today, 'month') ){
      filePath += '.' + requestDate.format().substr(0, 7);
    }
    return filePath;
  }
};

router.get('/', function(req, res, next) {
  var requestDate = moment(req.query.date);
  var logType = req.query.logType;

  var pathController;
  var filePath;
  if ( logType == 'temperatures' ){
    pathController = new DailyController(temperatureFile);
  } else if ( logType == 'power' ){
    pathController = new DailyController(powerFile);
  } else if ( logType == 'fill-level' ){
    pathController = new DailyController(fillLevelFile);
  } else if ( logType == 'costs' ){
    pathController = new MonthlyController(costsFile);
  }
  else{
    console.log('unknown logType:' + logType);
    res.sendStatus(404);
    return;
  }

  var filePath = pathController.getFullPath(requestDate);

  fs.stat(filePath, function(err, stats){
    if ( err || !stats.isFile() ){
      if ( logType == 'power' ){
        res.send(req.query.date + 'T00:00:00,000 GPIO HEAT_LEVEL=HEAT_POWER_0');
        return;
      } else if(logType == 'costs'){
        res.send(req.query.date + '0.00');
        return;
      } else{
        console.log('error or no file');
        console.log(err);
        res.sendStatus(404);
        return;
      }

    }

    res.writeHead(200, {
          'Content-Type': 'text/plain',
          'Content-Length': stats.size
    });

    fs.createReadStream(filePath).pipe(res);
  })
})

module.exports = router;
