import flexpokerModule  from './main';

flexpokerModule.config(['$routeProvider', function($routeProvider) {
    $routeProvider
        .when('/', {
            controller: 'TournamentRegisteringController',
            templateUrl: '/resources/templates/availableTournaments.html'
        })
        .when('/game/:gameId/table/:tableId', {
            controller: 'TableController',
            templateUrl: '/resources/templates/table.html'
        });
}]);
