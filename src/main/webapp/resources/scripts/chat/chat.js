import webSocketService from '../webSocketService.js';

class Chat {

    registerChat(channel, functionToRegister) {
        if (!functionToRegister) {
            functionToRegister = receiveChat;
        }
        webSocketService.registerSubscription(channel, functionToRegister);
    }

}

function receiveChat(message) {
    let destination = message.headers.destination;

    if (destination.match(/\/topic\/chat\/global/)) {
        document.querySelector('.global-chat').displayChat(message.body);
    } else if (destination.match(/\/topic\/chat\/game\/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\/(user|system)/)) {
        document.querySelector('.game-chat').displayChat(message.body);
    } else if (destination.match(/\/topic\/chat\/game\/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\/table\/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\/(user|system)/)) {
        document.querySelector('.table-chat').displayChat(message.body);
    }
}

let chat = new Chat();
chat.registerChat('/topic/chat/global/user');
chat.registerChat('/topic/chat/global/system');
chat.registerChat('/user/topic/chat/personal/user', function(message) { alert('personal' + message.body); });
chat.registerChat('/user/topic/chat/personal/system', function(message) { alert('personal' + message.body); });

export default chat;
