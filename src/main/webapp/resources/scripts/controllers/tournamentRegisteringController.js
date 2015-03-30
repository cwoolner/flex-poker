import flexpokerModule from '../main';
import webSocketService from '../webSocketService';

flexpokerModule.controller('TournamentRegisteringController', [function() {

    webSocketService.registerSubscription('/topic/availabletournaments', function(message) {
        document.querySelector('fp-gamelist').displayGames($.parseJSON(message.body));
    });

    document.querySelector('#create-game-button').addEventListener('click', function(evt) {
        document.querySelector('fp-create-game-dialog').showDialog();
    });

    document.querySelector('.global-chat').addEventListener('chat-msg-entered', function(evt) {
        var globalMessage = {
            message: evt.detail,
            receiverUsernames: null,
            gameId: null,
            tableId: null
        };

        webSocketService.send('/app/sendchatmessage', globalMessage);
    });

    document.querySelector('fp-gamelist').addEventListener('game-open-selected', function(evt) {
        window.tryingToJoinGameId = evt.detail;
        let joinGameDialog = document.querySelector('fp-join-game-dialog');
        joinGameDialog.showDialog(evt.detail);
    });

    document.querySelector('fp-create-game-dialog').addEventListener('create-game-submitted', function(evt) {
        webSocketService.send('/app/creategame', evt.detail);
    });

    document.querySelector('fp-join-game-dialog').addEventListener('join-game-submitted', function(evt) {
        webSocketService.send('/app/joingame', evt.detail);
    });

}]);
