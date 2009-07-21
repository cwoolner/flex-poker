package com.flexpoker.model {

    [RemoteClass(alias="com.flexpoker.model.Seat")]
    [Bindable]
    public class Seat {

        public var id:int;

        public var table:Table;

        public var user:User;

        public var position:int;

    }

}
