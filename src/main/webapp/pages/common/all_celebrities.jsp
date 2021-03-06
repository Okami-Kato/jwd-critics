<%@ taglib prefix="ctg" uri="customtag" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<c:set var="currentPage" value="/pages/common/all_celebrities.jsp" scope="request"/>
<c:choose>
    <c:when test="${not empty sessionScope.lang}">
        <fmt:setLocale value="${sessionScope.lang}" scope="session"/>
    </c:when>
    <c:otherwise>
        <fmt:setLocale value="en" scope="session"/>
    </c:otherwise>
</c:choose>
<fmt:setBundle basename="properties/content"/>
<html>
<head>
    <title>All celebrities</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/update.css">
</head>
<body>
<c:import url="/pages/componets/header.jsp"/>
<c:import url="/pages/componets/message.jsp"/>
<body>
<div class="container mt-2">
    <ctg:all_celebrities/>
</div>
</body>
</html>
