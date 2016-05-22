import logout from './modules/home/logout';

function handleRoutes() {
    let hash = location.hash;

    if (hash === '' || hash === '#/') {
        let viewArea = document.querySelector('#view-area');
        let mainPageElement = document.createElement('fp-main-page');

        // NOTE: probably leaks memory since no handlers are cleaned
        while (viewArea.firstChild) {
            viewArea.removeChild(viewArea.firstChild);
        }
        viewArea.appendChild(mainPageElement);
        return;
    }

    if (hash.match('#/?logout')) {
        logout();
        return;
    }

    if (hash.match(/#\/game\/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\/table\/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}/)) {
        let match = hash.match(/([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})*.([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})/g);
        let gameId = match[0].replace('/', '');
        let tableId = match[1].replace('/', '');

        let viewArea = document.querySelector('#view-area');

        let tablePageElement = document.createElement('fp-table-page');
        tablePageElement.setAttribute('gameid', gameId);
        tablePageElement.setAttribute('tableid', tableId);

        // NOTE: probably leaks memory since no handlers are cleaned
        while (viewArea.firstChild) {
            viewArea.removeChild(viewArea.firstChild);
        }
        viewArea.appendChild(tablePageElement);
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

export default handleRoutes;
