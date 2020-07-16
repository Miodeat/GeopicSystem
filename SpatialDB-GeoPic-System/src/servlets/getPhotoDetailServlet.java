package servlets;

import domain.UserInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.opencv.photo.Photo;
import services.FaceService;
import services.PhotoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet(name = "getPhotoDetailServlet")
public class getPhotoDetailServlet extends HttpServlet {
    private PhotoService photoService;
    private FaceService faceService;
    private UserInfo userInfo;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String photoPath = request.getParameter("photoPath");

        System.out.println(photoPath+"前端传回来得");
//        String photoPath ="";
////        System.out.println(photoPath1.indexOf("/"));
//        if(!photoPath1.equals("")&&photoPath1!=null){
//            photoPath = photoPath1.replaceAll("/","\\\\");
//        }

        System.out.println("hdj"+photoPath+"我的");

        String userDbname = request.getParameter("userDbname");
        userInfo = new UserInfo();
        userInfo.setUserDBName(userDbname);

        photoService = new PhotoService();
        JSONObject getPhotoDetailRes =new JSONObject();
        getPhotoDetailRes =  photoService.getPhotoDetailAccordingPhotoPath(photoPath,userInfo);
        System.out.println(getPhotoDetailRes);
        JSONArray photoDetailArray = getPhotoDetailRes.getJSONArray("photoDetail");
        JSONObject photoDetail = photoDetailArray.getJSONObject(0);

        System.out.println("dsd"+photoDetail);
        if(photoDetailArray.size()>0){
            photoDetail.put("message","success");
        }else{
            photoDetail.put("message","failure");
        }
        System.out.println("dssd"+photoDetail);
//        String facesId = photoDetail.getString("facesId");
//
//        photoDetail.put("facesPath",getFacePathFromFaceId(facesId).toString());
        System.out.println("dssds"+photoDetail);
        out.write(photoDetail.toString());

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    protected JSONArray getFacePathFromFaceId(String facesid){
        JSONArray facesIdArray = new JSONArray();

        if(facesid.indexOf(",")>-1){
            String []faces = facesid.split("\\{")[1].split(",");
            for(int i = 0;i<faces.length;i++){
                if(i<faces.length-1){
                    int faceId = Integer.parseInt(faces[i]);
                    JSONObject jsonObject = new JSONObject();
                    HashMap<String ,String > facePathAndLabel = getFacePathAndLabel(faceId);
                    for(String key:facePathAndLabel.keySet()){
                        jsonObject.put("facePath",key);
                        jsonObject.put("faceLabel",facePathAndLabel.get(key));
                    }
                    facesIdArray.add(jsonObject);
                }else{
                    int faceId = Integer.parseInt(faces[i].split("\\}")[0]);
                    JSONObject jsonObject = new JSONObject();
                    HashMap<String ,String > facePathAndLabel = getFacePathAndLabel(faceId);
                    for(String key:facePathAndLabel.keySet()){
                        jsonObject.put("facePath",key);
                        jsonObject.put("faceLabel",facePathAndLabel.get(key));
                    }
                    facesIdArray.add(jsonObject);
                }
            }
        }else if(facesid!=""){
            int faceId = Integer.parseInt(facesid.split("\\{")[1].split("\\}")[0]);
            JSONObject jsonObject = new JSONObject();
            HashMap<String ,String > facePathAndLabel = getFacePathAndLabel(faceId);
            for(String key:facePathAndLabel.keySet()){
                jsonObject.put("facePath",key);
                jsonObject.put("faceLabel",facePathAndLabel.get(key));
            }
            facesIdArray.add(jsonObject);
        }
        return facesIdArray;
    }

    //根据faceid获取facePath
    protected HashMap<String ,String> getFacePathAndLabel(int faceId){
        HashMap<String,String >facePathAndLabel = new HashMap<>();
        faceService = new FaceService();
        facePathAndLabel = faceService.getFacePathAndLabel(faceId,userInfo);
        return facePathAndLabel;
    }
}
