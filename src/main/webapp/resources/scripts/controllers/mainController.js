flexpokerModule.controller('MainController', ['$rootScope', '$scope', 'ngstomp', function($rootScope, $scope, ngstomp) {
    if ($rootScope.client === undefined) {
        $rootScope.client = ngstomp(new SockJS(rootUrl + 'application'));
    }

    $rootScope.client.connect("", "", function(frame) {
        var queueSuffix = frame.headers['queue-suffix'];

        $rootScope.client.subscribe('/queue/errors' + queueSuffix, function(message) {
            alert("Error " + message.body);
        });

        $rootScope.client.subscribe("/app/opengamesforuser", function(message) {
            $scope.gameTabs = $.parseJSON(message.body);
        });
    }, function() {}, '/');

}]);
