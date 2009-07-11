package com.flexpoker.model {

    [RemoteClass(alias="com.flexpoker.model.GameType")]
    [Bindable]
    public class GameType {

        public var id:int;

        public var name:String;

        public var minimumPlayers:int;

        public var maximumPlayers:int;

        public var allowRebuys:Boolean;

    }

}
