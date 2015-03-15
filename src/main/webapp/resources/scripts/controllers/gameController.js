import flexpokerModule from '../main';
import webSocketService from '../webSocketService';

flexpokerModule.controller('GameController', ['$scope', '$routeParams', function($scope, $routeParams) {
    $scope.gameId = $routeParams['gameId'];

    $scope.chatDisplay = '';

    $scope.sendChat = function() {
        if ($scope.chatMessage == '') {
            return;
        }

        var gameMessage = {
            message: $scope.chatMessage,
            receiverUsernames: null,
            gameId: $scope.gameId,
            tableId: null
        };

        webSocketService.send('/app/sendchatmessage', gameMessage);
        $scope.chatMessage = '';
    };

    webSocketService.registerSubscription('/topic/chat/game/' + $scope.gameId + '/user', receiveChat);
    webSocketService.registerSubscription('/topic/chat/game/' + $scope.gameId + '/system', receiveChat);

    function receiveChat(message) {
        var scrollHeight = $('.chat-display').prop('scrollHeight');
        $('.chat-display').prop('scrollTop', scrollHeight);
        $scope.$apply(function() {
            $scope.chatDisplay += message.body + '\n';
        });
    }

}]);
