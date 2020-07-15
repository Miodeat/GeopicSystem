package servlets;

import net.coobird.thumbnailator.Thumbnails;
import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Decoder;

//import Decoder.BASE64Decoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(name = "GetPhotoDataServlet")
public class GetPhotoDataServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        BufferedReader reader = request.getReader();
        JSONObject res = new JSONObject();
        String line = "";
        String content = "";
        while ((line = reader.readLine()) != null){
            content+=line;
        }
        String jbstring = content.toString();

        String temp[] = jbstring.split(",");

        String imageName = temp[0];
        String imgStr = temp[1];

        String imgDir = request.getServletContext().getRealPath("/").split("out")[0]+"img\\photoDataSet\\photos\\";
        String imagePath = imgDir+imageName;

        //下面这个是打包发布时的路径
//        String path = "/usr/local/tomcat/webapps/GeoPic/static/data/photos/";

        //下面这个是打包发布时的路径
//        String imagePath = "/usr/local/tomcat/webapps/GeoPic/static/data/photos/"+imageName;
        boolean judgeIsExistRes = judgeIsExist(imageName,imgDir);
        if(judgeIsExistRes){
            res.put("message","图片已存在，不重复入库");
            res.put("success",false);
            out.write(res.toString());
            return;
        }else{
            BASE64Decoder decoder = new BASE64Decoder();
            try{
                //Base64解码
                byte[] b = decoder.decodeBuffer(imgStr);
                for(int i = 0;i<b.length;i++){
                    if(b[i]<0){
                        b[i]+=256;
                    }
                }
                System.out.println(imagePath);
                OutputStream o = new FileOutputStream(imagePath);
                o.write(b);
                o.flush();
                o.close();
                res.put("message","success");
                res.put("success",true);
                out.write(res.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String thumbPath = imagePath.replace("photos","thumbs");
        try{
            Thumbnails.of(imagePath).size(200,200).toFile(thumbPath);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    protected boolean judgeIsExist(String photoName,String photoDir){
        boolean isExist = false;
        File file = new File(photoDir);
        File []array = file.listFiles();
        for(int i = 0;i<array.length;i++){
            if(array[i].isFile()){
                String imgName = array[i].getName();
                if(imgName.equals(photoName)){
                    isExist = true;
                    break;
                }
            }
        }
        return isExist;
    }
}
