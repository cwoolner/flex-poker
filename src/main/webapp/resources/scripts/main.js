var flexpokerModule = angular.module('flexpoker', ['AngularStomp', 'ngGrid']);

flexpokerModule.controller('HelloController', ['$scope', function($scope) {
    $scope.greeting = {text: 'Hello, World'};
}]);

flexpokerModule.controller('TournamentRegisteringController', ['$scope', 'ngstomp', function($scope, ngstomp) {
    $scope.games = [];
    $scope.gridOptions = { data: 'games' };
    $scope.client = ngstomp(new SockJS(rootUrl + 'application'));
    $scope.client.connect("", "", function() {
        $scope.client.subscribe("/app/availabletournaments", function(message) {
            $scope.games = $.parseJSON(message.body);
        });
        $scope.client.subscribe("/topic/availabletournaments-updates", function(message) {
            $scope.games = $.parseJSON(message.body);
        });
    }, function() {}, '/');
}]);

