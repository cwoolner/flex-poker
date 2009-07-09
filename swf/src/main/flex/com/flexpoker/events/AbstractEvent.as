package com.flexpoker.events {

    import flash.events.Event;
    import flash.errors.IllegalOperationError;

    public class AbstractEvent extends Event {

        public function AbstractEvent(name:String) {
            super(name, true, true);
        }

        override public function clone():Event {
            throw new IllegalOperationError("clone() method should not be "
                    + "called on AbstractEvent.");
        }

    }

}
