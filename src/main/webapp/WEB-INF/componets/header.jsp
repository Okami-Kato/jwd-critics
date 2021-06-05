<%--
  Created by IntelliJ IDEA.
  User: barka
  Date: 5/31/2021
  Time: 11:32 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css"/>
    <link href="https://fonts.googleapis.com/css?family=Ubuntu" rel="stylesheet">
</head>
<body>
<div class="header">
    <a href="${pageContext.request.contextPath}/controller" class="logo"><img src="../../assets/camera-icon.svg"
                                                                              width="68" height="68"></a>
    <div class="header-right">
        <a class="outlined" href="${pageContext.request.contextPath}/controller?command=show_login"><span>Log In</span></a>
    </div>
</div>
</body>
</html>
