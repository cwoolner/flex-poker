flexpokerModule.controller('LogoutController', ['$scope', function($scope) {
    $scope.logout = function() {
        stompClient.disconnect();
    }
}]);
