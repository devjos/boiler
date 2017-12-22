var express = require('express');
var router = express.Router();
var path = require('path');
var fs = require('fs');
var moment = require('moment');
var dateConverter = require('../public/lib/date/dateConverter.js');

var powerFile = "/var/log/boiler/power.log";
var temperatureFile = "/var/log/temperature/temp.log";
var fillLevelFile = "/var/log/boiler/fillLevel.log";

router.get('/', function(req, res, next) {

  var now = moment('2015-01-01');
  var currentMoment = moment(now).date(1);

  var expense = 0;
  while(currentMoment.isSameOrBefore(now, 'days') ){

    getPowerLogForMoment(currentMoment, function(powerLog){
      console.log(powerLog);

      for ( var i=0; i < powerLog.length; i++){
        if ( powerLog[i].power <= 0 ) continue;

        var startTime = powerLog[i].moment;
        var endTime;
        if ( i + 1 == powerLog.length ){
          var now = moment();
          if ( now.isSame(startTime, 'day') ){
            endTime = now;
          }
          else throw new Error('todo use value from next day?');
        }
        else endTime = powerLog[i+1].moment;

        var kwh = diffSeconds * getKWforPower(powerLog[i].power) / ( 60 * 60 );
        var diffSeconds = endTime.diff(startTime, 'seconds');

        if ( isNight(startTime) ){
          if ( isNight(endTime) ){
            //start in night and ends in night
            expense += kwh * pricePerKWH_night;
          }
          else{
            console.log('TODO start time in night, endtime in day');
            console.log(startTime.format());
            console.log(endTime.format());
          }
        }
        else{
          if ( isNight(endTime) ){
            console.log('TODO handle start time in day, endtime in night');
            console.log(startTime.format());
            console.log(endTime.format());
          }
          else{
            //start in day and ends in day
            expense += kwh * pricePerKWH_day;
          }

        }

      }

      console.log('expenses:' + expense);
      res.sendStatus(200);

    })

    currentMoment.add(1, 'day');
  }

})

function getPowerLogForMoment(date, callback){
  var filePath = powerFile;

  var today = moment();
  if ( date.isBefore(today, 'day') ){
    filePath += '.' + date.format().substr(0, 10);
  }
  fs.readFile(filePath, 'utf8', function (err,data) {
    var content = data;
    if (err) {
      content = date + 'T00:00:00,000 GPIO HEAT_LEVEL=HEAT_POWER_0';
    }

    var powerLog = convertPowerLog(content);
    callback(powerLog);

  });
}

function convertPowerLog(powerLog){
  var power = [];
  var lines = powerLog.split('\n');
  for(var i = 0;i < lines.length;i++){
    var line = lines[i];
    var search = 'HEAT_LEVEL=HEAT_POWER_';
    var index = line.search(search);
    if ( index != -1){
      power.push({
        moment: moment(line.split(' ')[0]),
        power: parseFloat(line.substr(index + search.length, 1))
      });
    }
  }
  return power;
}

module.exports = router;
