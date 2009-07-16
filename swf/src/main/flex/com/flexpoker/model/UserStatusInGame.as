package com.flexpoker.model {

    [RemoteClass(alias="com.flexpoker.model.UserStatusInGame")]
    [Bindable]
    public class UserStatusInGame {

        public var id:int;

        public var user:User;

        public var game:Game;

        public var chips:Integer;

        public var money:Number;

        public var enterTime:Date;

        public var exitTime:Date;

    }

}
