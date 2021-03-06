package persistence.impl;

import domain.PhotoInfo;
import domain.UserInfo;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import persistence.PhotoDao;
import persistence.UtilDao;


import javax.servlet.jsp.jstl.sql.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;


public class PhotoDaoImp implements PhotoDao {
//    private String[]specialCity={"锡林郭勒盟","阿拉善盟","延边朝鲜族自治州","大兴安岭地区","恩施土家族苗族自治州","湘西土家族苗族自治州",
//    "阿坝藏族羌族自治州","甘孜藏族自治州","凉山彝族自治州","黔西南布依族苗族自治州","黔东南苗族侗族自治州","黔南布依族苗族自治州",
//            "楚雄彝族自治州","红河哈尼族彝族自治州","文山壮族苗族自治州","西双版纳傣族自治州","大理白族自治州","德宏傣族景颇族自治州",
//            "怒江傈僳族自治州","迪庆藏族自治州","临夏回族自治州","甘南藏族自治州","海北藏族自治州","黄南藏族自治州","海南藏族自治州","果洛藏族自治州",
//            "玉树藏族自治州", "海西蒙古族藏族自治州", "阿里地区","阿克苏地区","喀什地区","和田地区","塔城地区", "阿勒泰地区"};
    private String []spectilCity= {"锡林郭勒盟","阿拉善盟"};


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

//        System.out.println(userInfo.getUserDBName());
        System.out.println(photoInfo.getRoads()+photoInfo.getPois()+photoInfo.getGeo()+photoInfo.getPhotoLabels());
        boolean insertPhotoInfoRes = false;
        Connection connection = null;
        try {
            connection = UtilDao.getConnection_UserDB(userInfo.getUserDBName());
            String insertPhotoInfoSql="";
            if(photoInfo.getGeo().equals("")){
                 insertPhotoInfoSql = "insert into photos(" +
                        "formatted_address,takentime,photolabels,photopath,pois,roads,facesid) values (?,?,'"+photoInfo.getPhotoLabels()+"',?,'"+photoInfo.getPois()+"','"+photoInfo.getRoads()+"','"+photoInfo.getFacesId()+"')";
                 System.out.println(insertPhotoInfoSql);
            }else{
                insertPhotoInfoSql = "insert into photos(" +
                        "formatted_address,takentime,geo,photolabels,photopath,pois,roads,facesid) values (?,?,st_geomfromtext('"+photoInfo.getGeo()+"',3857),'"+ photoInfo.getPhotoLabels()+"',?,'"+photoInfo.getPois()+"','"+photoInfo.getRoads()+"','"+photoInfo.getFacesId()+"')";
                System.out.println(insertPhotoInfoSql);
            }
            PreparedStatement preparedStatement = connection.prepareStatement(insertPhotoInfoSql);
            preparedStatement.setString(1,photoInfo.getFormatted_address());
            preparedStatement.setTimestamp(2,photoInfo.getTakenTime());
//            preparedStatement.setString(3,photoInfo.getPhotoLabels());
            preparedStatement.setString(3,photoInfo.getPhotoPath());
//            preparedStatement.setString(4,photoInfo.getPois());
//            preparedStatement.setString(5,photoInfo.getRoads());
//            preparedStatement.setString(7,photoInfo.getFacesId());
            int num =0;
//            preparedStatement.
            num = preparedStatement.executeUpdate();
            System.out.println(num);
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
     *          "AMapGPS":"POINT(112.3,28.6)"
     *          "photoPath":"photoDataSet\photos1\1.jpg
     *      },
     *      {
     *          "AMapGPS":"POINT(112.3,28.6)"
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
                jsonObject.put("AMapGPS",geo);
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
     *                "AMapGPS":[112.3,44.2]
     *                "photoPath":"photoDataSet\photos1\1.jpg
     *            },
     *            {
     *                "AMapGPS":[112.3,44.2]
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
                jsonObject.put("AMapGPS",geo);
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
     *            "data":[
     *            {
     *                "AMapGPS":[112.3,44.2]
     *                "photoPath":"photoDataSet\photos1\1.jpg
     *            },
     *            {
     *                "AMapGPS":[112.3,44.2]
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
                jsonObject.put("AMapGPS",geo);
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

    @Override
    public JSONObject initGeoPicDesktop(PhotoInfo photoInfo, UserInfo userInfo) {
        Connection connection = null;
        JSONObject initGeoPicDesktopRes = new JSONObject();
        JSONArray allphotoPathArray = new JSONArray();
        try{
            connection = UtilDao.getConnection_UserDB(userInfo.getUserDBName());
            String initGeoPicDesktopSql = "select photopath,st_astext(geo) ,formatted_address from photos limit 500 ";
            PreparedStatement preparedStatement = connection.prepareStatement(initGeoPicDesktopSql);
            ResultSet resultSet = preparedStatement.executeQuery();
            HashMap<String,String> getAllCities = new HashMap<>();
            int photoCount = 0;
            while (resultSet.next()){
                String path = resultSet.getString(1);
                String GPS = resultSet.getString(2);
                String formatted_address = resultSet.getString("formatted_address");
                formatted_address = getCity(formatted_address);
                if(!formatted_address.equals("")){
                    getAllCities.put(formatted_address,formatted_address);
                }
                JSONObject jsonObject  = new JSONObject();
                JSONArray jsonGPSArray = new JSONArray();
                jsonGPSArray = getGPSArray(GPS);
                jsonObject.put("AMapGPS",jsonGPSArray);
                jsonObject.put("photoPath",path);
                allphotoPathArray.add(jsonObject);
                photoCount++;
            }

            if(allphotoPathArray.size()>0){
                initGeoPicDesktopRes.put("message","success");

            }else {
                initGeoPicDesktopRes.put("message","failure");
            }

            initGeoPicDesktopRes.put("photoCount",photoCount);
            initGeoPicDesktopRes.put("placeCount",getAllCities.size());
            initGeoPicDesktopRes.put("photoPathAndGPS",allphotoPathArray);
            System.out.println(initGeoPicDesktopRes.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return initGeoPicDesktopRes;
    }

    @Override
    public JSONObject getintegratedQueryPhotoPath(String starttime, String endTime,
                                                  String address, String photolabel, String facelabel,String userDbname) {
        JSONObject integreatedQueryPhotoPathRes = new JSONObject();
        JSONArray integreatedQueryArray = new JSONArray();
        Connection connection = null;
        try {
            connection = UtilDao.getConnection_UserDB(userDbname);
            String integratedQuerySql = "select photopath,st_astext(geo) from photos ";
            String sqlTemp = getIntegratedQuerySql(starttime,endTime,address,photolabel,facelabel);
            if(!sqlTemp.equals("")){
                integratedQuerySql += " where "+sqlTemp;
                System.out.println(integratedQuerySql);
            }

            PreparedStatement preparedStatement = connection.prepareStatement(integratedQuerySql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String path = resultSet.getString(1);
                String AMapGPS = resultSet.getString(2);
                JSONArray jsonGPSArray = new JSONArray();
                jsonGPSArray = getGPSArray(AMapGPS);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("AMapGPS",jsonGPSArray);
                jsonObject.put("photoPath",path);
                integreatedQueryArray.add(jsonObject);
            }
            if(integreatedQueryArray.size()>0){
                integreatedQueryPhotoPathRes.put("message","success");
            }else{
                integreatedQueryPhotoPathRes.put("message","failure");
            }
            integreatedQueryPhotoPathRes.put("photoPathAndGPS",integreatedQueryArray);
            System.out.println(integreatedQueryPhotoPathRes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return integreatedQueryPhotoPathRes;
    }

    @Override
    public JSONObject getPhotoDetailAccordingPhotoPath(String photoPath,UserInfo userInfo) {
        JSONObject getPotoDeatilRes = new JSONObject();
        JSONArray getPotoDeatilResArray = new JSONArray();
        Connection connection = null;
        try{
            connection = UtilDao.getConnection_UserDB(userInfo.getUserDBName());
            String getPhotoDeatilSql = "select takentime,formatted_address,st_astext(geo),facesid,photolabels" +
                    " from photos where photopath = '"+photoPath+"'";
            System.out.println(getPhotoDeatilSql);
            PreparedStatement preparedStatement = connection.prepareStatement(getPhotoDeatilSql);
            ResultSet resultSet = preparedStatement.executeQuery();
            JSONObject jsonObject = new JSONObject();
            while (resultSet.next()){
                String formatted_address = resultSet.getString(2);
                String takentime = resultSet.getTimestamp(1).toString();;
                String GPS = resultSet.getString(3);
                Array facesId = resultSet.getArray(4);
                Array photoLabels = resultSet.getArray(5);
                JSONArray AMapGPS = new JSONArray();
                if(GPS!=null&&!GPS.equals("")){
                    AMapGPS  = getGPSArray(GPS);
                }
                jsonObject.put("formatted_address",formatted_address);
                jsonObject.put("takenTime",takentime);
                jsonObject.put("AMapGPS",AMapGPS.toString());
                jsonObject.put("facesId",facesId.toString());
                jsonObject.put("photoLabels",photoLabels.toString());
                getPotoDeatilResArray.add(jsonObject);
                jsonObject.clear();
            }
            if(getPotoDeatilResArray.size()>0){
                getPotoDeatilRes.put("message","success");
            }else{
                getPotoDeatilRes.put("message","failure");
            }
            getPotoDeatilRes.put("photoDetail",getPotoDeatilResArray);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return getPotoDeatilRes;
    }

    @Override
    public int getPhotoIdAcoordintPhotoPath(PhotoInfo photoInfo, UserInfo userInfo) {
        int photo_id = 0;
        Connection conn = null;
        try{
            System.out.println("photo Pathinget "+photoInfo.getPhotoId());
            String getPhotoIdAccordingPhotoPathSql = "select photo_id from photos " +
                    "where photopath = '"+photoInfo.getPhotoPath()+"'";
            conn = UtilDao.getConnection_UserDB(userInfo.getUserDBName());
            PreparedStatement preparedStatement = conn.prepareStatement(getPhotoIdAccordingPhotoPathSql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                photo_id = resultSet.getInt("photo_id");
            }
            UtilDao.closeConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return photo_id;
    }

    @Override
    public boolean insertPhotoFaceId(PhotoInfo photoInfo, UserInfo userInfo) {
        boolean insertPhotoFaceIdRes = false;
        Connection conn= null;
        try{
            conn = UtilDao.getConnection_UserDB(userInfo.getUserDBName());
            System.out.println(photoInfo.getFacesId());
            String insertPhotoFaceIdSql = "update photos set facesid = '{"+photoInfo.getFacesId()+"}' where photo_id = "+photoInfo.getPhotoId();
            System.out.println(insertPhotoFaceIdSql);
            PreparedStatement preparedStatement = conn.prepareStatement(insertPhotoFaceIdSql);
            int num = preparedStatement.executeUpdate();
            System.out.println("人脸id"+num);
            if(num>0){
                insertPhotoFaceIdRes = true;
            }
            UtilDao.closeConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return insertPhotoFaceIdRes;
    }

    @Override
    public  JSONObject updataPhotoInfo(String takenTime,String formatted_address, String photoLabels,String photoPath,int sharedFlag,UserInfo userInfo) {
        JSONObject updatePhotoInfoRes = new JSONObject();
        Connection connection = null;
        try {
            connection = UtilDao.getConnection_UserDB(userInfo.getUserDBName());
            String updatePhotoInfoSql = "";
            if(!formatted_address.equals("")){
                if(!photoLabels.equals("")){
                    if(!takenTime.equals("")){
                        updatePhotoInfoSql = " update photos set formatted_address ='"+formatted_address+"',photolabels ='{"+photoLabels+"}'," +
                                "takentime = '"+takenTime+"'";
                    }else{
                        updatePhotoInfoSql = " update photos set formatted_address ='"+formatted_address+"',photolabels ='{"+photoLabels+"}'";
                    }
                }else{
                    if(!takenTime.equals("")){
                        updatePhotoInfoSql = " update photos set formatted_address ='"+formatted_address+"',takentime = '"+takenTime+"'";
                    }else{
                        updatePhotoInfoSql = " update photos set formatted_address ='"+formatted_address+"'";
                    }
                }
            }else{
                if(!photoLabels.equals("")){
                    if(!takenTime.equals("")){
                        updatePhotoInfoSql = " update photos set photolabels ='{"+photoLabels+"}'," +
                                "takentime = '"+takenTime+"'";
                    }else{
                        updatePhotoInfoSql = " update photos set photolabels ='{"+photoLabels+"}'";
                    }
                }else{
                    if(!takenTime.equals("")){
                        updatePhotoInfoSql = " update photos set takentime = '"+takenTime+"'";
                    }else{
                        return updatePhotoInfoRes;
                    }
                }
            }

            updatePhotoInfoSql+=" where photoPath '"+photoPath+"'";
            System.out.println(updatePhotoInfoSql);

//            if(sharedFlag==1){
//                updatePhotoInfoSql += ", sharedflag = true where photopath = '"+photoPath+"'";
//            }

            PreparedStatement preparedStatement = connection.prepareStatement(updatePhotoInfoSql);
            int num = 0;
            num = preparedStatement.executeUpdate();
            if(num>0){
                updatePhotoInfoRes.put("message","success");
            }else{
                updatePhotoInfoRes.put("message","failure");
            }
            UtilDao.closeConnection(connection);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return updatePhotoInfoRes;
    }


    private  String getIntegratedQuerySql(String starttime, String endTime,
                                          String address, String photolabel, String facesid){
        String integratedQuerySql = "";
        if(photolabel.equals("\"\"")){
            photolabel = "";
        }

        String photolabels = photolabel;
        System.out.println(facesid);
        if(facesid.indexOf("-1")>-1){
            facesid="";
        }
        System.out.println(photolabels);
        if((!starttime.equals(""))&&(!endTime.equals(""))){
            if(!address.equals("")){
                if(!photolabel.equals("")){
                    if(!facesid.equals("")){
                        integratedQuerySql = " (takentime between '"+starttime+"' and '"+endTime+"') " +
                                " and (formatted_address like '%"+address+"%' or pois::text like '%"+address+"%' or roads::text like '%"+address+"%') " +
                                " and (photolabels @>'{"+photolabels+"}') " +
                                " and (facesid @>'{"+facesid+"}')";
                    }else {

                        integratedQuerySql = " (takentime between '"+starttime+"' and '"+endTime+"') " +
                                " and (formatted_address like '%"+address+"%' or pois::text like '%"+address+"%' or roads::text  like '%"+address+"%') " +
                                " and (photolabels @>'{"+photolabels+"}') ";
                    }
                }else{
                    if(!facesid.equals("")){
                        integratedQuerySql = " (takentime between '"+starttime+"' and '"+endTime+"') " +
                                " and (formatted_address like '%"+address+"%' or pois::text like '%"+address+"%' or roads::text  like '%"+address+"%') " +
                                " and (facesid @>'{"+facesid+"}')";
                    }else{
                        integratedQuerySql = " (takentime between '"+starttime+"' and '"+endTime+"') " +
                                " and (formatted_address like '%"+address+"%' or pois::text like '%"+address+"%' or roads::text  like '%"+address+"%') ";
                    }

                }
            }else {
                if(!photolabel.equals("")){
                    if(!facesid.equals("")){
                        integratedQuerySql = " (takentime between '"+starttime+"' and '"+endTime+"') " +
                                " and (photolabels @>'{"+photolabels+"}') " +
                                " and (facesid @>'{"+facesid+"}')";
                    }else{
                        integratedQuerySql = " (takentime between '"+starttime+"' and '"+endTime+"') " +
                                " and (photolabels @>'{"+photolabels+"}') ";
                    }
                }else{
                    if(!facesid.equals("")){
                        integratedQuerySql = " (takentime between '"+starttime+"' and '"+endTime+"') " +
                                " and (facesid @>'{"+facesid+"}')";
                    }else{

                    }
                    integratedQuerySql = " (takentime between '"+starttime+"' and '"+endTime+"') ";
                }
            }
        }else{
            if(!address.equals("")){
                if(!photolabel.equals("")){
                    if(!facesid.equals("")){
                        integratedQuerySql = " (formatted_address like '%"+address+"%' or pois::text like '%"+address+"%' or roads::text like '%"+address+"%') " +
                                " and (photolabels @>'{"+photolabels+"}') " +
                                " and (facesid @>'{"+facesid+"}')";
                    }else{
                        integratedQuerySql = "(formatted_address like '%"+address+"%' or pois::text like '%"+address+"%' or roads::text  like '%"+address+"%') " +
                                " and (photolabels @>'{"+photolabels+"}') ";
                    }
                }else{
                    if(!facesid.equals("")){
                        integratedQuerySql = "(formatted_address like '%"+address+"%' or pois::text like '%"+address+"%' or roads::text  like '%"+address+"%') " +
                                " and (facesid @>'{"+facesid+"}')";
                    }else{
                        integratedQuerySql = "(formatted_address like '%"+address+"%' or pois::text like '%"+address+"%' or roads::text  like '%"+address+"%') ";
                    }
                }
            }else {
                if(!photolabel.equals("")){
                    if(!facesid.equals("")){
                        integratedQuerySql = "(photolabels @>'{"+photolabels+"}') " +
                                " and (facesid @>'{"+facesid+"}')";
                    }else{
                        integratedQuerySql = "(photolabels @>'{"+photolabels+"}') ";
                    }
                }else{
                    if(!facesid.equals("")){
                        integratedQuerySql = "(facesid @>'{"+facesid+"}')";
                    }
                }
            }

        }
        return integratedQuerySql;
    }
    private JSONArray getGPSArray(String GPS){
        JSONArray jsonGPSArray = new JSONArray();
        double lon = 0.0f;
        double lat = 0.0f;
        System.out.println(GPS);
        if(!GPS.equals("")&&GPS!=null){
//            System.out.println(GPS);
            String temp[]= GPS.split("\\(")[1].split(" ");
            lon = Double.parseDouble(temp[0]);
            lat = Double.parseDouble(temp[1].split("\\)")[0]);
        }
        jsonGPSArray.add(lon);
        jsonGPSArray.add(lat);
        return jsonGPSArray;
    }

    private String getCity(String formatted_addresss){
        String city = "";
        if(formatted_addresss.indexOf("市")>-1){
            city = formatted_addresss.split("市")[0]+"市";
        }else if(formatted_addresss.indexOf("自治州")>-1){
            city = formatted_addresss.split("自治州")[0]+"自治州";
        }else {
            for(String c:spectilCity){
                if(formatted_addresss.indexOf(c)>-1){
                    city =c;
                    break;
                }
            }
        }
        return city;

    }


}
