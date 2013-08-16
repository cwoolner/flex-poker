flexpokerModule.controller('GameController', ['$scope', '$routeParams', function($scope, $routeParams) {
    $scope.gameId = $routeParams['gameId'];
}]);
