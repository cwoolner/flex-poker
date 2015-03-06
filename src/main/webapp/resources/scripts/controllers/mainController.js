flexpokerModule.controller('MainController', ['$rootScope', '$scope', 'ngstomp', '$location', '$templateCache',
    function($rootScope, $scope, ngstomp, $location, $templateCache) {
    
    $scope.clearCache = function() { 
        $templateCache.removeAll();
    };
    
    $rootScope.username = $('.username').prop('innerText');
    
    if ($scope.client === undefined) {
        $scope.client = ngstomp(new SockJS(rootUrl + 'application'));
    }

    $scope.client.connect("guest", "guest", function(frame) {

        $scope.client.subscribe('/user/queue/errors', function(message) {
            alert("Error " + message.body);
            $rootScope.tryingToJoinGameId = null;
        });
        
        $scope.client.subscribe('/user/topic/chat/personal/user', function(message) {
            alert('personal' + message.body);
        });

        $scope.client.subscribe('/user/topic/chat/personal/system', function(message) {
            alert('personal' + message.body);
        });

        $scope.client.subscribe('/app/opengamesforuser', function(message) {
            $scope.gameTabs = $.parseJSON(message.body);
        });
        
        $scope.client.subscribe('/user/queue/opengamesforuser', function(message) {
            $scope.gameTabs = $.parseJSON(message.body);
            if ($rootScope.tryingToJoinGameId != null) {
                $location.path('/game/' + $rootScope.tryingToJoinGameId)
                $rootScope.tryingToJoinGameId = null;
            }
        });
        
        $scope.client.subscribe('/user/queue/opentable', function(message) {
            var openTable = $.parseJSON(message.body);
            $location.path('/game/' + openTable.gameId + '/table/' + openTable.tableId);
        });

        $scope.client.subscribe('/user/queue/personaltablestatus', function(message) {
            alert($.parseJSON(message.body));
        });

        $scope.client.subscribe('/user/queue/pocketcards', function(message) {
            var pocketCards = $.parseJSON(message.body);
            $rootScope.$broadcast('pocketCardsReceived' + pocketCards.tableId,
                    {cardId1: pocketCards.cardId1, cardId2: pocketCards.cardId2});
        });

    }, function() {}, '/');

    if ($rootScope.stompClients === undefined) {
        $rootScope.stompClients = [];
    }
    $rootScope.stompClients.push($scope.client);
}]);
