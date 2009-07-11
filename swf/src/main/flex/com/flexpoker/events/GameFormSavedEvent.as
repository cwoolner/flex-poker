package com.flexpoker.events {

    import flash.events.Event;
    import components.GameForm;
    import com.flexpoker.model.Game;

    public class GameFormSavedEvent extends AbstractEvent {

        public static const NAME:String = "gameFormSavedEvent";

        private var game:Game;

        public function GameFormSavedEvent(game:Game) {
            super(NAME);
            this.game = game;
        }

        override public function clone():Event {
            return new GameFormSavedEvent(game);
        }

        public function getGame():Game {
            return game;
        }

    }

}
