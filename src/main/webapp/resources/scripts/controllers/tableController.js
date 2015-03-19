import flexpokerModule from '../main';
import webSocketService from '../webSocketService'
import cardData from '../cardData';
import chat from '../chat/chat';

flexpokerModule.controller('TableController', ['$scope', '$rootScope', '$routeParams', function($scope, $rootScope, $routeParams) {
    $scope.gameId = $routeParams['gameId'];
    $scope.tableId = $routeParams['tableId'];
    $scope.username = $rootScope.username;

    $scope.chatDisplay = '';

    $rootScope.$on(`pocketCardsReceived-${$scope.tableId}`, function(event, data) {
        $scope.$apply(function() {
            $scope.myLeftCardUrl = cardData[data.cardId1];
            $scope.myRightCardUrl = cardData[data.cardId2];
        });
    });

    $scope.sendChat = function() {
        if ($scope.chatMessage == '') {
            return;
        }

        var tableMessage = {
            message: $scope.chatMessage,
            receiverUsernames: null,
            gameId: $scope.gameId,
            tableId: $scope.tableId
        };

        webSocketService.send('/app/sendchatmessage', tableMessage);
        $scope.chatMessage = '';
    };

    $scope.check = function() {
        var checkMessage = {
            gameId: $scope.gameId,
            tableId: $scope.tableId
        };
        webSocketService.send('/app/check', checkMessage);
    };

    $scope.call = function() {
        var callMessage = {
            gameId: $scope.gameId,
            tableId: $scope.tableId
        };
        webSocketService.send('/app/call', callMessage);
    };

    $scope.raise = function() {
        var raiseMessage = {
            gameId: $scope.gameId,
            tableId: $scope.tableId
        };
        webSocketService.send('/app/raise', raiseMessage);
    };

    $scope.fold = function() {
        var foldMessage = {
            gameId: $scope.gameId,
            tableId: $scope.tableId
        };
        webSocketService.send('/app/fold', foldMessage);
    };

    chat.registerChat(`/topic/chat/game/${$scope.gameId}/table/${$scope.tableId}/user`);
    chat.registerChat(`/topic/chat/game/${$scope.gameId}/table/${$scope.tableId}/system`);

    webSocketService.registerSubscription(`/topic/game/${$scope.gameId}/table/${$scope.tableId}`, receiveTableUpdate);

    function receiveTableUpdate(message) {
        let table = $.parseJSON(message.body);
        $scope.$apply(function() {
            $scope.table = table;
        });
        let mySeat = $scope.table.seats.find((element, index, array) => {return element.name === $scope.username;})
        if (mySeat) {
            var pokerActions = {
                actionOn: mySeat.actionOn,
                check: mySeat.callAmount === 0,
                fold: mySeat.callAmount !== 0,
                call: mySeat.callAmount !== 0,
                raise: mySeat.raiseTo !== 0
            };
            $scope.$apply(function() {
                $scope.pokerActions = pokerActions;
            });
        }

        $scope.$apply(function() {
            $scope.commonCards = [];
            $scope.table.visibleCommonCards.forEach(function(commonCard) {
                $scope.commonCards.push(cardData[commonCard.id]);
            });
        });
    }

}]);
