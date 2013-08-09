flexpokerModule.config(function($routeProvider) {
    $routeProvider
        .when('/', {
            controller: 'HelloController',
            templateUrl: rootUrl + 'resources/templates/helloWorld.html'
        })
        .when('/availableTournaments', {
            controller: 'TournamentRegisteringController',
            templateUrl: rootUrl + 'resources/templates/availableTournaments.html'
        });
});
