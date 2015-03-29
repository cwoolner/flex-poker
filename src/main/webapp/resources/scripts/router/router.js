import logout from '../logout/logout';

class Router {

    handleRoutes() {
        let hash = location.hash;

        if (hash.startsWith('#/logout')) {
            logout();
            return;
        }

        if (hash.match(/#\/game\/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\/?/)) {
            let gameId = hash.match(/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}/)[0];
            let viewArea = document.querySelector('#view-area');

            let gamePageElement = document.createElement('game-page');
            gamePageElement.setAttribute('gameid', gameId);

            // NOTE: probably leaks memory since no handlers are cleaned
            while (viewArea.firstChild) {
                viewArea.removeChild(viewArea.firstChild);
            }
            viewArea.appendChild(gamePageElement);
            return;
        }
    }

}

export default new Router().handleRoutes;
