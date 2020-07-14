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

            String registerSql = "insert into users(username,password) values(?,?) returning user_id ";
            preparedStatement = connection.prepareStatement(registerSql);
            preparedStatement.setString(1,userInfo.getUsername());
            preparedStatement.setString(2,userInfo.getPassword());

            ResultSet resultSet1 = preparedStatement.executeQuery();
            System.out.println(resultSet1.toString()+"resulsd");
            int user_id = 0;

            while(resultSet1.next()){
              user_id = resultSet1.getInt(1);
              System.out.println(user_id);
              userInfo.setUserDBName("db"+user_id);
            }
            if(user_id!=0){
                createUserDbAndTables(userInfo,connection);
                registerResterRes.put("message","success");

            }else{
                registerResterRes.put("message","failure");
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
                    "where username ='"+userInfo.getUsername().trim()+"' and " +
                    "password ='"+userInfo.getPassword().trim()+"' limit 1";
            PreparedStatement preparedStatement = connection.prepareStatement(loginByUserNameAndPasswordSql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int userDB = resultSet.getInt("userdb");
                String userDbName = "db"+userDB;
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

    @Override
    public boolean createUserDbAndTables(UserInfo userInfo,Connection connection) {
        try {
            if(connection.isClosed()){
                connection = UtilDao.getConnection_SysOpDB();
            }
            String createDbSql = "CREATE DATABASE "+userInfo.getUserDBName();
            PreparedStatement preparedStatement = connection.prepareStatement(createDbSql);
            int createDbRes = preparedStatement.executeUpdate();
            connection.close();

            connection = UtilDao.getConnection_UserDB(userInfo.getUserDBName());
            System.out.println(connection);
            String createExtension = "create extension postgis";
            preparedStatement = connection.prepareStatement(createExtension);
            int creatEntension = preparedStatement.executeUpdate();

            String createPotosSql ="create table photos(\n" +
                    "\tphoto_id serial primary key,\n" +
                    "\tformatted_address varchar,\n" +
                    "\ttakentime timestamp DEFAULT '9999-01-01 00:00:00',\n" +
                    "\tgeo Geometry(('POINT'),3857),\n" +
                    "\tphotolabels text[],\n" +
                    "\tphotopath varchar ,\n" +
                    "\tpois text[],\n" +
                    "\troads text[],\n" +
                    "\tfacesid integer[],\n" +
                    "\tsharedflag boolean DEFAULT false\n" +
                    "\t);";
            preparedStatement = connection.prepareStatement(createPotosSql);
            boolean num = preparedStatement.execute();

            String createFaceSql = "CREATE table face(\n" +
                    "\tface_id serial primary key,\n" +
                    "\tfacepath varchar ,\n" +
                    "\tfacetoken varchar,\n" +
                    "\tfacelabel varchar\n" +
                    ");";
            preparedStatement = connection.prepareStatement(createFaceSql);
            int face = preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
