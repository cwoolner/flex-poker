function HelloController($scope) {
    $scope.greeting = {text: 'Hello'};
}

function TournamentRegisteringController($scope, $http) {
    $http.get(rootUrl + '/gamemanagement/tournament/registering/list').success(function(data, status, headers, config) {
        $scope.games = data;
    });
}
