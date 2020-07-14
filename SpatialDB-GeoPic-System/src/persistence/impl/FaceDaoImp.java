package persistence.impl;

import domain.FaceInfo;
import domain.UserInfo;
import persistence.FaceDao;
import persistence.UtilDao;

import javax.servlet.jsp.jstl.sql.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return faceCount;
    }

    @Override
    public boolean insertFaceInfoToDB(FaceInfo faceInfo,UserInfo userInfo) {
        return false;
    }

    @Override
    public boolean updateFaceLabelToDB(FaceInfo faceInfo,UserInfo userInfo) {
        return false;
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
            conn.close();
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
            conn.close();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return faceId;
    }
}
