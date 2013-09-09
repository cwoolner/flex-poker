flexpokerModule.config(function($routeProvider) {
    $routeProvider
        .when('/', {
            controller: 'TournamentRegisteringController',
            templateUrl: rootUrl + 'resources/templates/availableTournaments.html'
        })
        .when('/game/:gameId', {
            controller: 'GameController',
            templateUrl: rootUrl + 'resources/templates/game.html'
        })
        .when('/game/:gameId/table/:tableId', {
            controller: 'TableController',
            templateUrl: rootUrl + 'resources/templates/table.html'
        });
});
