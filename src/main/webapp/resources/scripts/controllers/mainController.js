import flexpokerModule from '../main';
import webSocketService from '../webSocketService'

flexpokerModule.controller('MainController', ['$rootScope', '$scope', '$location', '$templateCache', function($rootScope, $scope, $location, $templateCache) {

    $rootScope.username = $('.username').prop('innerText');

    webSocketService.registerSubscription('/user/queue/errors', function(message) {
        alert("Error " + message.body);
        $rootScope.tryingToJoinGameId = null;
    });

    webSocketService.registerSubscription('/user/topic/chat/personal/user', function(message) {
        alert('personal' + message.body);
    });

    webSocketService.registerSubscription('/user/topic/chat/personal/system', function(message) {
        alert('personal' + message.body);
    });

    webSocketService.registerSubscription('/app/opengamesforuser', function(message) {
        $scope.$apply(function() {
            $scope.gameTabs = $.parseJSON(message.body);
        });
    });

    webSocketService.registerSubscription('/user/queue/opengamesforuser', function(message) {
        $scope.$apply(function() {
            $scope.gameTabs = $.parseJSON(message.body);
        });
        if ($rootScope.tryingToJoinGameId != null) {
            $scope.$apply(function() {
                $location.path(`/game/${$rootScope.tryingToJoinGameId}`);
            });
            $rootScope.tryingToJoinGameId = null;
        }
    });

    webSocketService.registerSubscription('/user/queue/opentable', function(message) {
        var openTable = $.parseJSON(message.body);
        $scope.$apply(function() {
            $location.path(`/game/${openTable.gameId}/table/${openTable.tableId}`);
        });
    });

    webSocketService.registerSubscription('/user/queue/personaltablestatus', function(message) {
        alert($.parseJSON(message.body));
    });

    webSocketService.registerSubscription('/user/queue/pocketcards', function(message) {
        let parsedData = $.parseJSON(message.body);
        let pocketCards = {
            cardId1: parsedData.cardId1,
            cardId2: parsedData.cardId2
        };
        $rootScope.$broadcast(`pocketCardsReceived-${parsedData.tableId}`, pocketCards);
    });

}]);
