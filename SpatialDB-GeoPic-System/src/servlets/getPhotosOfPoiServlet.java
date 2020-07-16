package servlets;

import domain.UserInfo;
import net.sf.json.JSONObject;
import services.PoiService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "getPhotosOfPoiServlet")
public class getPhotosOfPoiServlet extends HttpServlet {
    private PoiService poiService;
    private UserInfo userInfo;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String poiGPS = request.getParameter("lnglat").toString();
        System.out.println(poiGPS);
        String dbname = request.getParameter("userDbname");
        userInfo = new UserInfo();
        userInfo.setUserDBName("sysopdb");
//        System.out.println(dbname);
        JSONObject res = new JSONObject();
        if(poiGPS.indexOf(",")>-1){
            poiService = new PoiService();
            res = poiService.getPhotosOfPoi(toWKT(poiGPS),userInfo);
            System.out.println(res+"rs");
        }else{
            res.put("message","failure");
        }


        out.write(res.toString());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected String toWKT(String poiGPS ){
        String wkt = "POINT";
        if(poiGPS.indexOf(",")>-1){
            String temp[] = poiGPS.split(",");
            if(temp[0].indexOf("[")>-1){
                String lon = temp[0].replace("[","(");
                wkt = wkt+lon+" ";
            }
            if(temp[1].indexOf("]")>-1){
                String lat = temp[1].replace("]",")");
                wkt = wkt+lat;
            }
        }
        return wkt;
    }
}
