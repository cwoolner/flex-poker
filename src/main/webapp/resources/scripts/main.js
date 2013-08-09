var flexpokerModule = angular.module('flexpoker', []);

flexpokerModule.controller('HelloController', ['$scope', function($scope) {
    $scope.greeting = {text: 'Hello'};
}]);

flexpokerModule.controller('TournamentRegisteringController', ['$scope', 'StompConnection', function($scope, StompConnection) {
    StompConnection.connect();
//    stompClient.subscribe("/app/availabletournaments", function(message) {
//        var scope = angular.element($("#tournament-registering")).scope();
//        scope.$apply(function(){
//            scope.games = $.parseJSON(message.body);
//        })
//    });

    $scope.games = [];
}]);

