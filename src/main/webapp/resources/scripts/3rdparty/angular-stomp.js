/*
 * Copyright: 2012, V. Glenn Tarcea
 * MIT License Applies
 */

angular.module('AngularStomp', []).
    factory('ngstomp', function($rootScope) {
        var stompClient = {};

        function NGStomp(connection) {
            if (connection instanceof SockJS) {
                this.stompClient = Stomp.over(connection);
            } else {
                this.stompClient = Stomp.client(connection);    
            }
        }

        NGStomp.prototype.subscribe = function(queue, callback) {
            this.stompClient.subscribe(queue, function() {
                var args = arguments;
                $rootScope.$apply(function() {
                    callback(args[0]);
                })
            })
        }

        NGStomp.prototype.send = function(queue, headers, data) {
            this.stompClient.send(queue, headers, data);
        }

        NGStomp.prototype.connect = function(user, password, on_connect, on_error, vhost) {
            if (stompClient.connected) {
                return;
            }
            this.stompClient.connect(user, password,
                function(frame) {
                    $rootScope.$apply(function() {
                        on_connect.apply(stompClient, frame);
                    })
                },
                function(frame) {
                    $rootScope.$apply(function() {
                        on_error.apply(stompClient, frame);
                    })
                }, vhost);
        }

        NGStomp.prototype.disconnect = function(callback) {
            this.stompClient.disconnect(function() {
                var args = arguments;
                $rootScope.$apply(function() {
                    callback.apply(args);
                })
            })
        }

        return function(connection) {
            return new NGStomp(connection);
        }
    });
