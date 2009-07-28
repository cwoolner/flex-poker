package com.flexpoker.model {

    import flash.errors.IllegalOperationError;

    public class TableImages {

        private static var instance:TableImages = new TableImages();

        [Embed(source="/assets/button.png")]
        private var buttonImage:Class;

        [Embed(source="/assets/smallBlind.png")]
        private var smallBlindImage:Class;

        [Embed(source="/assets/bigBlind.png")]
        private var bigBlindImage:Class;

        public function TableImages() {
            if (instance != null) {
                throw new IllegalOperationError("Can't create more than one "
                        + "instance.");
            }
        }

        public static function getInstance():TableImages {
            return instance;
        }

        public function getButtonImage():Class {
            return buttonImage;
        }

        public function getSmallBlindImage():Class {
            return smallBlindImage;
        }

        public function getBigBlindImage():Class {
            return bigBlindImage;
        }

    }

}
