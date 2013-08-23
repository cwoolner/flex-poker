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
    
    if ($rootScope.client === undefined) {
        $rootScope.client = ngstomp(new SockJS(rootUrl + 'application'));
    }

    $rootScope.client.connect("", "", function() {
        $rootScope.client.subscribe("/app/availabletournaments", function(message) {
            $scope.games = $.parseJSON(message.body);
        });
        $rootScope.client.subscribe("/topic/availabletournaments-updates", function(message) {
            $scope.games = $.parseJSON(message.body);
        });
        $rootScope.client.subscribe('/topic/chat/global/user', function(message) {
            var scrollHeight = $('.chat-display').prop('scrollHeight');
            $('.chat-display').prop('scrollTop', scrollHeight);
            $scope.chatDisplay += $.parseJSON(message.body) + '\n';
        });
        $rootScope.client.subscribe('/topic/chat/global/system', function(message) {
            var scrollHeight = $('.chat-display').prop('scrollHeight');
            $('.chat-display').prop('scrollTop', scrollHeight);
            $scope.chatDisplay += $.parseJSON(message.body) + '\n';
        });
        $rootScope.client.subscribe("/app/personalchatid", function(message) {
            $rootScope.client.subscribe('/topic/chat/personal/user/' + $.parseJSON(message.body), function(innerMessage) {
                alert('personal' + innerMessage.body);
            });
            $rootScope.client.subscribe('/topic/chat/personal/system/' + $.parseJSON(message.body), function(innerMessage) {
                alert('personal' + innerMessage.body);
            });
        });

    }, function() {}, '/');
    
    $scope.openCreateGameDialog = function() {
        $('#create-game-dialog').dialog({ width: 550 });
    }
    
    $scope.openJoinGameDialog = function(row) {
        $scope.joinGameId = $scope.games[row.rowIndex].name;
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
        $rootScope.client.send("/app/joingame", {}, $scope.joinGameId);
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

        $rootScope.client.send('/app/sendchatmessage', {}, JSON.stringify(globalMessage)); 
        $scope.chatMessage = '';
    };

}]);
