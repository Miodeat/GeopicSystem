package services;

import domain.UserInfo;
import net.sf.json.JSONObject;
import persistence.UserDao;
import persistence.impl.UserDaoImp;

public class UserService {
    private UserDao userDao;

    public JSONObject registerByUserNameAndPassword(UserInfo userInfo){
        userDao = new UserDaoImp();
        return userDao.registerByUserNameAndPassword(userInfo);
    }

    public JSONObject loginByUserNameAndPassword(UserInfo userInfo){
        userDao = new UserDaoImp();
        return userDao.loginByUserNameAndPassword(userInfo);
    }

}
