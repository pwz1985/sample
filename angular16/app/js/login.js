angular.module('app').controller('LoginController', ['$scope', '$state', function($scope, $state) {

  $scope.init = function() {
    $scope.username = 'username';
    $scope.password = 'password';
  }

  $scope.login = function() {
    $state.go('main');
  }

}]);
