package servlets;

import domain.FaceInfo;
import domain.UserInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import services.FaceService;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

@WebServlet(name = "uploadFaceInfoServlet")
public class uploadFaceInfoServlet extends HttpServlet {
    private FaceService faceService;
    private UserInfo userInfo;
    private  FaceInfo faceInfo;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String faces = request.getParameter("faces");
        String userDbName = request.getParameter("userDbName");
        userInfo = new UserInfo();
        userInfo.setUserDBName(userDbName);
        System.out.println(faces);
        String oriImagePath = request.getServletContext().getRealPath("/").split("out")[0]+
                "img\\photoDataSet\\photos\\"+request.getParameter("file");

//        String oriImagePath = request.getServletContext().getRealPath("/")+"static\\data/photos\\"+request.getParameter("file");
        //下面这个是打包发布时的路径
//        String oriImagePath ="/usr/local/tomcat/webapps/GeoPic/static/data/photos/"+request.getParameter("file");
        String faceDir = request.getServletContext().getRealPath("/").split("out")[0]+
                "img\\faces\\";

//        String facePath = request.getServletContext().getRealPath("/")+"static\\data\\faces\\";

        //下面这个是打包发布时的路径
//        String facePath = "/usr/local/tomcat/webapps/GeoPic/static/data/faces/";
        File file = new File(faceDir);// 图片存放路径
        File list[] = file.listFiles();
        int faceDirNum = list.length;
        JSONObject res = new JSONObject();
//        JSONArray facesBase64AndPathArray = new JSONArray();
        JSONArray facesName = new JSONArray();
        JSONArray facesPath = new JSONArray();
//        JSONArray facesPath = new JSONArray();
        boolean insertResult =false;
        JSONObject jsonObject = JSONObject.fromObject(faces);
        JSONArray jsonArray = jsonObject.getJSONArray("faces");
        for(int i = 0;i<jsonArray.size();i++){
            int faceId = (faceDirNum+i+1)+40;
            String faceStorePath = "faces\\"+faceId+".jpg";
            String faceDirPath = faceDir+faceId+".jpg";
            faceInfo = new FaceInfo();
            faceService = new FaceService();
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            JSONObject objRect = JSONObject.fromObject(jsonObject1.get("face_rectangle").toString());
            String face_token = "\""+jsonObject1.get("face_token").toString()+"\"";

            faceInfo.setFacePath(faceStorePath);
            faceInfo.setFaceToken(face_token);
            faceInfo.setFaceId(faceId);
            insertResult = faceService.insertFaceInfo(faceInfo,userInfo);

            if(insertResult){
                int top = (int)objRect.get("top");
                int left = (int)objRect.get("left");
                int width = (int)objRect.get("width");
                int height =(int)objRect.get("height");
                saveFace(top,left,width,height,faceDirPath,oriImagePath);
                String faceBase64 = getImgBase64(faceDirPath);
                facesName.add(faceBase64);
                facesPath.add(faceStorePath);
//                JSONObject facesBase64AndPath = new JSONObject();
//                facesBase64AndPath.put("faceBase64",faceBase64);
//                facesBase64AndPath.put("facePath",faceStorePath);
//                facesBase64AndPathArray.add(facesBase64AndPath);
            }
        }
        if(insertResult){
            res.put("message","success");

        }else{
            res.put("message","failure");
        }
        res.put("facesBase64",facesName);
        res.put("facesPath",facesPath);
        out.write(res.toString());

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected  boolean saveFace(int top,int left,int width,int height,String facePath,String oriImagePath) throws IOException {
        BufferedImage bufferedImage =  ImageIO.read(new File(oriImagePath));
        BufferedImage subImage=bufferedImage.getSubimage(left,top,width,height);
        ImageIO.write(subImage,"JPEG",new File(facePath));
        return  true;
    }

    //获取人脸的base64数据
    protected String getImgBase64(String facePath) {
        InputStream in = null;
        byte[] data = null;

        // 读取图片字节数组
        try {
            in = new FileInputStream(facePath);

            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }
}
