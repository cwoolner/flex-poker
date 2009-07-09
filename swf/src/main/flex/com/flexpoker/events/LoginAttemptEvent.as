package com.flexpoker.events {

    import flash.events.Event;

    public class LoginAttemptEvent extends AbstractEvent {

        public static const NAME:String = "loginAttemptEvent";

        private var username:String;

        private var password:String;

        public function LoginAttemptEvent(username:String, password:String) {
            super(NAME);
            this.username = username;
            this.password = password;
        }

        override public function clone():Event {
            return new LoginAttemptEvent(username, password);
        }

        public function getUsername():String {
            return username;
        }

        public function getPassword():String {
            return password;
        }

    }

}
