package com.flexpoker.events {

    import flash.events.Event;
    import components.GameForm;

    public class GameFormCanceledEvent extends AbstractEvent {

        public static const NAME:String = "gameFormCanceledEvent";

        private var form:GameForm;

        public function GameFormCanceledEvent(form:GameForm) {
            super(NAME);
            this.form = form;
        }

        override public function clone():Event {
            return new GameFormCanceledEvent(form);
        }

        public function getForm():GameForm {
            return form;
        }

    }

}
