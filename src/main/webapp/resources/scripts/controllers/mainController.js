flexpokerModule.controller('MainController', ['$rootScope', '$scope', 'ngstomp', function($rootScope, $scope, ngstomp) {
    if ($scope.client === undefined) {
        $scope.client = ngstomp(new SockJS(rootUrl + 'application'));
    }

    $scope.client.connect("", "", function(frame) {
        var queueSuffix = frame.headers['queue-suffix'];

        $scope.client.subscribe('/queue/errors' + queueSuffix, function(message) {
            alert("Error " + message.body);
        });
        
        $scope.client.subscribe('/topic/chat/personal/user' + queueSuffix, function(message) {
            alert('personal' + message.body);
        });

        $scope.client.subscribe('/topic/chat/personal/system' + queueSuffix, function(message) {
            alert('personal' + message.body);
        });

        $scope.client.subscribe("/app/opengamesforuser", function(message) {
            $scope.gameTabs = $.parseJSON(message.body);
        });
    }, function() {}, '/');

    if ($rootScope.stompClients === undefined) {
        $rootScope.stompClients = [];
    }
    $rootScope.stompClients.push($scope.client);
}]);
