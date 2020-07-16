package services;

import domain.UserInfo;
import net.sf.json.JSONObject;
import persistence.PoiDao;
import persistence.impl.PoiDaoImp;

public class PoiService {
    private PoiDao poiDao;

    public JSONObject getPoisInfo(){
        poiDao = new PoiDaoImp();
        return poiDao.getPoisInfo();
    }

    public JSONObject getPhotosOfPoi(String poiGPS, UserInfo userInfo){
        poiDao = new PoiDaoImp();
        return poiDao.getPhotosOfPoi(poiGPS,userInfo);
    }
}
