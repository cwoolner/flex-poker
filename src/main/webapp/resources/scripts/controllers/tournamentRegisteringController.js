import flexpokerModule from '../main';
import webSocketService from '../webSocketService';

flexpokerModule.controller('TournamentRegisteringController', ['$rootScope', '$scope', function($rootScope, $scope) {
    
    $('body').find('button, input[type=submit]').button();
    $scope.chatDisplay = '';

    webSocketService.registerSubscription('/topic/availabletournaments', function(message) {
        document.querySelector('fp-gamelist').displayGames($.parseJSON(message.body));
    });

    $scope.openCreateGameDialog = function() {
        document.querySelector('fp-create-game-dialog').showDialog();
    };

    $scope.openJoinGameDialog = function(gameId) {
        $rootScope.tryingToJoinGameId = gameId;
        let joinGameDialog = document.querySelector('fp-join-game-dialog');
        joinGameDialog.showDialog(gameId);
    };

    $scope.sendChat = function(message) {
        var globalMessage = {
            message: message,
            receiverUsernames: null,
            gameId: null,
            tableId: null
        };

        webSocketService.send('/app/sendchatmessage', globalMessage);
    };

    document.querySelector('.global-chat').addEventListener('chat-msg-entered', function(evt) {
        $scope.sendChat(evt.detail);
    });

    document.querySelector('fp-gamelist').addEventListener('game-open-selected', function(evt) {
        $scope.openJoinGameDialog(evt.detail);
    });

    document.querySelector('fp-create-game-dialog').addEventListener('create-game-submitted', function(evt) {
        webSocketService.send('/app/creategame', evt.detail);
    });

    document.querySelector('fp-join-game-dialog').addEventListener('join-game-submitted', function(evt) {
        webSocketService.send('/app/joingame', evt.detail);
    });

}]);
