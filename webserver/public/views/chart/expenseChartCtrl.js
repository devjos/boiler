app.registerCtrl('ExpenseChartCtrl', ExpenseChartCtrl);

var DailyExpenseController = function(period) {
  this.dateFormat = 'DD.MM';
  this.expenses = [];
  this.period = period;
  this.notBeforeDate = moment().subtract(period, 'days');
  this.formatDate = function(date){
    return dateConverter.getDateString(date);
  }
  this.addExpense = function (date, expense){
    if ( date.isSameOrAfter(this.notBeforeDate) ){
      this.expenses.push({
        x: new Date(dateConverter.getDateString(date)),
        y: expense,
        color: getColorForExpenseDay(expense)
      })
    }
  }
  function getColorForExpenseDay(expense){
    if ( expense < 1.5 ) return '#00ff00'; //green
    if ( expense < 2 ) return '#ffff00'; //yellow
    if ( expense < 2.5 ) return '#ff8000'; //orange
    else return '#ff0000'; //red
  }
};


function MonthlyExpenseController(){
  this.dateFormat = 'MMM YYYY';
  this.expenses = [];
  this.formatDate = function(date){
    return dateConverter.getMonthString(date);
  }
  this.addExpense = function (date, expense){
    var id = new Date(date.year(), date.month());
    var el = getElement(this.expenses, id);
    if ( el == null ){
      this.expenses.push({
        x: id,
        y: expense,
        color: getColorForExpenseMonth(expense)
      })
    }
    else{
      el.y += expense;
      el.color = getColorForExpenseMonth(el.y);
    }
  }
  function getElement(data, key) {
      var found = null;
      for (var i = 0; i < data.length; i++) {
          var element = data[i];
          if (element.x.getTime() == key.getTime()) {
             found = element;
         }
      }
      return found;
  }
  function getColorForExpenseMonth(expense){
    if ( expense < 50 ) return '#00ff00'; //green
    if ( expense < 55 ) return '#ffff00'; //yellow
    if ( expense < 60 ) return '#ff8000'; //orange
    else return '#ff0000'; //red
  }
}

function ExpenseChartCtrl ($http, $q) {
  var self = this;

  this.message = '';
  this.options = [
    {name: '30 days',   value: {unit: 'days', value:'31'}},
    {name: '60 days',   value: {unit: 'days', value:'61'}},
    {name: '90 days',   value: {unit: 'days', value:'91'}},
    {name: '6 months',   value: {unit: 'months', value:'6'}},
    {name: '1 year',   value: {unit: 'months', value:'12'}}
  ];

  this.update = function(){
    this.message = 'loading';

    var expenseController;
    if ( this.period.unit == 'months' ) expenseController = new MonthlyExpenseController();
    else if ( this.period.unit == 'days' ) expenseController = new DailyExpenseController(this.period.value);

    var today = moment();
    var requests = [];

    for ( var d = moment(today).subtract(this.period.value, this.period.unit); d.isSameOrBefore(today, 'month'); d.add(1, 'month') ){
      requests.push( $http( {
         method: 'GET',
         url: '/log',
         params: { date: expenseController.formatDate(d), logType: 'costs' }
      }) );
    }


    $q.all(requests).then( function(arrayOfResults){
      var notBeforeDate = moment(today).subtract(self.period.value, self.period.unit);
      self.message = '';
      var expenses = [];
      for ( var x=0; x < arrayOfResults.length; x++ ){

        var lines = arrayOfResults[x].data.split('\n');
        for(var i = 0;i < lines.length;i++){
          var line = lines[i];
          var splitted = line.split(' ');
          var date = moment(splitted[0]);
          var cost = parseFloat(splitted[1]);

          expenseController.addExpense(date, cost);

        }
      }

      var chartOptions = {
        zoomEnabled: false,
        animationEnabled: true,
        title:{ text: "Ausgaben" },
        axisX :{
                valueFormatString: expenseController.dateFormat
        },
        axisY :{
                valueFormatString: "##0 €"
        },
        data: [{
          type: "column",
          dataPoints: expenseController.expenses,
          yValueFormatString: "##0.00€",
        }]
      }

      var chart = new CanvasJS.Chart("chartContainer", chartOptions);
      chart.render();

    }, function (error){
      self.message = 'error';
    })
  }
}

function convertCostsLog(costsLog){

}
