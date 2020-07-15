package servlets;

import domain.FaceInfo;
import domain.UserInfo;
import net.sf.json.JSONObject;
import services.FaceService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "upLoadFaceLabelServlet")
public class upLoadFaceLabelServlet extends HttpServlet {
    private FaceService faceService;
    private UserInfo userInfo;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String faceLabel = request.getParameter("faceLabel");
        String facePath = request.getParameter("facePath");
        String userDbname = request.getParameter("userDbname");
        userInfo = new UserInfo();
        userInfo.setUserDBName(userDbname);
        JSONObject res = new JSONObject();
        faceService =new FaceService();
        FaceInfo faceInfo = new FaceInfo();
        faceInfo.setFacePath(facePath);
        faceInfo.setFaceLabel(faceLabel);
        boolean updateFaceLabelRes = faceService.updateFaceLabelToDB(faceInfo,userInfo);
        if(updateFaceLabelRes){
            res.put("message","success");
//            res.put("success",true);
        }else{
            res.put("message","failure");
//            res.put("success",false);
        }
        out.write(res.toString());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
