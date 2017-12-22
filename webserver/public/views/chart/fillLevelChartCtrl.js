app.registerCtrl('FillLevelChartCtrl', FillLevelChartCtrl);

function FillLevelChartCtrl ($http, $q) {
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
              text: "Füllstand"
      },
      axisX :{
              labelAngle: -30,
              valueFormatString: "D.M HH:mm",
              gridThickness: 1
      },
      axisY :{
              includeZero:true
      },
      data: chartData
  });

  var showGraphs = function (graphs){
    graphs.forEach( function(graph){
      var dataSeries = {
        type: "line",
        dataPoints: graph,

        color: 'blue'
      };

      chartData.push(dataSeries); //fill with new data
    });
    chart.render();
  }

  this.update = function(){
    this.message = 'loading';
    chartData.splice(0, chartData.length); //empty array

    var d = moment();
    for ( var i=0; i <= this.period; i++ ){

      $http( {
         method: 'GET',
         url: '/log',
         params: { date: dateConverter.getDateString(d), logType: 'fill-level' }
      }).then( function(fillLevel){
        self.message = '';

        var dataPoints = convertFillLevelLog(fillLevel.data);
        showGraphs([dataPoints]);

      }, function (error){
        self.message = 'error';
      })

      d.subtract(1, 'day');
    }

  }
}

function convertFillLevelLog(fillLevelLog){
  var dataPoints = [];
  var lines = fillLevelLog.split('\n');
  //dont use last line, its empty
  for(var i = 0; i < lines.length - 1;i++){
    var line = lines[i];
    var values = line.split(' ');
    var level = values[1].split('=')[1];
    dataPoints.push({
      x: dateConverter.convertLogTime(values[0]),
      y: parseFloat(level)
    });
  }
  return dataPoints;
}
