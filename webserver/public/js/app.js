var app=angular.module('temperatureApp',['ui.router']);

app.factory('Auth', function(){
  var user;

  return{
     setUser : function(aUser){
         user = aUser;
     },
     isLoggedIn : function(){
         return(user)? user : false;
     }
   }
})

app.config(function($stateProvider, $urlRouterProvider, $controllerProvider) {
        // remember mentioned function for later use
        app.registerCtrl = $controllerProvider.register;
        app.resolveScriptDeps = function(dependencies){
          return function($q,$rootScope){
            var deferred = $q.defer();
            $script(dependencies, function() {
              // all dependencies have now been loaded by $script.js so resolve the promise
              $rootScope.$apply(function()
              {
                deferred.resolve();
              });
            });

            return deferred.promise;
          }
        };

        //routes
        $urlRouterProvider.otherwise('/');
        $stateProvider
          .state('home', {
            url: '/',
            templateUrl: 'views/chart/chartContainer.html',
            controller: 'FillLevelChartCtrl as chart',
            resolve: {deps: app.resolveScriptDeps(['views/chart/fillLevelChartCtrl.js'])}
          })

          .state('login', {
            url: '/login',
            templateUrl: 'views/login/login.html',
            controller: 'LoginController as login',
            resolve: {deps: app.resolveScriptDeps(['views/login/loginController.js'])}
          })

          .state('logout', {
            url: '/logout',
            template: 'logging out',
            controller: 'LogoutController as logout',
            resolve: {deps: app.resolveScriptDeps(['views/login/logoutController.js'])}
          })

          .state('temperatures', {
            url: '/temperatures',
            templateUrl: 'views/chart/chartContainer.html',
            controller: 'ChartCtrl as chart',
            resolve: {deps: app.resolveScriptDeps(['views/chart/chartCtrl.js'])}
          })

          .state('expenses', {
            url: '/expenses',
            templateUrl: 'views/chart/chartContainer.html',
            controller: 'ExpenseChartCtrl as chart',
            resolve: {deps: app.resolveScriptDeps(['views/chart/expenseChartCtrl.js'])}
          })

          .state('settings', {
            url: '/settings',
            templateUrl: 'views/settings/settings.html',
            controller: 'SettingsCtrl as settings',
            resolve: {deps: app.resolveScriptDeps(['views/settings/settingsCtrl.js'])}
          })
    }
);

app.controller('NavBarCtrl', NavBarCtrl);

function NavBarCtrl ($rootScope) {
  var self = this;

  this.menus = [
    { icon: 'home', name: 'Home', link: 'home'},
    { icon: 'stats', name: 'Temperaturen', link: 'temperatures'},
    { icon: 'euro', name: 'Ausgaben', link: 'expenses'},
    { icon: 'wrench', name: 'Einstellungen', link: 'settings'}
  ]

  $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams){
    self.state = toState;
  });

}
