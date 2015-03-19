import webSocketService from '../webSocketService';

function logout() {
    webSocketService.disconnect();
    location.href = '/logout';
}

export default logout;
