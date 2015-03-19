import flexpokerModule  from './main';
import logout from './logout/logout'

class Router {

    handleRoutes() {
        switch (location.hash) {
            case '#logout':
                logout();
                break;
            default:
                break;
        }
    }

}

flexpokerModule.config(['$routeProvider', function($routeProvider) {
    $routeProvider
        .when('/', {
            controller: 'TournamentRegisteringController',
            templateUrl: '/resources/templates/availableTournaments.html'
        })
        .when('/game/:gameId', {
            controller: 'GameController',
            templateUrl: '/resources/templates/game.html'
        })
        .when('/game/:gameId/table/:tableId', {
            controller: 'TableController',
            templateUrl: '/resources/templates/table.html'
        });
}]);

export default new Router().handleRoutes;
