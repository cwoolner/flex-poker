flexpokerModule.controller('LogoutController', ['$rootScope', '$scope', 'ngstomp', function($rootScope, $scope, ngstomp) {
    if ($scope.client === undefined) {
        $scope.client = ngstomp(new SockJS(rootUrl + 'application'));
    }

    $scope.client.connect("guest", "guest", function() {
    }, function() {}, '/');

    if ($rootScope.stompClients === undefined) {
        $rootScope.stompClients = [];
    }
    $rootScope.stompClients.push($scope.client);

    $scope.logout = function() {
        angular.forEach($rootScope.stompClients, function(client) {
            client.disconnect();
        });
    }

}]);
