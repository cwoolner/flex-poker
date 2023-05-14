import WebSocketService from './WebSocketService'

const WebSocketSubscriptionManager = () => {

  const componentToSubscriptionsMap = new Map()

  const subscribe = (component, subscriptions) => {
    const existingSubscriptions = componentToSubscriptionsMap.get(component) || []

    const newSubscriptions = subscriptions.filter(x =>
      existingSubscriptions.filter(y => y.location === x.location).length === 0)

    const registeredSubs = newSubscriptions.map(x => ({
      location: x.location,
      subscription: WebSocketService.registerSubscription(x.location, x.subscription)
    }))
    componentToSubscriptionsMap.set(component, existingSubscriptions.concat(registeredSubs))
  }

  const unsubscribe = (component) => {
    componentToSubscriptionsMap.get(component).forEach(subscription =>
        subscription.subscription.then(unsubscribeCallback => unsubscribeCallback.unsubscribe()))
  }

  return ({subscribe, unsubscribe})

}

export default WebSocketSubscriptionManager()
