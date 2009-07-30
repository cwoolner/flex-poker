package com.flexpoker.model {

    import mx.collections.ArrayCollection;

    [RemoteClass(alias="com.flexpoker.model.Table")]
    [Bindable]
    public class Table {

        public var id:int;

        public var game:Game;

        public var button:Seat;

        public var smallBlind:Seat;

        public var bigBlind:Seat;

        public var actionOn:Seat;

        public var seats:ArrayCollection;

    }

}
