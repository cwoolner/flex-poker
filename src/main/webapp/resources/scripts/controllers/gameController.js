import flexpokerModule from '../main';
import webSocketService from '../webSocketService';
import chat from '../chat/chat';

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

    chat.registerChat(`/topic/chat/game/${$scope.gameId}/user`);
    chat.registerChat(`/topic/chat/game/${$scope.gameId}/system`);

}]);
