package services;

import domain.UserInfo;
import persistence.FaceDao;
import persistence.impl.FaceDaoImp;

public class FaceService {
    private FaceDao faceDao;

    public int getFaceCount(UserInfo userInfo){
        faceDao = new FaceDaoImp();
        return faceDao.getFaceCount(userInfo);
    }
}
