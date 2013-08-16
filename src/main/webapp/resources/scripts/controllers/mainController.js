flexpokerModule.controller('MainController', ['$scope', 'ngstomp', function($scope, ngstomp) {
    $scope.client = ngstomp(new SockJS(rootUrl + 'application'));
    $scope.client.connect("", "", function(frame) {
        var queueSuffix = frame.headers['queue-suffix'];

        $scope.client.subscribe('/queue/errors' + queueSuffix, function(message) {
            alert("Error " + message.body);
        });

        $scope.client.subscribe("/app/opengamesforuser", function(message) {
            $scope.gameTabs = $.parseJSON(message.body);
        });
    }, function() {}, '/');

}]);
