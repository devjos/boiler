app.registerCtrl('SettingsCtrl', SettingsCtrl);

function SettingsCtrl($http){
  var self = this;

  this.message = '';
  this.settings = {};
  this.keys = [];

  $http( {
     method: 'GET',
     url: '/settings'
  }).then(function success(res){
    self.settings = res.data;
    self.keys = Object.keys(self.settings);
  }, function error(err){
    self.message = 'could not load settings'
    console.log(err);
  })

  this.submit = function(){
    console.log(this.settings);

    $http.post('/settings', this.settings)
    .then(function success(){
      console.log('success');
      self.message = 'successfully saved';
    }, function error(err){
      console.log('error');
      console.log(err);
      self.message = 'error, could not save settings';
    });
  }

}
