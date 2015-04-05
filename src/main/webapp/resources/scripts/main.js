require('babel/polyfill');
import handleRoutes from './router/router';
import webSocketService from './webSocketService';

webSocketService.registerSubscription('/user/topic/chat/personal/user', function(message) { alert('personal' + message.body); });
webSocketService.registerSubscription('/user/topic/chat/personal/system', function(message) { alert('personal' + message.body); });

window.onhashchange = handleRoutes;
