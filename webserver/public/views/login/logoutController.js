app.registerCtrl('LogoutController', LogoutController);

function LogoutController($http, $rootScope, $state){
  $rootScope.isLoggedIn = false;
  $http.get('/logout')
  .then(function success(res){

  }, function error(err){
    console.log(err);
  })
  $state.go('login');

}
