require('babel/polyfill');
import handleRoutes from './router/router';
import chat from './chat/chat';

window.onhashchange = handleRoutes;

var flexpokerModule = angular.module('flexpoker', ['ngRoute']);

export default flexpokerModule;
