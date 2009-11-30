package com.flexpoker.model {

    [RemoteClass(alias="com.flexpoker.model.UserGameStatus")]
    [Bindable]
    public class UserGameStatus {

        public var user:User;

        public var minBet:int;

        public var callAmount:int;

        public var chips:int;

        public var enterTime:Date;

        public var exitTime:Date;

    }

}
