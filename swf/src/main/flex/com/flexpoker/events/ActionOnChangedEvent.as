package com.flexpoker.events {

    import flash.events.Event;

    public class ActionOnChangedEvent extends AbstractEvent {

        public static const NAME:String = "actionOnChangedEvent";

        public function ActionOnChangedEvent() {
            super(NAME);
        }

        override public function clone():Event {
            return new ActionOnChangedEvent();
        }

    }

}
