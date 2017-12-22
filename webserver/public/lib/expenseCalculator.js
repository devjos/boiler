var pricePerKWH_night = 0.185283; //18,5283 cent inkl. mwst
var pricePerKWH_day = 0.216342; //21,6342 cent inkl. mwst

var expenseCalculator = {
  calculateExpense: function(startTime, endTime, power){

    var diffSeconds = endTime.diff(startTime, 'seconds');
    var kwh = diffSeconds * getKWforPower(power) / ( 60 * 60 );

    var expense;

    if ( isNight(startTime) ){
      if ( isNight(endTime) ){
        //start in night and ends in night
        expense = kwh * pricePerKWH_night;
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
        expense = kwh * pricePerKWH_day;
      }
    }

    return expense;

  }

}

function isNight(moment){
  return moment.hour() >= 22 || moment.hour <= 5;
}

function getKWforPower(power){
  return power * 2;
}
