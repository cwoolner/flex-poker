package com.flexpoker.events {

    import flash.events.Event;

    public class CheckEvent extends AbstractEvent {

        public static const NAME:String = "checkEvent";

        public function CheckEvent() {
            super(NAME);
        }

        override public function clone():Event {
            return new CheckEvent();
        }

    }

}
