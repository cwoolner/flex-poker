flexpokerModule.controller('LogoutController', ['$rootScope', '$scope', 'ngstomp', function($rootScope, $scope, ngstomp) {
    if ($rootScope.client === undefined) {
        $rootScope.client = ngstomp(new SockJS(rootUrl + 'application'));
    }

    $rootScope.client.connect("", "", function() {
    }, function() {}, '/');
    
    $scope.logout = function() {
        $rootScope.client.disconnect();
    }

}]);
