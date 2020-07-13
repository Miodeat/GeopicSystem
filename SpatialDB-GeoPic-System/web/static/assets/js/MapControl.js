MapControl = function (options) {
    let me = this;

    me.ops = $.extend({
        width: 100,
        height: 600
    }, options);

    me._init()
};

MapControl.prototype._init = function () {
    let me = this;

    me.div = $("#" + me.ops.div).css({
        position: "relative",
        width: me.ops.width + "%",
        height: me.ops.height + "px"
    });

    me.map = new AMap.Map(me.ops.div, {
        center: [112.3, 28.9],
        zoom: 4
    })
};