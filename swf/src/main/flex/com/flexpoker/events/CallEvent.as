package com.flexpoker.events {

    import flash.events.Event;

    public class CallEvent extends AbstractEvent {

        public static const NAME:String = "callEvent";

        public function CallEvent() {
            super(NAME);
        }

        override public function clone():Event {
            return new CallEvent();
        }

    }

}
