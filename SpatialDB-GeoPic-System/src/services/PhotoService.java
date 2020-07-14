package services;

import domain.PhotoInfo;
import domain.UserInfo;
import net.sf.json.JSONObject;
import persistence.PhotoDao;
import persistence.impl.PhotoDaoImp;

public class PhotoService{
    private PhotoDao photoDao;
    public JSONObject initGeoPicDesktop(PhotoInfo photoInfo, UserInfo userInfo){
        photoDao = new PhotoDaoImp();
        return photoDao.initGeoPicDesktop(photoInfo,userInfo);
    }
}
