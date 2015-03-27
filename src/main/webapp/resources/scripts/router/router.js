import logout from '../logout/logout'

class Router {

    handleRoutes() {
        let hash = location.hash;

        if (hash.startsWith('#/logout')) {
            logout();
            return;
        }

        if (hash.match(/#\/game\/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\/?/)) {
            return;
        }
    }

}

export default new Router().handleRoutes;
