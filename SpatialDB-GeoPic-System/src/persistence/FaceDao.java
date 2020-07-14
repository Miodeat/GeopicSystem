package persistence;

import domain.UserInfo;

public interface FaceDao {
    public int getFaceCount(UserInfo userInfo);
}
