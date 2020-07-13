package servlets;

import domain.UserInfo;
import net.sf.json.JSONObject;
import services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "UserLoginServlet")
public class UserLoginServlet extends HttpServlet {
    private UserInfo userInfo;
    private UserService userService;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        userInfo = new UserInfo();
        userInfo.setPassword(password);
        userInfo.setUsername(username);
        userService = new UserService();
        JSONObject Res = new JSONObject();
        Res = userService.loginByUserNameAndPassword(userInfo);
        out.write(Res.toString());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
