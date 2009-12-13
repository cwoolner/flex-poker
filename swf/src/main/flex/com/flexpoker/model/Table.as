package com.flexpoker.model {

    import mx.collections.ArrayCollection;
    import mx.collections.SortField;
    import mx.collections.Sort;

    [RemoteClass(alias="com.flexpoker.model.Table")]
    [Bindable]
    public class Table {

        public var id:int;

        public var seats:ArrayCollection;

        public var totalPotAmount:int;

        public var potAmounts:ArrayCollection;

    }

}
