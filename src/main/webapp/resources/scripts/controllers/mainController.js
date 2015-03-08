flexpokerModule.controller('MainController', ['$rootScope', '$scope', '$location', '$templateCache',
    function($rootScope, $scope, $location, $templateCache) {
    
    stompClient.connect({}, function(frame) {
        $rootScope.$broadcast('stomp-connected', {frame: frame});
    });

    $scope.clearCache = function() { 
        $templateCache.removeAll();
    };

    $rootScope.username = $('.username').prop('innerText');

    if (stompClient.connected) {
        registerStompSubscriptions();
    }

    $scope.$on('stomp-connected', function(event, data) {
        registerStompSubscriptions();
    });

    function registerStompSubscriptions() {
        stompClient.subscribe('/user/queue/errors', function(message) {
            alert("Error " + message.body);
            $rootScope.tryingToJoinGameId = null;
        });

        stompClient.subscribe('/user/topic/chat/personal/user', function(message) {
            alert('personal' + message.body);
        });

        stompClient.subscribe('/user/topic/chat/personal/system', function(message) {
            alert('personal' + message.body);
        });

        stompClient.subscribe('/app/opengamesforuser', function(message) {
            $scope.$apply(function() {
                $scope.gameTabs = $.parseJSON(message.body);
            });
        });

        stompClient.subscribe('/user/queue/opengamesforuser', function(message) {
            $scope.$apply(function() {
                $scope.gameTabs = $.parseJSON(message.body);
            });
            if ($rootScope.tryingToJoinGameId != null) {
                $scope.$apply(function() {
                    $location.path('/game/' + $rootScope.tryingToJoinGameId);
                });
                $rootScope.tryingToJoinGameId = null;
            }
        });

        stompClient.subscribe('/user/queue/opentable', function(message) {
            var openTable = $.parseJSON(message.body);
            $scope.$apply(function() {
                $location.path('/game/' + openTable.gameId + '/table/' + openTable.tableId);
            });
        });

        stompClient.subscribe('/user/queue/personaltablestatus', function(message) {
            alert($.parseJSON(message.body));
        });

        stompClient.subscribe('/user/queue/pocketcards', function(message) {
            var pocketCards = $.parseJSON(message.body);
            $rootScope.$broadcast('pocketCardsReceived' + pocketCards.tableId,
                    {cardId1: pocketCards.cardId1, cardId2: pocketCards.cardId2});
        });
    }

}]);
