import flexpokerModule from '../main';
import webSocketService from '../webSocketService';

flexpokerModule.controller('TournamentRegisteringController', ['$rootScope', '$scope', function($rootScope, $scope) {
    
    $('#create-game-dialog, #join-game-dialog').hide();
    $('body').find('button, input[type=submit]').button();
    $scope.chatDisplay = '';

    $scope.games = [];
    $scope.gridOptions = {
            data: 'games',
            rowTemplate:
                '<div style="height: 100%">'
                  + '<div ng-style="{ \'cursor\': row.cursor }" ng-repeat="col in renderedColumns" ng-class="col.colIndex()" class="ngCell" ng-dblclick="openJoinGameDialog(row)">'
                  +     '<div class="ngVerticalBar" ng-style="{height: rowHeight}" ng-class="{ ngVerticalBarVisible: !$last }"></div>'
                  +     '<div ng-cell></div>'
                  + '</div>'
              + '</div>',
            columnDefs: [ {field: 'name', displayName: 'Name'},
                          {field: 'stage', displayName: 'Stage'},
                          {field: 'numberOfRegisteredPlayers', displayName: 'Registered Players'},
                          {field: 'maxNumberOfPlayers', displayName: 'Total Players'},
                          {field: 'maxPlayersPerTable', displayName: 'Players Per Table'},
                          {field: 'createdBy', displayName: 'Creator'}, 
                          {field: 'createdOn', displayName: 'Created'}]
        };

    webSocketService.registerSubscription('/topic/availabletournaments', function(message) {
        $scope.$apply(function() {
            $scope.games = $.parseJSON(message.body);
        });
    });

    $scope.openCreateGameDialog = function() {
        $('#create-game-dialog').dialog({ width: 550 });
    };

    $scope.openJoinGameDialog = function(row) {
        var gameId = $scope.games[row.rowIndex].id;
        $scope.joinGameId = gameId;
        $rootScope.tryingToJoinGameId = gameId;
        $('#join-game-dialog').dialog({ width: 550 });
    };

    $scope.submitCreateGame = function() {
        var newGame = {
            name: $scope.name,
            players: $scope.players,
            playersPerTable: $scope.playersPerTable
        };
        webSocketService.send('/app/creategame', newGame);
        $scope.name = '';
        $scope.players = '';
        $scope.playersPerTable = '';
        $('#create-game-dialog').dialog('destroy');
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

}]);
