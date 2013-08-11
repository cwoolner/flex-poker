flexpokerModule.config(function($routeProvider) {
    $routeProvider
        .when('/', {
            controller: 'TournamentRegisteringController',
            templateUrl: rootUrl + 'resources/templates/availableTournaments.html'
        });
});
