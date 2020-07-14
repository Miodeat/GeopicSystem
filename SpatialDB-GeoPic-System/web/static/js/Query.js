// 向后台发起查询数据库请求
//
// @param query_params: 向数据库发送查询语句中的筛选条件
//                      json Object, 形如:
//                      {
//                          photoLabels: "学校",
//                          faceLabel: "彭举/李伟霞"
//                      }
// @param return_type: 查询应当返回的信息
//                     json array, 形如:
//                     ["photoPath", "AMapGPS"]
// @param user_dbname: 当前用户的数据库名
// @return json列表, 包含多个json Object
//         形如:
//         [
//              {
//                  photoPath: "./img.jpg",
//                  AMapGPS: [23.4, 116.3]
//              }
//         ]
function Query(queryParams, returnType, userDbName) {
    let result = [];
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/SpatialDB_GeoPic_System/queryServlet",
        data: {
            data: queryParams,
            result: returnType,
            username: userDbName
        },
        success: function (res) {
            result = res;
        }
    });
    return result;
}
