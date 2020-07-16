/**
 *
 * @param options
 * @constructor
 */
InitGeoPicDesktop = function (options) {
    let me = this;
    me.userDbname = options;
    console.log(me.userDbname+"你还幼稚吗");
    me.getUserPhotosInfo();
    me.te();
    me.mapControl = new MapControl({
        div: "map"
    });

};

InitGeoPicDesktop.prototype.getUserPhotosInfo = function () {
    let me = this;

    let result = ["photoCount","placeCount","faceCount","photoPath","GPS"];
    $.ajax({
        url:"/SpatialDB-GeoPic-System/initGeoPicDesktopServlet",
        type:"POST",
        data:{
            "data":"",
            "result":result.toString(),
            "userDbname":me.userDbname
        },
        success:function (res) {
            let json = typeof res=='string'?JSON.parse(res):res;
            if(json.message=="success"){
                let photoCount = json.photoCount;
                let faceCount = json.faceCount;
                let placeCount = json.placeCount;

                //存储了所有的照片的GPS和Path,是个数组，每个数组是jsonObject[{"GPS":[112.3,32.1],"photoPath":"xx"},{}
                let photoPathAndGPS = json.photoPathAndGPS;
                me.setInitGeoPicDesktop(photoCount,faceCount,placeCount,photoPathAndGPS);

            }else{
                console.log(json.message)
            }
        },
        error:function (err) {
            console.log(err)
        },
    });
};

InitGeoPicDesktop.prototype.setInitGeoPicDesktop = function (photoCount,faceCount,
                                                             placeCount,photoPathAndGPS) {
    let me = this;
    $(".photoCount").text(photoCount);
    $(".faceCount").text(faceCount);
    $(".placeCount").text(placeCount);
    me.mapControl.initAsPreview(photoPathAndGPS, me.userDbname);
};
InitGeoPicDesktop.prototype.te= function () {
    $.ajax({
        url:"/SpatialDB-GeoPic-System/getPhotosOfPoiServlet",
        type:"post",
        data:{
            "GPS":"POINT(126.60644296 44.27068958)",
            "userDbname":"db1"
        },
        success:function (res) {
            console.log(res)
        },
        error:function (err) {
            console.log(err)
        }
    })
};



