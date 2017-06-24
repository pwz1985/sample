angular.module('app').controller('MainController', ['$scope', '$state', function($scope, $state) {
  $scope.init = function() {
    $scope.welcome = '欢迎';
    $scope.username = 'XXXX'
  }
}]);
