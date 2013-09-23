flexpokerModule.controller('TournamentRegisteringController', ['$rootScope', '$scope', 'ngstomp', function($rootScope, $scope, ngstomp) {
    
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
              + '</div>'
        };
    
    if ($scope.client === undefined) {
        $scope.client = ngstomp(new SockJS(rootUrl + 'application'));
    }

    $scope.client.connect("", "", function() {
        $scope.client.subscribe("/app/availabletournaments", function(message) {
            $scope.games = $.parseJSON(message.body);
        });
        $scope.client.subscribe("/topic/availabletournaments-updates", function(message) {
            $scope.games = $.parseJSON(message.body);
        });
        $scope.client.subscribe('/topic/chat/global/user', function(message) {
            var scrollHeight = $('.chat-display').prop('scrollHeight');
            $('.chat-display').prop('scrollTop', scrollHeight);
            $scope.chatDisplay += $.parseJSON(message.body) + '\n';
        });
        $scope.client.subscribe('/topic/chat/global/system', function(message) {
            var scrollHeight = $('.chat-display').prop('scrollHeight');
            $('.chat-display').prop('scrollTop', scrollHeight);
            $scope.chatDisplay += $.parseJSON(message.body) + '\n';
        });
    }, function() {}, '/');
    
    if ($rootScope.stompClients === undefined) {
        $rootScope.stompClients = [];
    }
    $rootScope.stompClients.push($scope.client);

    $scope.openCreateGameDialog = function() {
        $('#create-game-dialog').dialog({ width: 550 });
    }
    
    $scope.openJoinGameDialog = function(row) {
        var gameId = $scope.games[row.rowIndex].name;
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
        $scope.client.send("/app/creategame", {}, JSON.stringify(newGame));
        $scope.name = '';
        $scope.players = '';
        $scope.playersPerTable = '';
        $('#create-game-dialog').dialog('destroy');
    };
    
    $scope.submitJoinGame = function() {
        $scope.client.send("/app/joingame", {}, JSON.stringify($scope.joinGameId));
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

        $scope.client.send('/app/sendchatmessage', {}, JSON.stringify(globalMessage)); 
        $scope.chatMessage = '';
    };

}]);
