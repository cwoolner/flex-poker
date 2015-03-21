import flexpokerModule from '../main';
import webSocketService from '../webSocketService';
import chat from '../chat/chat';

flexpokerModule.controller('GameController', ['$scope', '$routeParams', function($scope, $routeParams) {
    $scope.gameId = $routeParams['gameId'];

    $scope.chatDisplay = '';

    $scope.sendChat = function(message) {
        var gameMessage = {
            message: message,
            receiverUsernames: null,
            gameId: $scope.gameId,
            tableId: null
        };

        webSocketService.send('/app/sendchatmessage', gameMessage);
    };

    chat.registerChat(`/topic/chat/game/${$scope.gameId}/user`);
    chat.registerChat(`/topic/chat/game/${$scope.gameId}/system`);

    document.querySelector('.game-chat').addEventListener('chat-msg-entered', function(evt) {
        $scope.sendChat(evt.detail);
    });

}]);
