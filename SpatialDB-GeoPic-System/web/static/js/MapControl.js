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

MapControl.prototype.initAsPreview = function (photos) {
    let me = this;

    me._loadMarkerCluster(photos);
};

MapControl.prototype.initAsDiscovery = function() {
    let me = this;
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/SpatialDB-GeoPic-System/discoveryServlet",
        data: {
            data: {},
            result: ["Name", "AMapGPS", "TypeCode", "Rating"]
        },
        success: function (res) {
            if(res.message == "success"){
                me._loadPOIMass(res.pois);
            }
            else {
                alert(res.message);
            }
        }
    })
};

MapControl.prototype.addQueryResult = function(startTime, endTime,
                                               loc, photoLabels,
                                               faces, userDbName){
    let me = this;
    let queryParams = {
        startTime: startTime,
        endTime: endTime,
        queryPlace: loc,
        queryPhotoLabel: photoLabels,
        queryFaceLabel: faces
    };
    let returnType = ["photoPath", "AMapGPS"];

    $.ajax({
        type: "POST",
        url: "http://localhost:8080/SpatialDB-GeoPic-System/queryServlet",
        data: {
            data: queryParams,
            result: returnType,
            userDbname: userDbName
        },
        success: function (res) {
            if(res.message == "success") {
                me._loadMarkers(res.photoPathAndGPS);
            }
            else {
                alert(res.message);
            }
        }
    });
};

MapControl.prototype._loadMarkerCluster = function (photos) {
    let me = this;

    let markers = me._constructMarkerArray(photos);
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
        marker.setPosition(e.data.AMapGPS);
        marker.setLabel({content: e.data.Name});
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

MapControl.prototype._loadMarkers = function (photos) {
    let me = this;

    let markers = me._constructMarkerArray(photos);
    me.map.add(markers);
    me.map.setFitView();
};

MapControl.prototype._constructMarkerArray = function (photos) {
    let me = this;
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
        marker.on("click", function (e) {
            let photoPath = e.target.getIcon();
            me._markerClick(photoPath);
        });
        markers.push(marker);
    }
    return markers;
};

MapControl.prototype._markerClick = function (photoPath) {
    let me = this;
    $(".photoDetailModal-content-originPhoto").attr({
        src: photoPath
    });
    let detailAjax = $.ajax({
        type: "POST",
        url: "http://localhost:8080/SpatialDB-GeoPic-System/getPhotoDetailServlet",
        data: {
            photoPath: photoPath,
            dbname: me.dbname
        },
        success: function (res) {
            if(res.message == "success"){
                let detail = res.photoDetail;
                $(".photoInfo-takenTime").val("拍摄时间："
                    + detail.takenTime);
                $(".photoInfo-takenPlace").val("拍摄地点："
                    + detail.formatted_address);
                let faceList = $(".faces-list");
                let facePaths = detail.facePath;
                for(let i = 0, len = facePaths.length; i < len; i++){
                    let facePath = facePaths[i];
                    $("<li>").appendTo(faceList).css({
                        background: "../../../img/" + facePath
                    })
                }
                $(".inputPhotoLabel").val(detail.photoLabels);
            }
            else {
                alert(res.message);
            }
        }
    });

    $.when(detailAjax).done(function () {
        $("#mediumModal").modal("show");
    })
};