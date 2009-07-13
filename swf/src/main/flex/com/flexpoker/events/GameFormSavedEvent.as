package com.flexpoker.events {

    import flash.events.Event;
    import components.GameForm;
    import com.flexpoker.model.Game;
    import components.GameForm;

    public class GameFormSavedEvent extends AbstractEvent {

        public static const NAME:String = "gameFormSavedEvent";

        private var game:Game;

        private var form:GameForm;

        public function GameFormSavedEvent(game:Game, form:GameForm) {
            super(NAME);
            this.game = game;
            this.form = form;
        }

        override public function clone():Event {
            return new GameFormSavedEvent(game, form);
        }

        public function getGame():Game {
            return game;
        }

        public function getForm():GameForm {
            return form;
        }

    }

}
