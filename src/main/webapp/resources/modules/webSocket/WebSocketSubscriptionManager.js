import WebSocketService from './WebSocketService';

const WebSocketSubscriptionManager = () => {

  const componentToSubscriptionsMap = new Map();

  const subscribe = (component, subscriptions) => {
    const unsubscribePromises = subscriptions.map(
      x => WebSocketService.registerSubscription(x.location, x.subscription))
    componentToSubscriptionsMap.set(component, unsubscribePromises);
  };

  const unsubscribe = (component) => {
    componentToSubscriptionsMap.get(component).forEach(unsubscribePromise =>
        unsubscribePromise.then(unsubscribeCallback => unsubscribeCallback.unsubscribe()));
  };

  return ({subscribe, unsubscribe});

};

export default WebSocketSubscriptionManager();
