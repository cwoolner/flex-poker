flexpokerModule.controller('TableController', ['$scope', '$rootScope', '$routeParams', 'ngstomp', function($scope, $rootScope, $routeParams, ngstomp) {
    $scope.gameId = $routeParams['gameId'];
    $scope.tableId = $routeParams['tableId'];

    $scope.chatDisplay = '';
    
    if ($scope.client === undefined) {
        $scope.client = ngstomp(new SockJS(rootUrl + 'application'));
    }

    $scope.client.connect("", "", function() {
        
        $scope.client.subscribe('/topic/chat/game/' + $scope.gameId
                + '/table/' + $scope.tableId + '/user', function(message) {
            var scrollHeight = $('.chat-display').prop('scrollHeight');
            $('.chat-display').prop('scrollTop', scrollHeight);
            $scope.chatDisplay += $.parseJSON(message.body) + '\n';
        });
        $scope.client.subscribe('/topic/chat/game/' + $scope.gameId
                + '/table/' + $scope.tableId + '/system', function(message) {
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

        var tableMessage = {
                message: $scope.chatMessage,
                receiverUsernames: null,
                gameId: $scope.gameId,
                tableId: $scope.tableId
        };

        $scope.client.send('/app/sendchatmessage', {}, JSON.stringify(tableMessage)); 
        $scope.chatMessage = '';
    };

}]);
