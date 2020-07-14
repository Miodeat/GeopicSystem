package persistence;

import domain.UserInfo;
import net.sf.json.JSONObject;

import java.sql.Connection;

public interface UserDao {
    public JSONObject registerByUserNameAndPassword(UserInfo userInfo);

    public JSONObject loginByUserNameAndPassword(UserInfo userInfo);

    public boolean createUserDbAndTables(UserInfo userInfo, Connection connection);

}
