package com.flexpoker.events {

    import flash.events.Event;
    import components.GamePanel;

    public class GamePanelCanceledEvent extends AbstractEvent {

        public static const NAME:String = "gamePanelCanceledEvent";

        private var panel:GamePanel;

        public function GamePanelCanceledEvent(panel:GamePanel) {
            super(NAME);
            this.panel = panel;
        }

        override public function clone():Event {
            return new GamePanelCanceledEvent(panel);
        }

        public function getPanel():GamePanel {
            return panel;
        }

    }

}
