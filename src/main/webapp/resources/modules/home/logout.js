import webSocketService from '../common/webSocketService';

function logout() {
    webSocketService.disconnect();

    var token = document.querySelector("meta[name='_csrf']").content;
    var header = document.querySelector("meta[name='_csrf_header']").content;

    var myHeaders = new Headers();
    myHeaders.append(header, token);

    var myInit = {
            method: 'POST',
            headers: myHeaders,
            cache: 'no-cache',
            redirect: 'manual',
            credentials: 'same-origin'
    };

    // the url coming back is actually blank becuase it's an opaqueredirect.
    // this is fine for our purposes because we'll just be redirected to the
    // login page anyway
    fetch('/logout', myInit).then(response => location.href = response.url);
}

export default logout;
