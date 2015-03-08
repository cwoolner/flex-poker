flexpokerModule.controller('GameController', ['$scope', '$routeParams', function($scope, $routeParams) {
    $scope.gameId = $routeParams['gameId'];

    $scope.chatDisplay = '';
    
    if (stompClient.connected) {
        registerStompSubscriptions();
    }

    $scope.$on('stomp-connected', function(event, data) {
        registerStompSubscriptions();
    });

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

        stompClient.send('/app/sendchatmessage', {}, JSON.stringify(gameMessage));
        $scope.chatMessage = '';
    };

    function registerStompSubscriptions() {
        stompClient.subscribe('/topic/chat/game/' + $scope.gameId + '/user', function(message) {
            var scrollHeight = $('.chat-display').prop('scrollHeight');
            $('.chat-display').prop('scrollTop', scrollHeight);
            $scope.$apply(function() {
                $scope.chatDisplay += message.body + '\n';
            });
        });

        stompClient.subscribe('/topic/chat/game/' + $scope.gameId + '/system', function(message) {
            var scrollHeight = $('.chat-display').prop('scrollHeight');
            $('.chat-display').prop('scrollTop', scrollHeight);
            $scope.$apply(function() {
                $scope.chatDisplay += message.body + '\n';
            });
        });
    }

}]);
