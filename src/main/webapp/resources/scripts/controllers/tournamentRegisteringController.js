import flexpokerModule from '../main';
import webSocketService from '../webSocketService';

flexpokerModule.controller('TournamentRegisteringController', ['$rootScope', '$scope', function($rootScope, $scope) {
    
    $('#create-game-dialog, #join-game-dialog').hide();
    $('body').find('button, input[type=submit]').button();
    $scope.chatDisplay = '';

    webSocketService.registerSubscription('/topic/availabletournaments', function(message) {
        document.querySelector('fp-gamelist').displayGames($.parseJSON(message.body));
    });

    $scope.openCreateGameDialog = function() {
        document.querySelector('fp-create-game-dialog').showDialog();
    };

    $scope.openJoinGameDialog = function(gameId) {
        $scope.joinGameId = gameId;
        $rootScope.tryingToJoinGameId = gameId;
        $('#join-game-dialog').dialog({ width: 550 });
    };

    $scope.submitJoinGame = function() {
        webSocketService.send('/app/joingame', $scope.joinGameId);
        $('#join-game-dialog').dialog('destroy');
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

}]);
