package com.flexpoker.events {

    import flash.events.Event;

    public class FoldEvent extends AbstractEvent {

        public static const NAME:String = "foldEvent";

        public function FoldEvent() {
            super(NAME);
        }

        override public function clone():Event {
            return new FoldEvent();
        }

    }

}
