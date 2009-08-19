package com.flexpoker.model {

    import mx.collections.ArrayCollection;
    import mx.collections.SortField;
    import mx.collections.Sort;

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

        public function sortPositions():void {
             var positionSortField:SortField = new SortField();
             positionSortField.name = "position";
             positionSortField.numeric = true;

             var numericDataSort:Sort = new Sort();
             numericDataSort.fields = [positionSortField];

             seats.sort = numericDataSort;
             seats.refresh();
        }

    }

}
