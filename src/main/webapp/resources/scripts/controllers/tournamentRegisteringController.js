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

    if (stompClient.connected) {
        registerStompSubscriptions();
    }

    $scope.$on('stomp-connected', function(event, data) {
        registerStompSubscriptions();
    });

    function registerStompSubscriptions() {
        stompClient.subscribe("/topic/availabletournaments", function(message) {
            $scope.$apply(function() {
                $scope.games = $.parseJSON(message.body);
            });
        });

        stompClient.subscribe('/topic/chat/global/user', function(message) {
            var scrollHeight = $('.chat-display').prop('scrollHeight');
            $('.chat-display').prop('scrollTop', scrollHeight);
            $scope.$apply(function() {
                $scope.chatDisplay += message.body + '\n';
            });
        });

        stompClient.subscribe('/topic/chat/global/system', function(message) {
            var scrollHeight = $('.chat-display').prop('scrollHeight');
            $('.chat-display').prop('scrollTop', scrollHeight);
            $scope.$apply(function() {
                $scope.chatDisplay += message.body + '\n';
            });
        });
    };
    
    $scope.openCreateGameDialog = function() {
        $('#create-game-dialog').dialog({ width: 550 });
    }
    
    $scope.openJoinGameDialog = function(row) {
        var gameId = $scope.games[row.rowIndex].id;
        $scope.joinGameId = gameId;
        $rootScope.tryingToJoinGameId = gameId; 
        $('#join-game-dialog').dialog({ width: 550 });
    }
    
    $scope.submitCreateGame = function() {
        var newGame = {
                name: $scope.name,
                players: $scope.players,
                playersPerTable: $scope.playersPerTable
        };
        stompClient.send("/app/creategame", {}, JSON.stringify(newGame));
        $scope.name = '';
        $scope.players = '';
        $scope.playersPerTable = '';
        $('#create-game-dialog').dialog('destroy');
    };
    
    $scope.submitJoinGame = function() {
        stompClient.send("/app/joingame", {}, JSON.stringify($scope.joinGameId));
        $('#join-game-dialog').dialog('destroy');
    };
    
    $scope.sendChat = function() {
        if ($scope.chatMessage == '') {
            return;
        }

        var globalMessage = {
                message: $scope.chatMessage,
                receiverUsernames: null,
                gameId: null,
                tableId: null
        };

        stompClient.send('/app/sendchatmessage', {}, JSON.stringify(globalMessage));
        $scope.chatMessage = '';
    };

}]);
