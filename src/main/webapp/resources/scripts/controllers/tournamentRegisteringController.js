flexpokerModule.controller('TournamentRegisteringController', ['$scope', 'ngstomp', function($scope, ngstomp) {
    
    $('#create-game-dialog').hide();
    $('body').find('button, input[type=submit]').button();
    $scope.chatDisplay = '';
    
    $scope.games = [];
    $scope.gridOptions = { data: 'games' };
    $scope.client = ngstomp(new SockJS(rootUrl + 'application'));
    $scope.client.connect("", "", function(frame) {
        var queueSuffix = frame.headers['queue-suffix'];

        $scope.client.subscribe('/queue/errors' + queueSuffix, function(message) {
            alert("Error " + message.body);
        });

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
        $scope.client.subscribe("/app/personalchatid", function(message) {
            $scope.client.subscribe('/topic/chat/personal/user/' + $.parseJSON(message.body), function(innerMessage) {
                alert('personal' + innerMessage.body);
            });
            $scope.client.subscribe('/topic/chat/personal/system/' + $.parseJSON(message.body), function(innerMessage) {
                alert('personal' + innerMessage.body);
            });
        });

    }, function() {}, '/');
    
    $scope.openCreateGameDialog = function() {
        $('#create-game-dialog').dialog({ width: 550 });
    }
    
    $scope.submit = function() {
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
