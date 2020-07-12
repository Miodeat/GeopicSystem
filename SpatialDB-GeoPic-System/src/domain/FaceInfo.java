package domain;

import java.util.ArrayList;

public class FaceInfo {
    private String facePath;
    private String faceLabel;
    private String faceToken;

    public void setFaceLabel(String faceLabel) {
        this.faceLabel = faceLabel;
    }

    public String getFaceLabel() {
        return faceLabel;
    }

    public void setFacePath(String facePath) {
        this.facePath = facePath;
    }

    public String getFacePath() {
        return facePath;
    }

    public String getFaceToken() {
        return faceToken;
    }

    public void setFaceToken(String faceToken) {
        this.faceToken = faceToken;
    }
}
