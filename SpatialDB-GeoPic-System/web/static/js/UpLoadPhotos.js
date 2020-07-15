
// let index = 0;//每张图片的下标，
let formatted_address ="";
let takenTime = "";
let AMapGPS="";
// let photoData = "";
let api_key = "ms3MvC2UjwJBlSs5wNTVj-3SXPPAURq3";
let api_secret = "P6wgCmBYbeFGRG76cwTOTe6k2V5jS1vY";
let faceset_token = "2d7ddb438c720087b2d4e95c22535d41";
let outer_id = "GeoPic";
let pois = "";
let roads = "";
UpLoadPhotos = function (options) {
    let me = this;
    me.SearchRes = [];

};

/**
 * 上传图片的入口
 */
UpLoadPhotos.prototype.startToUpload = function () {
    let me = this;
    me.passPhotoData();
};

/**
 *
 */
UpLoadPhotos.prototype.passPhotoData = function () {
    let me = this;
    me.isExist = false;
    let files = document.getElementById("input_upload_driver").files;

    for(let i = 0;i<files.length;i++){
        let file =files[i];
        let r =new FileReader();
        r.readAsDataURL(file);
        r.onloadend= function (ev){
           let photoData = this.result;
           var fileNameAndUserDb = file.name+","+"db1"
           let photoDataAndFileName = photoData.replace("data:image/jpeg;base64",fileNameAndUserDb);
            photoDataAndFileName = photoDataAndFileName.replace("data:image/png;base64",fileNameAndUserDb);
            photoDataAndFileName = photoDataAndFileName.replace("data:image/gif;base64",fileNameAndUserDb);
            photoDataAndFileName = photoDataAndFileName.replace("data:image/bmp;base64",fileNameAndUserDb);
            console.log(photoDataAndFileName);
            $.ajax({
                url:"/SpatialDB-GeoPic-System/GetPhotoDataServlet",
                type:"POST",
                data:photoDataAndFileName,
                async:false,
                contentType : false,
                processData : false,  //必须false才会避开jQuery对 formdata 的默认处理
                cache : false,
                success:function (res) {
                    let json = typeof res=='string'?JSON.parse(res):res;
                    console.log(json)
                    //如果数据库中没有这张照片，那么将这张照片上传到数据库，同时检测人脸信息
                    if((json.message)=="success"){
                        me.uploadPhotoInfoToDB(file);
                        me.isExist = true;
                    }else{
                        console.log(json.message);

                    }
                },
                error:function (err) {
                    console.log()
                }
            });
        }
    }
};

/**
 * 将照片上传至数据库
 * @param file  获取input中的file
 */
UpLoadPhotos.prototype.uploadPhotoInfoToDB = function (file) {
    let me = this;
    me.getExifData(file);
};

UpLoadPhotos.prototype.getExifData = function (file) {
    let me = this;
    let location = "";
    EXIF.getData(file,function () {
        let exifData = EXIF.pretty(this);
        console.log(exifData);
        if(exifData){
            let lat = EXIF.getTag(this, "GPSLatitude");
            let lon = EXIF.getTag(this, "GPSLongitude");
            takenTime = EXIF.getTag(this,"DateTime");
            if(!takenTime){
                takenTime = "9999-01-01 00:00:00";
            }
            console.log(takenTime);
            if(lat&&lon){
                let lat1 = parseFloat(lat[0])+parseFloat(lat[1]/60)+parseFloat(lat[2]/3600);
                let lon1 =  parseFloat(lon[0])+parseFloat(lon[1]/60)+parseFloat(lon[2]/3600);
                location = lon1+","+lat1;
                AMapGPS = 'POINT('+lon1+' '+lat1+')';
                console.log(AMapGPS);
                $.ajax({
                    type:"get",
                    url:"https://restapi.amap.com/v3/geocode/regeo",
                    data:{
                        key:"8b6b3261c4d70b409feaa273ada901f2",
                        location:location,
                        extensions:'all',
                    },
                    async:true,
                    success:function (res) {
                        let json = typeof res  =='string'?JSON.parse(res):res;
                        let poisName = [];
                        let roadName = [];
                        if(typeof json.regeocode !="undefined"){
                            if (json.regeocode.length>0){
                                pois = json.regeocode.pois;
                                roads = json.regeocode.roads;
                                formatted_address = json.regeocode.formatted_address;

                                if(pois.length>0){
                                    for(let j = 0;j<pois.length;j++){
                                        poisName[j] = pois[j].name;
                                    }
                                }
                                if(roads.length>0){
                                    for(let k = 0;k<roads.length;k++){
                                        roadName[k] = roads[k].name;
                                    }
                                }
                            }
                        }

                        // PhotoWall.prototype.passPhotoInfo(file);
                    }
                });
            }else{
                formatted_address = "";
                // PhotoWall.prototype.passPhotoInfo(file);
            }


        }else {
            takenTime = "9999-01-01 00:00:00";
            geo = "";
            formatted_address = "";
            // PhotoWall.prototype.passPhotoInfo(file);

        }
    });
};


UpLoadPhotos.prototype.getFaceInfo = function (file) {
    let  me = this;
    me.faces = [];
    let data = new FormData();
    data.append('api_key', "ms3MvC2UjwJBlSs5wNTVj-3SXPPAURq3");
    data.append('api_secret', "P6wgCmBYbeFGRG76cwTOTe6k2V5jS1vY");
    data.append('image_file', file)
    let detectData ='api_key=ms3MvC2UjwJBlSs5wNTVj-3SXPPAURq3&api_secret=P6wgCmBYbeFGRG76cwTOTe6k2V5jS1vY'+
        '&image_file='+file;
    $.ajax({
        url:"https://api-cn.faceplusplus.com/facepp/v3/detect",
        type:"POST",
        data:data,
        cache: false,
        processData: false,
        contentType: false,
        // dataType:"json",
        success:function (res) {
            console.log(res);
            let json = typeof res=='string'?JSON.parse(res):res;
            let face_num = json.face_num;
            let faces = json.faces;
            me.faces = [];
            me.face_tokens=[];
            if(face_num>0){
                me.searchRes = [];
                for(let i = 0;i<face_num;i++){

                    //从FaceSet集合中搜索与检测出的人脸最相似的照片，若相似度大于75，我们认为这是同一个人

                    //这里我采用的同步方式，不知道后面后不会造成堵塞，可能会的吧
                    me.searchFace(faces[i].face_token);
                    if(me.searchRes.length>0){
                        console.log("faceset中已有此人物")
                        //me.face_tokens.push(faces[i].face_token);
                    }else{
                        me.addFace_tokenToFaceSet(faces[i].face_token);
                        me.faces.push(faces[i]);
                    }
                }
                console.log(me.searchRes)
                if(me.faces.length>0||me.searchRes.length>0){
                    let data = {};
                    data["faces"] = me.faces;
                    // me.uploadFaceInfo(data,file,me.searchRes);
                }
            }else{
                console.log("no faceInfo")
            }
        },
        error:function (err) {
            console.log(err)
        }
    });
};

/**
 * 用于人脸检测
 * @param face_token 人脸的标识face_token
 */
UpLoadPhotos.prototype.searchFace = function(face_token){
    let me = this;
    let searchRes = [];
    let data = new FormData();
    data.append("api_key",api_key);
    data.append("api_secret",api_secret);
    data.append("face_token",face_token);
    data.append("faceset_token",faceset_token);
    $.ajax({
        url:"https://api-cn.faceplusplus.com/facepp/v3/search",
        type:"POST",
        data:data,
        cache:false,
        processData:false,
        contentType:false,
        async:false,
        success:function (res) {
            let json = typeof res=='string'?JSON.parse(res):res;
            console.log(json);
            for(let i = 0;i<json.results.length;i++){
                let confidence = json.results[i].confidence;
                let face_token = json.results[i].face_token.toString();
                //这里数值比较的方式需要注意，不能直接比较，需要使用eval函数，置信度75不知道低不
                if(eval(confidence)>eval(75)){
                    me.searchRes.push(face_token);
                    break;
                }
            }
            return me.searchRes;
        },
        error:function (err) {
            console.log(err)
            return err;
        }
    });
    //return searchRes;
}

/**
 * 将人脸的face_token添加至FaceSet
 * @param face_token :检测出的图片中人脸的唯一标识face_token
 */
UpLoadPhotos.prototype.addFace_tokenToFaceSet = function(face_token){
    let me = this;
    let data = new FormData();
    data.append("api_key",api_key);
    data.append("api_secret",api_secret);
    data.append("faceset_token",faceset_token);
    data.append("face_tokens",face_token);
    $.ajax({
        url:"https://api-cn.faceplusplus.com/facepp/v3/faceset/addface",
        type:"POST",
        data:data,
        cache:false,
        processData:false,
        contentType:false,
        success:function (res) {
            console.log(res)
        },
        error:function (err) {
            console.log(err)
        }
    });
};



