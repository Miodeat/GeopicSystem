QueryPageControl = function (dbName) {
    let me = this;

    me.dbName = dbName;
    me._init();
};

QueryPageControl.prototype._init = function () {
    let me = this;

    me.mapControl = MapControl({
        div: "map"
    })
};