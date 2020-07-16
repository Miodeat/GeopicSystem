package persistence.impl;

import domain.UserInfo;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import persistence.PoiDao;
import persistence.UtilDao;

import javax.servlet.jsp.jstl.sql.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PoiDaoImp implements PoiDao {
    @Override
    public JSONObject getPoisInfo() {
        JSONObject allPoisInfo = new JSONObject();
        JSONArray allPoisInfoArray = new JSONArray();
        Connection connection = null;
        try{
            connection = UtilDao.getConnection_BasicGeoDataDB();
            String getAllPoisSql = "select name,st_astext(geo),typecode,rating from pois ";
            PreparedStatement preparedStatement = connection.prepareStatement(getAllPoisSql);
            ResultSet resultSet = preparedStatement.executeQuery();
            JSONObject jsonObject = new JSONObject();
            while(resultSet.next()){
                String Name = resultSet.getString(1);
                String lnglat = resultSet.getString(2);
                String TypeCode =  resultSet.getString(3);
                String Rating =resultSet.getString(4);
                jsonObject.put("Name",Name);
                jsonObject.put("lnglat",lnglat);
                jsonObject.put("TypeCode",TypeCode);
                jsonObject.put("Rating",Rating);
                allPoisInfoArray.add(jsonObject);
                jsonObject.clear();
            }
            if(allPoisInfoArray.size()>0){
                allPoisInfo.put("message","success");
            }else{
                allPoisInfo.put("message","failure");
            }
            allPoisInfo.put("poiDetail",allPoisInfoArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(allPoisInfo);
        return allPoisInfo;
    }

    @Override
    public JSONObject getPhotosOfPoi(String poiGPS, UserInfo userInfo) {
        JSONObject photosOfpoiRes = new JSONObject();
        JSONArray photosOfpoiResArray = new JSONArray();
        Connection connection = null;
        try{
            connection = UtilDao.getConnection_UserDB(userInfo.getUserDBName());
            String getPhotoOfPoisSql = "select * from photos" +
                    "  where ST_Distance(st_transform(st_setsrid(geo,4326),3857), st_transform(st_setsrid('"+
                    poiGPS+"'::geometry,4326),3857))<60000";
            System.out.println(getPhotoOfPoisSql);

            PreparedStatement preparedStatement = connection.prepareStatement(getPhotoOfPoisSql);
            ResultSet resultSet = preparedStatement.executeQuery();
            JSONObject jsonObject = new JSONObject();

            while (resultSet.next()){
                String photoPath = resultSet.getString("photopath");
                String AMapGPS = resultSet.getString("geo");
                String formatted_address = resultSet.getString("formatted_address");
                jsonObject.put("photoPath",photoPath);
                jsonObject.put("AMapGPS",AMapGPS);
                jsonObject.put("formatted_address",formatted_address);
                photosOfpoiResArray.add(jsonObject);
                jsonObject.clear();
            }
            if(photosOfpoiResArray.size()>0){
                photosOfpoiRes.put("message","success");

            }else{
                photosOfpoiRes.put("message","failure");
            }
            System.out.println(photosOfpoiResArray);
            photosOfpoiRes.put("photosOfPoi",photosOfpoiResArray);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return photosOfpoiRes;
    }
}
