package com.flexpoker.model {

    [RemoteClass(alias="com.flexpoker.model.Seat")]
    [Bindable]
    public class Seat {

        public var id:int;

        public var userGameStatus:UserGameStatus;

        public var position:int;

        public var stillInHand:Boolean;

        public var allIn:Boolean;

        public var playerJustLeft:Boolean;

        public var chipsInFront:int;

        public var minBet:int;

        public var callAmount:int;

    }

}
