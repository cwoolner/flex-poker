var flexpokerModule = angular.module('flexpoker', ['AngularStomp']);

flexpokerModule.controller('HelloController', ['$scope', function($scope) {
    $scope.greeting = {text: 'Hello'};
}]);

flexpokerModule.controller('TournamentRegisteringController', ['$scope', 'ngstomp', function($scope, ngstomp) {
    $scope.games = [];
    $scope.client = ngstomp(new SockJS(rootUrl + 'application'));
    $scope.client.connect("", "", function() {
        $scope.client.subscribe("/app/availabletournaments", function(message) {
            $scope.games = $.parseJSON(message.body);
        });
    }, function() {}, '/');
}]);

