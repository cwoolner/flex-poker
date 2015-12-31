import webSocketService from '../webSocketService.js';

function logout() {
    webSocketService.disconnect();
    location.href = '/logout';
}

export default logout;
