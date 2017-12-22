var dateConverter = {
  getDateString: function (date){
    return date.format().substr(0, 10);
  },

  getMonthString: function (date){
    return date.format().substr(0, 7);
  },

  convertLogTime: function (logtime){
    return new Date(moment(logtime).format());
  }

}
