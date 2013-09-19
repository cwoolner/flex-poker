flexpokerModule.controller('MainController', ['$rootScope', '$scope', 'ngstomp', '$location', function($rootScope, $scope, ngstomp, $location) {
    if ($scope.client === undefined) {
        $scope.client = ngstomp(new SockJS(rootUrl + 'application'));
    }

    $scope.client.connect("", "", function(frame) {
        var queueSuffix = frame.headers['queue-suffix'];

        $scope.client.subscribe('/queue/errors' + queueSuffix, function(message) {
            alert("Error " + message.body);
            $rootScope.tryingToJoinGameId = null;
        });
        
        $scope.client.subscribe('/topic/chat/personal/user' + queueSuffix, function(message) {
            alert('personal' + message.body);
        });

        $scope.client.subscribe('/topic/chat/personal/system' + queueSuffix, function(message) {
            alert('personal' + message.body);
        });

        $scope.client.subscribe('/app/opengamesforuser', function(message) {
            $scope.gameTabs = $.parseJSON(message.body);
        });
        
        $scope.client.subscribe('/queue/opengamesforuser' + queueSuffix, function(message) {
            $scope.gameTabs = $.parseJSON(message.body);
            if ($rootScope.tryingToJoinGameId != null) {
                $location.path('/game/' + $rootScope.tryingToJoinGameId)
                $rootScope.tryingToJoinGameId = null;
            }
        });
        
        $scope.client.subscribe('/queue/opentable' + queueSuffix, function(message) {
            var openTable = $.parseJSON(message.body);
            $location.path('/game/' + openTable.gameId + '/table/' + openTable.tableId);
        });

        $scope.client.subscribe('/queue/personaltablestatus' + queueSuffix, function(message) {
            alert($.parseJSON(message.body));
        });

        $scope.client.subscribe('/queue/pocketcards' + queueSuffix, function(message) {
            alert($.parseJSON(message.body));
        });

    }, function() {}, '/');

    if ($rootScope.stompClients === undefined) {
        $rootScope.stompClients = [];
    }
    $rootScope.stompClients.push($scope.client);
}]);
