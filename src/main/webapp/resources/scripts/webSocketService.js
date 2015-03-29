class WebSocketService {

    constructor() {
        this.nonRegisteredSubscriptions = [];
        this.client = Stomp.over(new SockJS('/application'));

        let connectCallback = frame => {
            this.nonRegisteredSubscriptions.forEach(item => {
                this.client.subscribe(item.location, item.subscription);
            });
        };

        this.client.connect({}, connectCallback);
    }

    registerSubscription(location, subscription) {
        if (this.client.connected) {
            this.client.subscribe(location, subscription);
        } else {
            this.nonRegisteredSubscriptions.push({location, subscription});
        }
    }

    send(location, objectToSend) {
        this.client.send(location, {}, JSON.stringify(objectToSend));
    }

    disconnect() {
        this.client.disconnect();
    }

}

// HACK: making it global for usage inside of a WebComponent.  get rid of this once a real es6 module loader is added to the project
let webSocketService = new WebSocketService();

if (window) {
    window.webSocketService = webSocketService;
}

export default webSocketService;
