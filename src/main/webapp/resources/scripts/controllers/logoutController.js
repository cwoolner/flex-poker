import flexpokerModule from '../main';
import webSocketService from '../webSocketService';

flexpokerModule.controller('LogoutController', ['$scope', function($scope) {
    $scope.logout = function() {
        webSocketService.disconnect();
    }
}]);
