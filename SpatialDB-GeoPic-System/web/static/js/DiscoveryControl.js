DiscoveryControl = function (ops) {
    let me = this;

    me.ops = ops;
    me.mapDivID = me.ops.div;

    me._init();
};

DiscoveryControl.prototype._init = function () {
    let me = this;
    let mapControl = new MapControl({
        div: me.mapDivID
    });
    mapControl.initAsDiscovery();
};