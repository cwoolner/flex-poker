import handleRoutes from './router/router.js';
import webSocketService from './webSocketService.js';

webSocketService.registerSubscription('/user/topic/chat/personal/user', function(message) { alert('personal' + message.body); });
webSocketService.registerSubscription('/user/topic/chat/personal/system', function(message) { alert('personal' + message.body); });

window.onhashchange = handleRoutes;
