<%--
  Created by IntelliJ IDEA.
  User: Miodeat
  Date: 2020/7/13
  Time: 10:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html class="no-js" lang="en">
  <head>
    <title>GeoPic</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Sufee Admin - HTML5 Admin Template</title>
    <meta name="description" content="Sufee Admin - HTML5 Admin Template">
    <meta name="viewport" content="width=device-width, initial-scale=1">
<%--    <link rel="apple-touch-icon" href="apple-icon.png">--%>
    <link rel="shortcut icon" href="static/images/favicon.ico">
    <link rel="stylesheet" href="static/vendors/bootstrap/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="static/vendors/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="static/vendors/themify-icons/css/themify-icons.css">
<%--    <link rel="stylesheet" href="static/vendors/flag-icon-css/css/flag-icon.min.css">--%>
<%--    <link rel="stylesheet" href="static/vendors/selectFX/css/cs-skin-elastic.css">--%>
<%--    <link rel="stylesheet" href="static/assets/css/style.css">--%>
    <script src="support/jquery-3.4.1.min.js"></script>
<%--    <script src="https://webapi.amap.com/maps?v=1.4.15&key=0a2e5f7264c791daee51483cb024a8b8"></script>--%>
    <script src="static/js/MapControl.js"></script>
    <script type="text/javascript" src="static/js/RegisterAndLogin.js"></script>
    <script>
      // $(document).ready(function () {
      //   new MapControl({
      //     div: "map"
      //   });
      // })
      var register = new RegisterAnfLogin();
    </script>
  </head>
  <body class="bg-dark">
  <div class="sufee-login d-flex align-content-center flex-wrap">
    <div class="container">
      <div class="login-content">
        <div class="login-logo">
          <a href="static/html/index.html">
            <img class="align-content" src="static/images/logo.png" alt="">
          </a>
        </div>
        <div class="login-form">
<%--          <form>--%>
            <div class="form-group">
              <label>用户名&nbsp;</label>
              <input type="text" class="form-control usernameInput" placeholder="User Name">
            </div>
            <div class="form-group">
              <label>密码&nbsp;</label>
              <input type="password" class="form-control passwordInput" placeholder="Password">
            </div>

            <button type="submit" class="btn btn-primary btn-flat m-b-30 m-t-30 btnSubmit" onclick="register.register()">注册&nbsp;</button>
            <div class="register-link m-t-15 text-center">
              <p>已经有账号了? <a href="static/html/login.html"> 点击登录 &nbsp;</a></p>
            </div>
<%--          </form>--%>
        </div>
      </div>
    </div>
  </div>
  <script src="static/vendors/jquery/dist/jquery.min.js"></script>
  <script src="static/vendors/popper.js/dist/umd/popper.min.js"></script>
  <script src="static/vendors/bootstrap/dist/js/bootstrap.min.js"></script>
  <script src="static/assets/js/main.js"></script>
  </body>
</html>
