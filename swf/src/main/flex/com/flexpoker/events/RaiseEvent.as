package com.flexpoker.events {

    import flash.events.Event;

    public class RaiseEvent extends AbstractEvent {

        public static const NAME:String = "raiseEvent";

        private var raiseAmount:String;

        public function RaiseEvent(raiseAmount:String) {
            super(NAME);
            this.raiseAmount = raiseAmount;
        }

        override public function clone():Event {
            return new RaiseEvent(raiseAmount);
        }

        public function getRaiseAmount():String {
            return raiseAmount;
        }

   }

}
