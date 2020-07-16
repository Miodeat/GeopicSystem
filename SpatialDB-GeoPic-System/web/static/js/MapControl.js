MapControl = function (options) {
    let me = this;

    me.ops = options;
    me._init();
};

MapControl.prototype._init = function () {
    let me = this;

    me.div = $("#" + me.ops.div);

    me.map = new AMap.Map(me.ops.div, {
        center: [112.3, 28.9],
        zoom: 4
    })
};

MapControl.prototype.initAsPreview = function (photos, dbName) {
    let me = this;

    me._loadMarkerCluster(photos, dbName);
};

MapControl.prototype.initAsDiscovery = function() {
    let me = this;
    $.ajax({
        type: "POST",
        url: "/SpatialDB-GeoPic-System/discoveryServlet",
        data: {
<<<<<<< HEAD
            data: {},
            result: ["Name", "AMapGPS", "TypeCode", "Rating"]
=======
            "data": {},
            "result": ["Name", "lnglat", "TypeCode", "Rating"]
>>>>>>> 213a161e76707eba937e0ad8e1a1b9276f3f63e2
        },
        success: function (res) {
            let json = typeof res=='string'?JSON.parse(res):res;
            if(json.message == "success"){
                me._loadPOIMass(json.poiDetail);
            }
            else {
                alert(json.message);
            }
        }
    })
};

MapControl.prototype.addQueryResult = function(startTime, endTime,
                                               loc, photoLabels,
                                               faces, userDbName){
    let me = this;
    let queryParams = {
        "startTime": startTime,
        "endTime": endTime,
        "queryPlace": loc,
        "queryPhotoLabel": photoLabels,
        "queryFaceLabel": faces
    };
    let returnType = ["photoPath", "AMapGPS"];

    $.ajax({
        type: "POST",
        url: "/SpatialDB-GeoPic-System/queryServlet",
        data: {
            "data": queryParams,
            "result": returnType,
            "userDbname": userDbName
        },
        success: function (res) {
            let json = typeof res=='string'?JSON.parse(res):res;
            if(json.message == "success") {
                me._loadMarkers(json.photoPathAndGPS, userDbName);
            }
            else {
                alert(json.message);
            }
        }
    });
};

MapControl.prototype._loadMarkerCluster = function (photos, dbName) {
    let me = this;

    let markers = me._constructMarkerArray(photos, dbName);
    me.markerCluster = new AMap.MarkerClusterer(me.map, markers, {
        zoomOnClick: false,
        renderClusterMarker: function (context) {
            me._clusterRenderer(context);
        }
    })
};

MapControl.prototype._loadPOIMass = function (POIs) {
    let me = this;

    let style = {
        url: 'https://a.amap.com/jsapi_demos/static/images/mass2.png',
        anchor: new AMap.Pixel(3, 3),
        size: new AMap.Size(5, 5)
    };

    let mass = new AMap.MassMarks(POIs, {
        opacity: 0.8,
        zIndex: 111,
        cursor: 'pointer',
        style: style
    });

    let marker = new AMap.Marker({content: ' ', map: me.map});

    mass.on('mouseover', function (e) {
<<<<<<< HEAD
        marker.setPosition(e.data.AMapGPS);
        marker.setLabel({content: e.data.Name});
=======
        marker.setPosition(e.data.lnglat);
        marker.setLabel({content: e.data.Name + ",评分:" + e.data.Rating});
        marker.show();
    });

    mass.on('mouseout', function (e) {
        marker.hide();
    });

    mass.on('mousedown', function (e) {
        me._getNearbyPhoto(e.data.lnglat);
>>>>>>> 213a161e76707eba937e0ad8e1a1b9276f3f63e2
    });

    mass.setMap(me.map);

};

MapControl.prototype._clusterRenderer = function (context) {
    let container = document.createElement("div");
    container.style.width = "34px";

    let label = document.createElement("div");
    label.style.width = "20px";
    label.style.height = "20px";
    label.style.borderRadius = "10px";
    label.style.backgroundColor = "rgb(200, 0, 0)";
    label.style.position = "absolute";
    label.style.top = "-9px";
    label.style.left = "23px";
    label.style.textAlign = "center";
    label.style.fontSize = "0.7em";
    label.style.fontFamily = '"Times New Roman", Times, serif';
    label.style.fontWeight = "900";
    label.style.color = "white";

    if(context.count > 99){
        label.innerHTML = "99+";
    }
    else{
        label.innerHTML = context.count;
    }
    let imgDiv = document.createElement("div");
    imgDiv.style.width = "34px";
    imgDiv.style.border = "2px solid black";
    imgDiv.style.borderRadius = "3px";
    imgDiv.style.backgroundColor = "rgba(255, 255, 255, 0.3)";

    let img1 = document.createElement("img");
    img1.setAttribute("src", context.markers[0].getIcon());
    img1.setAttribute("width", "30");
    img1.style.margin = "1px 0 1px 0";
    img1.style.float = "left";

    let img2 = document.createElement("img");
    img2.setAttribute("src", context.markers[1].getIcon());
    img2.setAttribute("width", "30");
    img2.style.margin = "1px 0 1px 0";
    img1.style.float = "left";

    imgDiv.appendChild(img1);
    imgDiv.appendChild(img2);
    container.appendChild(label);
    container.appendChild(imgDiv);

    context.marker.setAnchor("bottom-center");
    context.marker.setOffset(new AMap.Pixel(0,0));
    context.marker.setContent(container);
};

MapControl.prototype._loadMarkers = function (photos, dbName) {
    let me = this;

    let markers = me._constructMarkerArray(photos, dbName);
    me.map.add(markers);
    me.map.setFitView();
};

<<<<<<< HEAD
MapControl.prototype._constructMarkerArray = function (photos) {
=======
MapControl.prototype._constructMarkerArray = function (photos, dbName) {
    let me = this;
>>>>>>> 213a161e76707eba937e0ad8e1a1b9276f3f63e2
    let markers = [];
    for(let i = 0, len = photos.length; i < len; i++){
        let photo = photos[i];
        let lngLat = new AMap.LngLat(photo.AMapGPS[0], photo.AMapGPS[1]);
        let content = document.createElement("img");
        content.setAttribute("src", "../../../img/" + photo.photoPath);
        content.setAttribute("width", "30px");
        content.style.border = "2px solid black";
        content.style.borderRadius = "3px";
        let marker = new AMap.Marker({
            position: lngLat,
            anchor: "bottom-center",
            offset: new AMap.Pixel(0,0),
            icon: "../../../img/" + photo.photoPath,
            content: content
        });
<<<<<<< HEAD
        markers.push(marker);
    }
    return markers;
=======
        marker.on("click", function (e) {
            let photoPath = e.target.getIcon();
            me._markerClick(photoPath, dbName);
        });
        markers.push(marker);
    }
    return markers;
};

MapControl.prototype._markerClick = function (photoPath, dbName) {
    let me = this;
    $(".photoDetailModal-content-originPhoto").attr({
        src: photoPath
    });
    let dbPhotoName = photoPath.slice(13);
    console.log(dbPhotoName);
    let detailAjax = $.ajax({
        type: "POST",
        url: "/SpatialDB-GeoPic-System/getPhotoDetailServlet",
        data: {
            "photoPath": dbPhotoName,
            "userDbname": dbName,
        },
        success: function (res) {
            let json = typeof res=='string'?JSON.parse(res):res;
            if(json.message == "success"){
                let detail = json.photoDetail;
                $(".takenTime").val(detail.takenTime);
                $(".takenPlace").val(detail.formatted_address);
                let faceList = $(".faces-list");
                let facesPaths = detail.facePath;
                for(let i = 0, len = facesPaths.length; i < len; i++){
                    let facePath = facesPaths[i];
                    let faceImgUrl = "../../../img/"
                        + facePath.facePath.replace(/\\/g, "/");
                    $("<li>").appendTo(faceList).css({
                        background:  faceImgUrl
                    })
                }
                $(".inputPhotoLabel").val(detail.photoLabels);
            }
            else {
                alert(json.message);
            }
        }
    });

    $.when(detailAjax).done(function () {
        $("#mediumModal").modal("show");
    })
};

MapControl.prototype._getNearbyPhoto = function (lnglat) {
    let me = this;
    $.ajax({
        type: "POST",
        data: {
            "data": {
                "lnglat": lnglat,
            },
            "result": ["photoPath", "AMapGPS"],
        },
        success: function (res) {
            if(res.message == "success"){
                me._loadMarkers(res.photoPathAndGPS);
            }
            else{
                alert(res.message);
            }
        }

    })
>>>>>>> 213a161e76707eba937e0ad8e1a1b9276f3f63e2
};