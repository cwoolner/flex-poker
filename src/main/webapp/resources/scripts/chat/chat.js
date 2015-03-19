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
    let chatArea = $('.chat-display');
    let scrollHeight = chatArea.prop('scrollHeight');
    chatArea.prop('scrollTop', scrollHeight);
    chatArea.val(`${chatArea.val()}${message.body}\n`);
}

let chat = new Chat();
chat.registerChat('/topic/chat/global/user');
chat.registerChat('/topic/chat/global/system');
chat.registerChat('/user/topic/chat/personal/user', function(message) { alert('personal' + message.body); });
chat.registerChat('/user/topic/chat/personal/system', function(message) { alert('personal' + message.body); });

export default chat;
