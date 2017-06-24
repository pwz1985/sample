angular.module('app').config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {

  $stateProvider
    .state('login', {
      url: '/login',
      templateUrl: 'templates/login.html'
    })
    .state('main', {
      url: '/main',
      templateUrl: 'templates/main.html'
    })
  $urlRouterProvider.otherwise('/login');
}]);
