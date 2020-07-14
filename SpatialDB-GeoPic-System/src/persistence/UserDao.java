package persistence;

import domain.UserInfo;
import net.sf.json.JSONObject;

public interface UserDao {
    public JSONObject registerByUserNameAndPassword(UserInfo userInfo);

    public JSONObject loginByUserNameAndPassword(UserInfo userInfo);

}
