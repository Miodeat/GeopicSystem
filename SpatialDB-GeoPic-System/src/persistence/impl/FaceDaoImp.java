package persistence.impl;

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
}
