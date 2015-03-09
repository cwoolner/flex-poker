import { flexpokerModule, stompClient } from '../main';

flexpokerModule.controller('LogoutController', ['$scope', function($scope) {
    $scope.logout = function() {
        stompClient.disconnect();
    }
}]);
