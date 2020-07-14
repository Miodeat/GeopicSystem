/**
 *
 * @param options
 * @constructor
 */
InitGeoPicDesktop = function (options) {
    let me = this;
    me.getUserPhotosInfo();
    me.mapControl = new MapControl({
        div: "map"
    });
};

InitGeoPicDesktop.prototype.getUserPhotosInfo = function () {
    let me = this;
<<<<<<< HEAD

=======
    let username = $(".username").val();
    me.username = username;
    let us = $(".username").text();
    console.log(username);
>>>>>>> ba2eb02bc4ea49bdb897e1c7bfc07fa7eec6a6d4
    let result = ["photoCount","placeCount","faceCount","photoPath","GPS"];
    $.ajax({
        url:"/SpatialDB-GeoPic-System/initGeoPicDesktopServlet",
        type:"POST",
        data:{
            "data":"",
            "result":result.toString(),
            "username":"db1"
        },
        success:function (res) {
            let json = typeof res=='string'?JSON.parse(res):res;
            if(json.message=="success"){
                let photoCount = json.photoCount;
                let faceCount = json.faceCount;
                let placeCount = json.placeCount;

                //存储了所有的照片的GPS和Path,是个数组，每个数组是jsonObject[{"GPS":[112.3,32.1],"photoPath":"xx"},{}
                let photoPath = json.photoPath;
                me.setInitGeoPicDesktop(photoCount,faceCount,placeCount,photoPath);

            }else{
                console.log(json.message)
            }
        },
        error:function (err) {
            console.log(err)
        },
    });
};

InitGeoPicDesktop.prototype.setInitGeoPicDesktop = function (photoCount,faceCount,placeCount,photoPath) {
    let me = this;
    $(".photoCount").text(photoCount);
    $(".faceCount").text(faceCount);
    $(".placeCount").text(placeCount)
    me.mapControl.initAsPreview(photoPath)
};

