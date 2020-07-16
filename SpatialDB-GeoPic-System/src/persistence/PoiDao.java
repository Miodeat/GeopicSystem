package persistence;

import domain.PhotoInfo;
import domain.UserInfo;
import net.sf.json.JSON;
import net.sf.json.JSONObject;

public interface PoiDao {

    public JSONObject getPoisInfo();

    public JSONObject getPhotosOfPoi(String poiGPS,UserInfo userInfo);

    public JSONObject insertSharedPhotos(PhotoInfo photoInfo,UserInfo userInfo);
}
