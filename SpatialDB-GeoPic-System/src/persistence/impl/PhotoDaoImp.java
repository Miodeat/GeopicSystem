package persistence.impl;

import domain.PhotoInfo;
import domain.UserInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import persistence.PhotoDao;
import persistence.UtilDao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class PhotoDaoImp implements PhotoDao {
    @Override
    /**
     *  将照片上传至数据库：上传的数据有（
     *      - 结构化地址：例 湖南省长沙市岳麓区清水路
     *      - GPS: 例 POINT(112.3,28.6)
     *      - 拍摄时间: 例 2020-07-13 12:56:56
     *      - 照片标签: 例 '{"自拍","学校"}'
     *      - 照片本地保存路径： 例 photoDataSet\photos1\1.jpg
     *      - 照片周边的poi: 例 '{"中南大学升华公寓31栋","中南大学二食堂"}'
     *      - 照片周边的道路信息: 例'{"清水路"}'
     *      - 照片中包含的人脸信息: 例 '{1,2,3}'
     *     这些信息使用photoInfo获取
     * @param photoInfo
     * @param userInfo 获取用户的数据库名称，用于确定连接哪个数据库
     * @return 返回照片的插入结果：true or false
     */
    public boolean insertPhotoInfo(PhotoInfo photoInfo, UserInfo userInfo) {
        boolean insertPhotoInfoRes = false;
        Connection connection = null;
        try {
            connection = UtilDao.getConnection_UserDB(userInfo.getUserDBName());
            String insertPhotoInfoSql="";
            if(photoInfo.getGeo().equals("")){
                 insertPhotoInfoSql = "insert into photos(" +
                        "formatted_address,takentime,photolabels,photopath,pois,roads,facesid values(" +
                        "?,?,?,?,?,?,?)";
            }else{
                insertPhotoInfoSql = "insert into photos(" +
                        "formatted_address,takentime,geo,photolabels,photopath,pois,roads,facesid values(" +
                        "?,?,st_geomfromtext('"+photoInfo.getGeo()+"',3857),?,?,?,?,?)";
            }
            PreparedStatement preparedStatement = connection.prepareStatement(insertPhotoInfoSql);
            preparedStatement.setString(1,photoInfo.getFormatted_address());
            preparedStatement.setTimestamp(2,photoInfo.getTakenTime());
            preparedStatement.setString(3,photoInfo.getPhotoLabels());
            preparedStatement.setString(4,photoInfo.getPhotoPath());
            preparedStatement.setString(5,photoInfo.getPois());
            preparedStatement.setString(6,photoInfo.getRoads());
            preparedStatement.setString(7,photoInfo.getFacesId());
            int num =0;
            num = preparedStatement.executeUpdate();
            if(num>0){
                insertPhotoInfoRes = true;
            }
            UtilDao.closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return insertPhotoInfoRes;
    }

    /**
     * 时间查询
     * @param startTime ：查询的开始时间 例 2019-09-12 09:08:45
     * @param endTime :查询的开始时间 例 2020-06-12 09:08:45
     * @return 返回查询结果的照片及其对应的经纬度，返回数据格式为
     *  {
     *      "timeQueryRes":[
     *      {
     *          "GPS":"POINT(112.3,28.6)"
     *          "photoPath":"photoDataSet\photos1\1.jpg
     *      },
     *      {
     *          "GPS":"POINT(112.3,28.6)"
     *          "photoPath":"photoDataSet\photos1\1.jpg
     *      }
     *      ]
     *  }
     */
    @Override
    public JSONObject getTimeQueryPhotoPath(String startTime, String endTime, UserInfo userInfo) {
        JSONObject timeQueryRes = new JSONObject();
        JSONArray timeQueryResArray = new JSONArray();
        Connection connection = null;
        try {
            connection = UtilDao.getConnection_UserDB(userInfo.getUserDBName());
            String getTimeQueryPhotoPahtSql = "select photopath,geo from photos " +
                    "where takentime between '"+
                    startTime+"' and '"+endTime+"'";
            PreparedStatement preparedStatement =connection.prepareStatement(getTimeQueryPhotoPahtSql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                JSONObject jsonObject = new JSONObject();
                String path = resultSet.getString(1);
                String geo = resultSet.getString(2);
                jsonObject.put("GPS",geo);
                jsonObject.put("photoPath",path);
                timeQueryResArray.add(jsonObject);
            }
            if(timeQueryResArray.size()>0){
                timeQueryRes.put("message","success");
                timeQueryRes.put("timeQueryRes",timeQueryResArray);
            }else{
                timeQueryRes.put("message","failure");
                timeQueryRes.put("timeQueryRes",timeQueryResArray);
            }
            UtilDao.closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeQueryRes;
    }

    /**
     *
     * @param place :查询的地点
     * @param userInfo: 取用户的数据库名称，用于确定连接哪个数据库
     * @return 返回查询结果的照片及其对应的经纬度，返回数据格式为
     *        {
     *            "placeQueryRes":[
     *            {
     *                "GPS":"POINT(112.3,28.6)"
     *                "photoPath":"photoDataSet\photos1\1.jpg
     *            },
     *            {
     *                "GPS":"POINT(112.3,28.6)"
     *                "photoPath":"photoDataSet\photos1\1.jpg
     *            }
     *            ]
     *        }
     */
    @Override
    public JSONObject getPlaceQueryPotoPath(String place, UserInfo userInfo) {
        Connection connection = null;
        JSONObject placeQueryRes = new JSONObject();
        JSONArray placeQueryResArray = new JSONArray();
//        String place = photoInfo.getFormatted_address();
        try{
            connection = UtilDao.getConnection_UserDB(userInfo.getUserDBName());
            String getPlaceQueryPotoPathSql = "select geo,photopath from  photos where " +
                    "formatted_address like '%"+place+"%' " +
                    " or pois::text like '%"+place+"%' " +
                    " or roads::text like '%"+place+"%'";

            PreparedStatement preparedStatement = connection.prepareStatement(getPlaceQueryPotoPathSql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                JSONObject jsonObject = new JSONObject();
                String geo = resultSet.getString(1);
                String path = resultSet.getString(2);
                jsonObject.put("GPS",geo);
                jsonObject.put("photoPath",path);
                placeQueryResArray.add(jsonObject);
            }

            if(placeQueryResArray.size()>0){
                placeQueryRes.put("message","success");
                placeQueryRes.put("placeQueryRes",placeQueryResArray);
            }else{
                placeQueryRes.put("message","failure");
                placeQueryRes.put("placeQueryRes",placeQueryResArray);
            }

            UtilDao.closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return placeQueryRes;
    }

    /**
     *
     * @param photoInfo :用于获取用户查询的照片标签和人脸id
     *           - 照片标签：'{"自拍","学校"}'
     *           - 人脸id：'{1,2}'
     * @param userInfo: 取用户的数据库名称，用于确定连接哪个数据库
     * @return 返回查询结果的照片及其对应的经纬度，返回数据格式为
     *        {
     *            "semanticQueryRes":[
     *            {
     *                "GPS":"POINT(112.3,28.6)"
     *                "photoPath":"photoDataSet\photos1\1.jpg
     *            },
     *            {
     *                "GPS":"POINT(112.3,28.6)"
     *                "photoPath":"photoDataSet\photos1\1.jpg
     *            }
     *            ]
     *        }
     */
    @Override
    public JSONObject getSemanticQueryPhotoPath(PhotoInfo photoInfo, UserInfo userInfo) {
        Connection connection = null;
        JSONObject semanticQueryRes = new JSONObject();
        JSONArray semanticQueryArray = new JSONArray();

        try{
            connection = UtilDao.getConnection_UserDB(userInfo.getUserDBName());
            String semanticQuerySql = "";
            if(!photoInfo.getPhotoLabels().equals("")){
                if(!photoInfo.getFacesId().equals("")){
                    semanticQuerySql = "select geo,photopath from photos where" +
                            " photolabels @> '"+photoInfo.getPhotoLabels()+"' and " +
                            " facesid @> '"+photoInfo.getFacesId()+"'";

                }else{
                    semanticQuerySql = "select geo,photopath from photos where" +
                            " photolabels @> '"+photoInfo.getPhotoLabels()+"'";

                }
            }else{
                if(!photoInfo.getFacesId().equals("")){
                    semanticQuerySql = "select geo,photopath from photos where" +
                            " facesid @> '"+photoInfo.getFacesId()+"'";

                }
            }

            PreparedStatement preparedStatement = connection.prepareStatement(semanticQuerySql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                JSONObject jsonObject = new JSONObject();
                String geo = resultSet.getString(1);
                String path = resultSet.getString(2);
                jsonObject.put("GPS",geo);
                jsonObject.put("photoPath",path);
                semanticQueryArray.add(jsonObject);
            }

            if(semanticQueryArray.size()>0){
                semanticQueryRes.put("message","success");
                semanticQueryRes.put("placeQueryRes",semanticQueryArray);
            }else {
                semanticQueryRes.put("message","failure");
                semanticQueryRes.put("placeQueryRes",semanticQueryArray);
            }
            UtilDao.closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return semanticQueryRes;
    }

}
