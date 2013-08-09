flexpokerModule.provider('StompConnection', function() {
    var self = this;
    var stompClient;

    self.setStompClient = function(client) {
        stompClient = client;
    };
    
    function StompConnection() {
        this.connect = function() {
            stompClient.connect('', '', function(frame) {
                var queueSuffix = frame.headers['queue-suffix'];

                stompClient.subscribe("/app/availabletournaments", function(message) {
                    var scope = angular.element($("#tournament-registering")).scope();
                    scope.$apply(function(){
                        scope.games = $.parseJSON(message.body);
                    })
                });
                stompClient.subscribe("/topic/availabletournaments-updates", function(message) {
                    alert(JSON.parse(message.body));
                });
                stompClient.subscribe("/queue/errors" + queueSuffix, function(message) {
                    alert("Error " + message.body);
                });
            }, function(error) {
                console.log("STOMP protocol error " + error);
            });
        }
        
        this.logout = function() {
            stompClient.disconnect();
        };
    }
    
    self.$get = function() {
        return new StompConnection();
    };

});

var socket = new SockJS(rootUrl + 'application');
var stompClient = Stomp.over(socket);
flexpokerModule.config(function(StompConnectionProvider) {
    StompConnectionProvider.setStompClient(stompClient);
});
