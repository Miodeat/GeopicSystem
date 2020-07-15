package services;

import net.sf.json.JSONObject;
import persistence.PoiDao;
import persistence.impl.PoiDaoImp;

public class PoiService {
    private PoiDao poiDao;
    public JSONObject getPoisInfo(){
        poiDao = new PoiDaoImp();
        return poiDao.getPoisInfo();
    }
}
