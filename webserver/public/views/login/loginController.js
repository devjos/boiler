app.registerCtrl('LoginController', LoginController);

function LoginController($http, $rootScope, $state){
  var self = this;

  this.message = '';
  this.user = '';
  this.pw = '';

  this.login = function(){
    $http.post('/login', {user: self.user, pw: self.pw })
    .then(function success(res){
      $rootScope.isLoggedIn = true;
      $state.go('settings');
    }, function error(err){
      $rootScope.isAuthenticated = false;
      self.message = 'could not login'
      console.log(err);
    })
  }

}
