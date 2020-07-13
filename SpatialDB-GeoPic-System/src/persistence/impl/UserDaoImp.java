package persistence.impl;

import domain.UserInfo;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import persistence.UserDao;
import persistence.UtilDao;

import javax.servlet.jsp.jstl.sql.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDaoImp implements UserDao {
    @Override
    public JSONObject registerByUserNameAndPassword(UserInfo userInfo) {
        Connection connection = null;
        JSONObject registerResterRes = new JSONObject();
        try{
            String registerUsername = userInfo.getUsername();
            connection = UtilDao.getConnection_SysOpDB();

            String findWhetherUserHasExistedSql = "select username from users";
            PreparedStatement preparedStatement = connection.prepareStatement(findWhetherUserHasExistedSql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                String username = resultSet.getString(1);
                if(username.equals(registerUsername)){
                    registerResterRes.put("message","failure,用户名已被占用");
                    UtilDao.closeConnection(connection);
                    return registerResterRes;
                }
            }

            String registerSql = "insert into users(username,password) values(?,?)";
            preparedStatement = connection.prepareStatement(registerSql);
            preparedStatement.setString(1,userInfo.getUsername());
            preparedStatement.setString(2,userInfo.getPassword());
            int num = preparedStatement.executeUpdate();
            if(num>0){
                registerResterRes.put("message","success,注册成功");
            }else{
                registerResterRes.put("message","注册失败");
            }
            UtilDao.closeConnection(connection);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return registerResterRes;
    }

    @Override
    public JSONObject loginByUserNameAndPassword(UserInfo userInfo) {
        Connection connection = null;
        JSONObject loginRes = new JSONObject();
        try{
            connection = UtilDao.getConnection_SysOpDB();
            String loginByUserNameAndPasswordSql ="select * from users " +
                    "where username ="+userInfo.getUsername()+" and " +
                    "password ="+userInfo.getPassword()+" limit 1";
            PreparedStatement preparedStatement = connection.prepareStatement(loginByUserNameAndPasswordSql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int userDB = resultSet.getInt("userdb");
                String userDbName = "DB"+userDB;
                loginRes.put("message","success");
                loginRes.put("userDbName",userDbName);
                UtilDao.closeConnection(connection);
                return loginRes;
            }
            loginRes.put("message","failure");
            UtilDao.closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginRes;
    }
}
