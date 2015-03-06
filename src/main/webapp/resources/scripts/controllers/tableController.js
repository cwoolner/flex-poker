flexpokerModule.controller('TableController', ['$scope', '$rootScope', '$routeParams', 'ngstomp', function($scope, $rootScope, $routeParams, ngstomp) {
    $scope.gameId = $routeParams['gameId'];
    $scope.tableId = $routeParams['tableId'];
    $scope.username = $rootScope.username;

    $scope.chatDisplay = '';
    
    if ($scope.client === undefined) {
        $scope.client = ngstomp(new SockJS(rootUrl + 'application'));
    }
    
    $rootScope.$on('pocketCardsReceived' + $scope.tableId, function(event, data) {
        $scope.myLeftCardUrl = cardData[data.cardId1];
        $scope.myRightCardUrl = cardData[data.cardId2];
    });

    $scope.client.connect("guest", "guest", function() {
        
        $scope.client.subscribe('/topic/chat/game/' + $scope.gameId
                + '/table/' + $scope.tableId + '/user', function(message) {
            var scrollHeight = $('.chat-display').prop('scrollHeight');
            $('.chat-display').prop('scrollTop', scrollHeight);
            $scope.chatDisplay += message.body + '\n';
        });

        $scope.client.subscribe('/topic/chat/game/' + $scope.gameId
                + '/table/' + $scope.tableId + '/system', function(message) {
            var scrollHeight = $('.chat-display').prop('scrollHeight');
            $('.chat-display').prop('scrollTop', scrollHeight);
            $scope.chatDisplay += message.body + '\n';
        });

        $scope.client.subscribe('/topic/game/' + $scope.gameId
                + '/table/' + $scope.tableId, function(message) {
            var table = $.parseJSON(message.body);
            $scope.table = table;
            
            var mySeat = _.find(table.seats, {name: $scope.username});
            
            if (mySeat) {
                var pokerActions = {
                    actionOn: mySeat.actionOn,
                    check: mySeat.callAmount === 0,
                    fold: mySeat.callAmount !== 0,
                    call: mySeat.callAmount !== 0,
                    raise: mySeat.raiseTo !== 0
                };
                $scope.pokerActions = pokerActions;
            }
            
            $scope.commonCards = [];
            $scope.table.visibleCommonCards.forEach(function(commonCard) {
                $scope.commonCards.push(cardData[commonCard.id]);
            });
        });

    }, function() {}, '/');
    
    if ($rootScope.stompClients === undefined) {
        $rootScope.stompClients = [];
    }
    $rootScope.stompClients.push($scope.client);

    $scope.sendChat = function() {
        if ($scope.chatMessage == '') {
            return;
        }

        var tableMessage = {
                message: $scope.chatMessage,
                receiverUsernames: null,
                gameId: $scope.gameId,
                tableId: $scope.tableId
        };

        $scope.client.send('/app/sendchatmessage', {}, JSON.stringify(tableMessage)); 
        $scope.chatMessage = '';
    };
    
    $scope.check = function() {
        var checkMessage = {
            gameId: $scope.gameId,
            tableId: $scope.tableId
        };
        $scope.client.send('/app/check', {}, JSON.stringify(checkMessage));
    };

    $scope.call = function() {
        var callMessage = {
            gameId: $scope.gameId,
            tableId: $scope.tableId
        };
        $scope.client.send('/app/call', {}, JSON.stringify(callMessage));
    };

    $scope.raise = function() {
        var raiseMessage = {
            gameId: $scope.gameId,
            tableId: $scope.tableId
        };
        $scope.client.send('/app/raise', {}, JSON.stringify(raiseMessage));
    };

    $scope.fold = function() {
        var foldMessage = {
            gameId: $scope.gameId,
            tableId: $scope.tableId
        };
        $scope.client.send('/app/fold', {}, JSON.stringify(foldMessage));
    };

}]);
