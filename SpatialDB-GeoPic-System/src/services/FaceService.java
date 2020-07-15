package services;

import domain.FaceInfo;
import domain.UserInfo;
import persistence.FaceDao;
import persistence.impl.FaceDaoImp;

import java.util.HashMap;

public class FaceService {
    private FaceDao faceDao;

    public int getFaceCount(UserInfo userInfo){
        faceDao = new FaceDaoImp();
        return faceDao.getFaceCount(userInfo);
    }

    //用来插入人脸的路径、face_token信息
    public boolean insertFaceInfo(FaceInfo faceInfo,UserInfo userInfo){
        faceDao = new FaceDaoImp();
        return faceDao.insertFaceInfoToDB(faceInfo,userInfo);
    }

    //根据人物token获取人物的id
    public int getFaceIdAccordingFaceToken(FaceInfo faceInfo,UserInfo userInfo){
        faceDao = new FaceDaoImp();
        return faceDao.getFaceIdAccordingFaceToken(faceInfo,userInfo);
    }

    //根据人物path获取人物的id
    public  int getFaceIdAccordingFacePath(FaceInfo faceInfo,UserInfo userInfo){
        faceDao = new FaceDaoImp();
        return faceDao.getFaceIdAccordingFacePath(faceInfo,userInfo);
    }


    //根据法测Label获取face_id
    public int getFaceIdAccordingFaceLabel(FaceInfo faceInfo,UserInfo userInfo) {
        faceDao = new FaceDaoImp();
        return faceDao.getFaceIdAccordingFaceLabel(faceInfo,userInfo);
    }

    public boolean updateFaceLabelToDB(FaceInfo faceInfo,UserInfo userInfo){
        faceDao = new FaceDaoImp();
        return faceDao.updateFaceLabelToDB(faceInfo,userInfo);
    }

    //根据id获取facePath和faceLabel
    public HashMap<String, String> getFacePathAndLabel(int faceId, UserInfo userInfo){
        faceDao = new FaceDaoImp();
        return faceDao.getFacePathAndLabel(faceId,userInfo);
    }
}

