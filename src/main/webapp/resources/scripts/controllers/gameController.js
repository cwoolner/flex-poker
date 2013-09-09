flexpokerModule.controller('GameController', ['$scope', '$rootScope', '$routeParams', 'ngstomp', function($scope, $rootScope, $routeParams, ngstomp) {
    $scope.gameId = $routeParams['gameId'];

    $scope.chatDisplay = '';
    
    if ($scope.client === undefined) {
        $scope.client = ngstomp(new SockJS(rootUrl + 'application'));
    }

    $scope.client.connect("", "", function() {
        
        $scope.client.subscribe('/topic/chat/game/' + $scope.gameId + '/user', function(message) {
            var scrollHeight = $('.chat-display').prop('scrollHeight');
            $('.chat-display').prop('scrollTop', scrollHeight);
            $scope.chatDisplay += $.parseJSON(message.body) + '\n';
        });
        $scope.client.subscribe('/topic/chat/game/' + $scope.gameId + '/system', function(message) {
            var scrollHeight = $('.chat-display').prop('scrollHeight');
            $('.chat-display').prop('scrollTop', scrollHeight);
            $scope.chatDisplay += $.parseJSON(message.body) + '\n';
        });
    }, function() {}, '/');
    
    if ($rootScope.stompClients === undefined) {
        $rootScope.stompClients = [];
    }
    $rootScope.stompClients.push($scope.client);

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

        $scope.client.send('/app/sendchatmessage', {}, JSON.stringify(gameMessage)); 
        $scope.chatMessage = '';
    };

}]);
