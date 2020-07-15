package persistence.impl;

import domain.UserInfo;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import persistence.PoiDao;
import persistence.UtilDao;

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
}
