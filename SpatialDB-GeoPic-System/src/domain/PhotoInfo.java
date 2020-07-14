package domain;

import java.sql.Timestamp;
import java.util.ArrayList;

public class PhotoInfo {
    private Timestamp takenTime; //照片的拍摄时间
    private String formatted_address;//照片的拍摄地点
    private String  photoLabels;//照片的标签
    private String facesId;//照片中人物的id
    private String photoPath;//照片的本地存储路径
    private String geo;//照片拍摄的GPS坐标
    private String thumbsPath;
    private  int photoId;
    private String pois;//照片周围的pois信息
    private String roads;//照片周雯的roads信息

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setThumbsPath(String thumbsPath) {
        this.thumbsPath = thumbsPath;
    }

    public String getThumbsPath() {
        return thumbsPath;
    }

    public void setTakenTime(Timestamp takenTime) {
        this.takenTime = takenTime;
    }

    public Timestamp getTakenTime() {
        return takenTime;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoLabels(String photoLabels) {
        this.photoLabels = photoLabels;
    }

    public String getPhotoLabels() {
        return photoLabels;
    }

    public void setGeo(String geo) {
        this.geo = geo;
    }

    public String getGeo() {
        return geo;
    }

    public void setFacesId(String facesId) {
        this.facesId = facesId;
    }

    public String getFacesId() {
        return facesId;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setPois(String pois) {
        this.pois = pois;
    }

    public String getPois() {
        return pois;
    }

    public void setRoads(String roads) {
        this.roads = roads;
    }

    public String getRoads() {
        return roads;
    }
}
