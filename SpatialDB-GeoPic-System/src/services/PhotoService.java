package services;

import domain.PhotoInfo;
import domain.UserInfo;
import net.sf.json.JSONObject;
import persistence.PhotoDao;
import persistence.impl.PhotoDaoImp;

public class PhotoService{
    private PhotoDao photoDao;

    public boolean insertPhotoInfo(PhotoInfo photoInfo, UserInfo userInfo){
        photoDao = new PhotoDaoImp();
        return photoDao.insertPhotoInfo(photoInfo,userInfo);
    }

    public JSONObject initGeoPicDesktop(PhotoInfo photoInfo, UserInfo userInfo){
        photoDao = new PhotoDaoImp();
        return photoDao.initGeoPicDesktop(photoInfo,userInfo);
    }

    public JSONObject getintegratedQueryPhotoPath(String starttime, String endTime,
                                       String address, String photolabel, String facelabel,String userDbname){
        photoDao = new PhotoDaoImp();
        return photoDao.getintegratedQueryPhotoPath(starttime,endTime,address,photolabel,facelabel,userDbname);
    }

    public JSONObject getPhotoDetailAccordingPhotoPath(String photoPath,UserInfo userInfo){
        photoDao = new PhotoDaoImp();
        return  photoDao.getPhotoDetailAccordingPhotoPath(photoPath,userInfo);
    }

    public int getPhotoIdAcoordintPhotoPath(PhotoInfo photoInfo,UserInfo userInfo){
        photoDao = new PhotoDaoImp();
        return  photoDao.getPhotoIdAcoordintPhotoPath(photoInfo,userInfo);
    }

    public boolean insertPhotoFaceId(PhotoInfo photoInfo, UserInfo userInfo){
        photoDao = new PhotoDaoImp();
        return  photoDao.insertPhotoFaceId(photoInfo,userInfo);
    }

}
