package servlets;

import domain.PhotoInfo;
import domain.UserInfo;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.opencv.photo.Photo;
import services.PhotoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

@WebServlet(name = "uploadPhotoExifDataServlet")
public class uploadPhotoExifDataServlet extends HttpServlet {
    private UserInfo userInfo;
    private PhotoInfo photoInfo;
    private PhotoService photoService;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String data = request.getParameter("data");
        String userDbname = request.getParameter("userDbname");
        String resultType = request.getParameter("result");
//        System.out.println(data);
        JSONObject dataObj = JSONObject.fromObject(data);
        String photopath = "photoDataSet\\photos\\"+dataObj.getString("photoName");
        ;
        String formatted_address = dataObj.getString("formatted_address");
        String takenTime = dataObj.getString("takenTime");
        String geo = dataObj.getString("AMapGPS");
        String pois = dataObj.getString("pois");
        String roads = dataObj.getString("roads");
        if(pois.equals("")){
            pois="{}";
        }else{
            pois = pois.replace("[","{");
            pois = pois.replace("]","}");
        }
        if(roads.equals("")){
            roads = "{}";
        }else {
            roads = roads.replace("[","{");
            roads = roads.replace("]","}");
        }

        photoInfo = new PhotoInfo();
        userInfo = new UserInfo();
        photoInfo.setFormatted_address(formatted_address);
        photoInfo.setPhotoPath(photopath);
        photoInfo.setTakenTime(toTimeStamp(takenTime));
        photoInfo.setPois(pois);
        photoInfo.setRoads(roads);
        photoInfo.setGeo(geo);
        photoInfo.setPhotoLabels("{}");
        photoInfo.setFacesId("{}");
        userInfo.setUserDBName(userDbname);
        photoService = new PhotoService();
        boolean inserRes = photoService.insertPhotoInfo(photoInfo,userInfo);
        JSONObject res = new JSONObject();
        if(inserRes){
            res.put("message","success");
        }else {
            res.put("message","failure");
        }
        out.write(res.toString());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    private Timestamp toTimeStamp(String time) {
        String dateString = "";
        System.out.println("我看看"+time);
        if(time.equals("9999-01-01 00:00:00")){
            System.out.println("进来吗");
            dateString=time;
        }else{
            String[]date = time.split(":");

            if(date.length==5){
                dateString = date[0]+"-"+date[1]+"-"+date[2]+":"+date[3]+":"+date[4];
            }else{
                dateString=time;
            }
        }


        System.out.println(dateString);
        Timestamp timestamp = Timestamp.valueOf(dateString);
        return timestamp;
    }
}
