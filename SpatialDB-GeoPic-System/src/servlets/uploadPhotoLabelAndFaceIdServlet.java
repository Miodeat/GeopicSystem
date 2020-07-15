package servlets;

import domain.FaceInfo;
import domain.PhotoInfo;
import domain.UserInfo;
import net.sf.json.JSONObject;
import services.FaceService;
import services.PhotoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "uploadPhotoLabelAndFaceIdServlet")
public class uploadPhotoLabelAndFaceIdServlet extends HttpServlet {
    private PhotoService photoService;
    private PhotoInfo photoInfo;
    private FaceService faceService;
    private FaceInfo faceInfo;
    private UserInfo userInfo;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String type = request.getParameter("type");
        String userDbname = request.getParameter("userDbname");
        userInfo = new UserInfo();
        userInfo.setUserDBName(userDbname);
        JSONObject resObj = new JSONObject();
        switch (type){
            case"photoLabel":
                boolean updatePhotoLabelres = updatePhotoLabel(request);
                if(updatePhotoLabelres){
                    resObj.put("message","update PhotoLabel successfully");
                    resObj.put("success",true);

                }else{
                    resObj.put("message","fail to update PhotoLabel");
                    resObj.put("success",false);
                }
                break;
            case "faceId":
                boolean res = updatePhotoFaceId(request);
                if(res){
                    resObj.put("message","success");
//                    resObj.put("success",true);
                }else {
                    resObj.put("message","failure ");
//                    resObj.put("success",false);
                }

                break;
            default:
                break;
        }
        out.write(resObj.toString());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    protected boolean updatePhotoLabel(HttpServletRequest request){
        boolean updateRes = false;
        String photoLabels = request.getParameter("photoLabels");
        String photoPath = request.getParameter("photoPath");
        ArrayList<String> photolabels = new ArrayList<>();
        if(photoLabels!=""){
            photolabels = getFormattedPhotoLabels(photoLabels);
            photoInfo = new PhotoInfo();
            photoInfo.setPhotoLabels(photolabels.toString());
            photoInfo.setPhotoPath(photoPath);
            photoService = new PhotoService();
            updateRes = photoService.insertPhotoInfo(photoInfo,userInfo);

        }else{
            updateRes =false;
        }

        return  updateRes;
    }

    protected ArrayList<String>getFormattedPhotoLabels(String photoLabels){
        ArrayList<String>photolabels = new ArrayList<>();
        System.out.println("我在getformattd"+photoLabels);
        if(photoLabels.contains(",")){
            String []photoLabel = photoLabels.split(",");
            for(int i = 0;i<photoLabel.length;i++){
                photolabels.add(photoLabel[i]);
            }
        }else {
            photolabels.add(photoLabels);
        }
        return photolabels;
    }

    protected boolean updatePhotoFaceId(HttpServletRequest request){
        boolean updateRes = false;
        String photoPath = request.getParameter("photoPath");
        String facesPath = request.getParameter("facesPath");
        String faceTokens = request.getParameter("faceTokens");
        System.out.println(facesPath+"hellow");
        ArrayList<Integer> facesId = new ArrayList<>();
        faceInfo = new FaceInfo();
        if(facesPath.contains(",")){
            String []facePath = facesPath.split(",");
            for(int i = 0;i<facePath.length;i++){
                faceInfo.setFacePath(facePath[i]);
                faceService = new FaceService();
                int faceId = faceService.getFaceIdAccordingFacePath(faceInfo,userInfo);
                facesId.add(faceId);
            }
        }else if(facesPath!=""){
            faceInfo.setFacePath(facesPath);
            faceService = new FaceService();
            int faceId = faceService.getFaceIdAccordingFacePath(faceInfo,userInfo);
            facesId.add(faceId);
        }else{
            updateRes = false;
        }

        if(faceTokens.contains(",")){
            String[] faceToken = faceTokens.split(",");
            for(int i = 0;i<faceToken.length;i++){

//                ArrayList<String> faceTokenList = new ArrayList<>();
//                faceTokenList.add(faceToken[i]);
                faceInfo.setFaceToken(faceToken[i]);
                faceService = new FaceService();
                int faceId = faceService.getFaceIdAccordingFaceToken(faceInfo,userInfo);
                facesId.add(faceId);
            }
        }else if(faceTokens!=""){

//            ArrayList<String> faceTokenList = new ArrayList<>();
//            faceTokenList.add(faceTokens);
            faceInfo.setFaceToken(faceTokens);
            faceService = new FaceService();
            int faceId = faceService.getFaceIdAccordingFaceToken(faceInfo,userInfo);
            facesId.add(faceId);
        }else {
            updateRes = false;
        }
        photoInfo = new PhotoInfo();


        photoInfo.setPhotoPath(photoPath);
//        getPhotoIdAcoordintPhotoPath
        photoService = new PhotoService();

        photoInfo.setPhotoId(photoService.getPhotoIdAcoordintPhotoPath(photoInfo,userInfo));
        photoInfo.setFacesId(getFaceIdString(facesId));
        System.out.println(photoInfo.getFacesId());
        updateRes = photoService.insertPhotoFaceId(photoInfo,userInfo);

        System.out.println(photoPath);
        System.out.println("JHHHJ"+facesPath);
        return  updateRes;
    }

    protected String getFaceIdString(ArrayList<Integer> faceid){
        String facesId = "";
        for(int i = 0;i<faceid.size();i++){
            if(i<faceid.size()-1){
                facesId+= faceid.get(i)+",";
            }else{
                facesId+=faceid.get(i);
            }
        }
        return facesId;
    }
}
