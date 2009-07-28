package com.flexpoker.model {

    [RemoteClass(alias="com.flexpoker.model.UserGameStatus")]
    [Bindable]
    public class UserGameStatus {

        public var id:int;

        public var user:User;

        public var game:Game;

        public var chips:int;

        public var money:Number;

        public var enterTime:Date;

        public var exitTime:Date;

    }

}
