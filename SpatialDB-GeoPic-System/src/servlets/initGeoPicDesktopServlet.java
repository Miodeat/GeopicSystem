package servlets;

import domain.PhotoInfo;
import domain.UserInfo;
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

@WebServlet(name = "initGeoPicDesktopServlet")
public class initGeoPicDesktopServlet extends HttpServlet {
    private PhotoInfo photoInfo;
    private UserInfo userInfo;
    private PhotoService photoService;
    private FaceService faceService;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String result = request.getParameter("result");
        userInfo = new UserInfo();
        userInfo.setUserDBName("db1");
        photoInfo = new PhotoInfo();
        JSONObject res = new JSONObject();
        photoService = new PhotoService();
        res = photoService.initGeoPicDesktop(photoInfo,userInfo);
        faceService = new FaceService();
        int faceCount = faceService.getFaceCount(userInfo);
        res.put("faceCount",faceCount);
        out.write(res.toString());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);

    }
}
