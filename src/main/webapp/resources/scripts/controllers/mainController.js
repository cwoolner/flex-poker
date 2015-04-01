import flexpokerModule from '../main';
import webSocketService from '../webSocketService'

flexpokerModule.controller('MainController', ['$rootScope', '$scope', '$location', '$templateCache', function($rootScope, $scope, $location, $templateCache) {

    $rootScope.username = document.querySelector('.username').innerHTML;

    webSocketService.registerSubscription('/user/queue/errors', function(message) {
        alert("Error " + message.body);
        window.tryingToJoinGameId = null;
    });

    webSocketService.registerSubscription('/app/opengamesforuser', function(message) {
        $scope.$apply(function() {
            $scope.gameTabs = JSON.parse(message.body);
        });
    });

    webSocketService.registerSubscription('/user/queue/opengamesforuser', function(message) {
        $scope.$apply(function() {
            $scope.gameTabs = JSON.parse(message.body);
        });
        if (window.tryingToJoinGameId != null) {
            $scope.$apply(function() {
                $location.path(`/game/${window.tryingToJoinGameId}`);
            });
            window.tryingToJoinGameId = null;
        }
    });

    webSocketService.registerSubscription('/user/queue/opentable', function(message) {
        var openTable = JSON.parse(message.body);
        $scope.$apply(function() {
            $location.path(`/game/${openTable.gameId}/table/${openTable.tableId}`);
        });
    });

    webSocketService.registerSubscription('/user/queue/personaltablestatus', function(message) {
        alert(JSON.parse(message.body));
    });

    webSocketService.registerSubscription('/user/queue/pocketcards', function(message) {
        let parsedData = JSON.parse(message.body);
        let pocketCards = {
            cardId1: parsedData.cardId1,
            cardId2: parsedData.cardId2
        };
        $rootScope.$broadcast(`pocketCardsReceived-${parsedData.tableId}`, pocketCards);
    });

}]);
