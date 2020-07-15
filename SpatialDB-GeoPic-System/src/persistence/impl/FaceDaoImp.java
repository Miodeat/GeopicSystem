package persistence.impl;

import domain.FaceInfo;
import domain.UserInfo;
import persistence.FaceDao;
import persistence.UtilDao;

import javax.servlet.jsp.jstl.sql.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class FaceDaoImp implements FaceDao {

    @Override
    public int getFaceCount(UserInfo userInfo) {
        int faceCount = 0;
        Connection connection = null;
        try{
           connection = UtilDao.getConnection_UserDB(userInfo.getUserDBName());
           String getFaceCountSql = "select count(*) from face";
            PreparedStatement preparedStatement = connection.prepareStatement(getFaceCountSql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                faceCount = resultSet.getInt(1);
            }
            UtilDao.closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return faceCount;
    }

    @Override
    public boolean insertFaceInfoToDB(FaceInfo faceInfo,UserInfo userInfo) {
        boolean insertRes = false;
        Connection connection = null;
        try {
            connection = UtilDao.getConnection_UserDB(userInfo.getUserDBName());
            String insertFaceInfoSql = "insert into face(face_id,facelabel, facepath,facetoken) values (?,?,?,'{"+faceInfo.getFaceToken()+"}')";
            PreparedStatement preparedStatement = connection.prepareStatement(insertFaceInfoSql);
            preparedStatement.setInt(1,faceInfo.getFaceId());
            preparedStatement.setString(2,faceInfo.getFaceLabel());
            preparedStatement.setString(3,faceInfo.getFacePath());
            int num = preparedStatement.executeUpdate();
            if(num>0){
                insertRes = true;
            }
            UtilDao.closeConnection(connection);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return insertRes;
    }

    @Override
    public boolean updateFaceLabelToDB(FaceInfo faceInfo,UserInfo userInfo) {

        boolean updateFaceLabelResult = false;
        Connection conn = null;
        try{
            System.out.println(faceInfo.getFacePath());
            String updateFaceLabelSql = "update face set facelabel = '" +
                    faceInfo.getFaceLabel()+"'"+
                    " where facepath ='"+faceInfo.getFacePath()+"'";
            conn = UtilDao.getConnection_UserDB(userInfo.getUserDBName());
            PreparedStatement preparedStatement = conn.prepareStatement(updateFaceLabelSql);
            int result = preparedStatement.executeUpdate();
            System.out.println(result);
            if(result>0){
                updateFaceLabelResult = true;
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updateFaceLabelResult;

    }

    @Override
    public int getFaceIdAccordingFacePath(FaceInfo faceInfo,UserInfo userInfo) {
        int id = 0;
        Connection conn;
        try{
            conn = UtilDao.getConnection_UserDB(userInfo.getUserDBName());
            String getFaceIdSql = "select face_id from face where facepath = '"+faceInfo.getFacePath()+"'limit 1";
            PreparedStatement preparedStatement = conn.prepareStatement(getFaceIdSql);

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                id = resultSet.getInt(1);
            }
            UtilDao.closeConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public int getFaceIdAccordingFaceToken(FaceInfo faceInfo,UserInfo userInfo) {
        int id = 0;
        Connection conn;
        try{
            conn = UtilDao.getConnection_UserDB(userInfo.getUserDBName());
            String faceTokens = faceInfo.getFaceToken();
            System.out.println(faceTokens);
            String getFaceIdSql = "select face_id from face where facetoken @>'{"+faceTokens+"}'limit 1";
            PreparedStatement preparedStatement = conn.prepareStatement(getFaceIdSql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                id = resultSet.getInt(1);
            }
            UtilDao.closeConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public int getFaceIdAccordingFaceLabel(FaceInfo faceInfo,UserInfo userInfo) {
        int faceId = -1;
        Connection conn;
        try{
            conn = UtilDao.getConnection_UserDB(userInfo.getUserDBName());
            String getFAceIdAccordingFaceLabelSql = "select face_id from face " +
                    "where facelabel = '"+faceInfo.getFaceLabel()+"' limit 1";
            PreparedStatement preparedStatement = conn.prepareStatement(getFAceIdAccordingFaceLabelSql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                faceId = resultSet.getInt("face_id");
            }
            UtilDao.closeConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return faceId;
    }

    @Override
    public HashMap<String, String> getFacePathAndLabel(int faceId,UserInfo userInfo) {
        String facePath = "";
        String faceLabel = "";
        HashMap<String,String>facePathAndLabel = new HashMap<>();
        Connection conn;
        try{
            String getFacePathSql = "select facepath,facelabel from face " +
                    "where face_id = "+faceId +" limit 1";
            conn = UtilDao.getConnection_UserDB(userInfo.getUserDBName());
            PreparedStatement preparedStatement = conn.prepareStatement(getFacePathSql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                facePath = resultSet.getString("facepath");
                faceLabel = resultSet.getString("facelabel");
                facePathAndLabel.put(facePath,faceLabel);
            }
            UtilDao.closeConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return facePathAndLabel;
    }
}
