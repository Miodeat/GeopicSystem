package persistence;

import domain.PhotoInfo;
import domain.UserInfo;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.opencv.photo.Photo;

import java.util.ArrayList;
import java.util.HashMap;

public interface PhotoDao {

    public boolean insertPhotoInfo(PhotoInfo photoInfo, UserInfo userInfo);

    public JSONObject getTimeQueryPhotoPath(String startTime, String endTime, UserInfo userInfo);

    public JSONObject getPlaceQueryPotoPath(String place, UserInfo userInfo);

    public JSONObject getSemanticQueryPhotoPath(PhotoInfo photoInfo,UserInfo userInfo);

    public JSONObject initGeoPicDesktop(PhotoInfo photoInfo,UserInfo userInfo);

    public JSONObject getintegratedQueryPhotoPath(String starttime,String endTime,String address,String photolabel,String facelabel,String userDbname);

    public JSONObject getPhotoDetailAccordingPhotoPath(String photoPath,UserInfo userInfo);

    public int getPhotoIdAcoordintPhotoPath(PhotoInfo photoInfo,UserInfo userInfo);

    public boolean insertPhotoFaceId(PhotoInfo photoInfo,UserInfo userInfo);


}
