package com.flexpoker.model {

    [RemoteClass(alias="com.flexpoker.model.User")]
    [Bindable]
    public class User {

        public var id:int;

        public var username:String;

        public var password:String;

    }

}
