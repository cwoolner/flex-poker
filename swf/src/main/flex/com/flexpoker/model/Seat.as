package com.flexpoker.model {

    [RemoteClass(alias="com.flexpoker.model.Seat")]
    [Bindable]
    public class Seat {

        public var id:int;

        public var userGameStatus:UserGameStatus;

        public var position:int;

    }

}
