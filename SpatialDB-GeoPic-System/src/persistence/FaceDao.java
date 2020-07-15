package persistence;

import domain.FaceInfo;
import domain.UserInfo;

import java.util.HashMap;

public interface FaceDao {
    public int getFaceCount(UserInfo userInfo);

    public boolean insertFaceInfoToDB(FaceInfo faceInfo,UserInfo userInfo);
    public boolean updateFaceLabelToDB(FaceInfo faceInfo,UserInfo userInfo);
    public int getFaceIdAccordingFacePath(FaceInfo faceInfo,UserInfo userInfo);
    public int getFaceIdAccordingFaceToken(FaceInfo faceInfo,UserInfo userInfo);
    public int getFaceIdAccordingFaceLabel(FaceInfo faceInfo,UserInfo userInfo);
    public HashMap<String,String> getFacePathAndLabel(int faceId,UserInfo userInfo);


}
