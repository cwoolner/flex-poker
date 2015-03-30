import flexpokerModule  from './main';

flexpokerModule.config(['$routeProvider', function($routeProvider) {
    $routeProvider
        .when('/game/:gameId/table/:tableId', {
            controller: 'TableController',
            templateUrl: '/resources/templates/table.html'
        });
}]);
