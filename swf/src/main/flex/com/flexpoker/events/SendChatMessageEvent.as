package com.flexpoker.events {

    import flash.events.Event;

    public class SendChatMessageEvent extends AbstractEvent {

        public static const NAME:String = "sendChatMessageEvent";

        private var messageText:String;

        public function SendChatMessageEvent(messageText:String) {
            super(NAME);
            this.messageText = messageText;
        }

        override public function clone():Event {
            return new SendChatMessageEvent(messageText);
        }

        public function getMessageText():String {
            return messageText;
        }

    }

}
