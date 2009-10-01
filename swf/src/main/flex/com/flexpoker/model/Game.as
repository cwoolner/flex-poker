package com.flexpoker.model {

    [RemoteClass(alias="com.flexpoker.model.Game")]
    [Bindable]
    public class Game {

        public var id:int;

        public var createdByUser:User;

        public var startTime:Date;

        public var endTime:Date;

        public var createdOn:Date;

        public var canceledOn:Date;

        public var totalPlayers:int;

        public var playersRemaining:int;

        public var gameStage:String;

    }

}
