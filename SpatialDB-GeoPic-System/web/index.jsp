<%--
  Created by IntelliJ IDEA.
  User: Miodeat
  Date: 2020/7/13
  Time: 10:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>GeoPic</title>
    <script src="support/jquery-3.4.1.min.js"></script>
    <script src="https://webapi.amap.com/maps?v=1.4.15&key=0a2e5f7264c791daee51483cb024a8b8"></script>
    <script src="static/js/MapControl.js"></script>
    <script>
      $(document).ready(function () {
        new MapControl({
          div: "map"
        });
      })
    </script>
  </head>
  <body>
    <div id="map"></div>
  </body>
</html>
