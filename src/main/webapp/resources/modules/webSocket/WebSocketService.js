import webstomp from 'webstomp-client';
import SockJS from 'sockjs-client';

class WebSocketService {

    constructor() {
        this.client = webstomp.over(new SockJS('/application'), {debug: false});

        let connectCallback = frame => {
          const webSocketConnectedEvent = new CustomEvent('webSocketConnected', {
            detail: {},
            bubbles: true
          });
          document.dispatchEvent(webSocketConnectedEvent);
        };

        this.client.connect({}, connectCallback);
    }

    registerSubscription(location, subscription) {
      return new Promise((resolve, reject) => {
          if (this.client.connected) {
            resolve(this.client.subscribe(location, subscription));
        } else {
          document.addEventListener('webSocketConnected', evt => {
            resolve(this.client.subscribe(location, subscription));
          });
        }
      });
    }

    send(location, objectToSend) {
        this.client.send(location, JSON.stringify(objectToSend));
    }

    disconnect() {
        this.client.disconnect();
    }

}

export default new WebSocketService();
