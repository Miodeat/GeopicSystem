/**
 * 百度坐标（BD09）、国测局坐标（火星坐标，GCJ02）、和WGS84坐标系之间的转换的工具
 * 
 * 参考 https://gist.github.com/jp1017/71bd0976287ce163c11a7cb963b04dd8 实现的JavaScript版本
 * @author pcwang
 */

ProjectionUtils = function () {
    var me = this;
    me.x_pi = 3.1415926535897932384626 * 3000.0 / 180.0;
    me.pi = 3.1415926535897932384626;
    me.a = 6378245.0;
    me.ee = 0.00669342162296594323;
};


/**
 * 百度坐标系(BD-09)转WGS坐标
 * 
 * @param lng 百度坐标纬度
 * @param lat 百度坐标经度
 * @return WGS84坐标数组
 */
ProjectionUtils.prototype.bd09towgs84 = function (lng, lat) {
    var me = this;
    var gcj = me.bd09togcj02(lng, lat);
    var wgs84 = me.gcj02towgs84(gcj[0], gcj[1]);
    return wgs84;
};

/**
 * WGS坐标转百度坐标系(BD-09)
 * 
 * @param lng WGS84坐标系的经度
 * @param lat WGS84坐标系的纬度
 * @return 百度坐标数组
 */
ProjectionUtils.prototype.wgs84tobd09 = function (lng, lat) {
    var me = this;
    var gcj = me.wgs84togcj02(lng, lat);
    var bd09 = me.gcj02tobd09(gcj[0], gcj[1]);
    return bd09;
};


/**
 * 火星坐标系(GCJ-02)转百度坐标系(BD-09)
 * 
 * 谷歌、高德——>百度
 * @param lng 火星坐标经度
 * @param lat 火星坐标纬度
 * @return 百度坐标数组
 */
ProjectionUtils.prototype.gcj02tobd09 = function (lng, lat) {
    var me = this;
    var z = Math.sqrt(lng * lng + lat * lat) + 0.00002 * Math.sin(lat * me.x_pi);
    var theta = Math.atan2(lat, lng) + 0.000003 * Math.cos(lng * me.x_pi);
    var bd_lng = z * Math.cos(theta) + 0.0065;
    var bd_lat = z * Math.sin(theta) + 0.006;
    return [bd_lng, bd_lat];
};


/**
 * 百度坐标系(BD-09)转火星坐标系(GCJ-02)
 * 
 * 百度——>谷歌、高德
 * @param bd_lon 百度坐标纬度
 * @param bd_lat 百度坐标经度
 * @return 火星坐标数组
 */
ProjectionUtils.prototype.bd09togcj02 = function (bd_lon, bd_lat) {
    var me = this;
    var x = bd_lon - 0.0065;
    var y = bd_lat - 0.006;
    var z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * me.x_pi);
    var theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * me.x_pi);
    var gg_lng = z * Math.cos(theta);
    var gg_lat = z * Math.sin(theta);
    return [gg_lng, gg_lat];
};

/**
 * WGS84转GCJ02(火星坐标系)
 * 
 * @param lng WGS84坐标系的经度
 * @param lat WGS84坐标系的纬度
 * @return 火星坐标数组
 */
ProjectionUtils.prototype.wgs84togcj02 = function (lng, lat) {
    var me = this;
    if (me.out_of_china(lng, lat)) {
        return [lng, lat];
    }
    var dlat = me.transformlat(lng - 105.0, lat - 35.0);
    var dlng = me.transformlng(lng - 105.0, lat - 35.0);
    var radlat = lat / 180.0 * me.pi;
    var magic = Math.sin(radlat);
    magic = 1 - me.ee * magic * magic;
    var sqrtmagic = Math.sqrt(magic);
    dlat = (dlat * 180.0) / ((me.a * (1 - me.ee)) / (magic * sqrtmagic) * me.pi);
    dlng = (dlng * 180.0) / (me.a / sqrtmagic * Math.cos(radlat) * me.pi);
    var mglat = lat + dlat;
    var mglng = lng + dlng;
    return [mglng, mglat];
};



/**
 * GCJ02(火星坐标系)转GPS84
 * 
 * @param lng 火星坐标系的经度
 * @param lat 火星坐标系纬度
 * @return WGS84坐标数组
 */
ProjectionUtils.prototype.gcj02towgs84 = function (lng, lat) {
    var me = this;
    if (me.out_of_china(lng, lat)) {
        return[lng, lat];
    }
    var dlat = me.transformlat(lng - 105.0, lat - 35.0);
    var dlng = me.transformlng(lng - 105.0, lat - 35.0);
    var radlat = lat / 180.0 * me.pi;
    var magic = Math.sin(radlat);
    magic = 1 - me.ee * magic * magic;
    var sqrtmagic = Math.sqrt(magic);
    dlat = (dlat * 180.0) / ((me.a * (1 - me.ee)) / (magic * sqrtmagic) * me.pi);
    dlng = (dlng * 180.0) / (me.a / sqrtmagic * Math.cos(radlat) * me.pi);
    var mglat = lat + dlat;
    var mglng = lng + dlng;
    return [lng * 2 - mglng, lat * 2 - mglat];
};



/**
 * 纬度转换
 * 
 * @param lng
 * @param lat
 * @return
 */
ProjectionUtils.prototype.transformlat = function (lng, lat) {
    var me = this;
    var ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
    ret += (20.0 * Math.sin(6.0 * lng * me.pi) + 20.0 * Math.sin(2.0 * lng * me.pi)) * 2.0 / 3.0;
    ret += (20.0 * Math.sin(lat * me.pi) + 40.0 * Math.sin(lat / 3.0 * me.pi)) * 2.0 / 3.0;
    ret += (160.0 * Math.sin(lat / 12.0 * me.pi) + 320 * Math.sin(lat * me.pi / 30.0)) * 2.0 / 3.0;
    return ret;
};

/**
 * 经度转换
 * 
 * @param lng
 * @param lat
 * @return
 */
ProjectionUtils.prototype.transformlng = function (lng, lat) {
    var me = this;
    var ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
    ret += (20.0 * Math.sin(6.0 * lng * me.pi) + 20.0 * Math.sin(2.0 * lng * me.pi)) * 2.0 / 3.0;
    ret += (20.0 * Math.sin(lng * me.pi) + 40.0 * Math.sin(lng / 3.0 * me.pi)) * 2.0 / 3.0;
    ret += (150.0 * Math.sin(lng / 12.0 * me.pi) + 300.0 * Math.sin(lng / 30.0 * me.pi)) * 2.0 / 3.0;
    return ret;
};

/**
 * 判断是否在国内，不在国内不做偏移
 * 
 * @param lng
 * @param lat
 * @return
 */
ProjectionUtils.prototype.out_of_china = function (lng, lat) {
    if (lng < 72.004 || lng > 137.8347) {
        return true;
    } else if (lat < 0.8293 || lat > 55.8271) {
        return true;
    }
    return false;
};
