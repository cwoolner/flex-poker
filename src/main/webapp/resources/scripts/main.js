var flexpokerModule = angular.module('flexpoker', []);

flexpokerModule.controller('HelloController', ['$scope', function($scope) {
    $scope.greeting = {text: 'Hello'};
}]);

flexpokerModule.controller('TournamentRegisteringController', ['$scope', function($scope) {
    $scope.games = [];
}]);
