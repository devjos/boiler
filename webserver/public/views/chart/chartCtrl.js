app.registerCtrl('ChartCtrl', ChartCtrl);

function ChartCtrl ($http, $q) {
  var self = this;

  this.message = '';
  this.options = [
    {name: 'this day',  value: 0 },
    {name: '3 days',    value: 2},
    {name: '7 days',    value: 6},
    {name: '30 days',   value: 29}
  ];

  var chartData = [];

  var chart = new CanvasJS.Chart("chartContainer",
  {
      zoomEnabled: true,
      animationEnabled: true,
      title:{
              text: "Temperature"
      },
      axisX :{
              labelAngle: -30,
              valueFormatString: "D.M HH:mm",
              gridThickness: 1
      },
      axisY :{
              includeZero:false
      },
      data: chartData
  });

  var showGraphs = function (graphs){
    graphs.forEach( function(graph){
      var dataSeries = {
        type: "line",
        color: getColor(graph.power)
      };

      dataSeries.dataPoints = graph.dataPoints;
      chartData.push(dataSeries); //fill with new data
    });
    chart.render();
  }

  var requestTempLog = function(date){
    return $http( {
       method: 'GET',
       url: '/log',
       params: { date: dateConverter.getDateString(date), logType: 'temperatures' }
     }
    );
  }

  var requestPowerLog = function(date){
    return $http( {
       method: 'GET',
       url: '/log',
       params: { date: dateConverter.getDateString(date), logType: 'power' }
    } );
  }

  this.update = function(){
    this.message = 'loading';
    chartData.splice(0, chartData.length); //empty array


    var currentDay = moment();
    var firstDay = moment(currentDay).subtract(this.period, 'day');
    var dayBefore = moment(currentDay).subtract(1, 'day');

    var powerLogPromise = requestPowerLog(currentDay)

    while ( currentDay.isSameOrAfter(firstDay) ){

      var tempLogPromise = requestTempLog(currentDay);
      var powerLogPromiseDayBefore = requestPowerLog(dayBefore);

      $q.all([tempLogPromise, powerLogPromise, powerLogPromiseDayBefore]).then(function(arrayOfResults) {
        self.message = '';

        var dataPoints = convertTemperatureLog(arrayOfResults[0].data, self.period);
        var powerLog = convertPowerLog(arrayOfResults[1].data);
        var powerLogYesterday = convertPowerLog(arrayOfResults[2].data);

        //add last power info from day before to current day
        powerLog.unshift(powerLogYesterday.pop());

        var graphs = createGraphs(dataPoints, powerLog);;

        showGraphs(graphs);

      }, function (reason) {
        self.message = 'error';
      });

      /*
       * for next iteration turn dates one day back
       */
       currentDay.subtract(1, 'day');
       dayBefore.subtract(1, 'day');
       powerLogPromise = powerLogPromiseDayBefore;

    }
  }
}

function createGraphs(dataPoints, powerLog){
  var graphs = [];
  var powerIndex = 0;
  for ( var x = 0; x < dataPoints.length; x++ ){
    if ( powerIndex < powerLog.length && powerLog[powerIndex].date < dataPoints[x].x ){
      //rollover
      var power = -1;
      if ( powerIndex != 0 ){
        power = powerLog[powerIndex - 1].power;
      }
      graphs.push({
        power: power,
        dataPoints: dataPoints.splice(0, x)
      })
      x=0;
      powerIndex++
    }
  }

  graphs.push({
    power: powerLog[powerLog.length - 1].power,
    dataPoints: dataPoints
  })
  return graphs;
}

function getColor(power){
  switch(power){
    case -1: return 'black';
    case 0: return 'blue';
    case 1: return '#ffff00';
    case 2: return '#ff6600';
    case 3: return '#ff0000';
  }
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
        date: dateConverter.convertLogTime(line.split(' ')[0]),
        power: parseFloat(line.substr(index + search.length, 1))
      });
    }
  }
  return power;
}

function convertTemperatureLog(temperaturelog, period){
  var dataPoints = [];
  var lines = temperaturelog.split('\n');
  //dont use last line, its empty
  if ( period == 0 ) period++;
  for(var i = 0;i < lines.length - 1;i++){
    //nehme weniger werte für große zeiträume
    if ( i % period != 0 ) continue;
    var line = lines[i];
    var values = line.split(' ');
    dataPoints.push({
      x: dateConverter.convertLogTime(values[0]),
      y: parseFloat(values[1])
    });
  }
  return dataPoints;
}
