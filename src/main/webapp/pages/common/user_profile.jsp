<%@ taglib prefix="ctg" uri="customtag" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<c:set var="currentPage" value="/pages/common/user_profile.jsp" scope="request"/>
<fmt:setLocale value="${sessionScope.lang}" scope="session"/>
<fmt:setBundle basename="properties/content"/>
<html>
<head>
    <title>Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user_profile.css">
</head>
<body>
<c:import url="/pages/componets/header.jsp"/>
<c:import url="/pages/componets/message.jsp"/>
<div class="container mt-5">
    <div class="row">
        <div class="col-4">
            <img src="${userProfile.imagePath}" alt="${userProfile.firstName}" class="img-thumbnail">
        </div>
        <div class="col-4">
            <h4>${userProfile.firstName} ${userProfile.lastName}</h4>
            <strong><fmt:message key="user.role"/>:</strong> ${userProfile.role}<br>
            <strong><fmt:message key="user.status"/>:</strong> ${userProfile.status}<br>
            <strong><fmt:message key="user.reviewCount"/>:</strong> ${userProfile.reviewCount}<br>
            <c:choose>
                <c:when test="${userProfile.id eq user.id}">
                    <a href="${pageContext.request.contextPath}/controller?command=open_update_user&userId=${user.id}&currentPage=${currentPage}">
                        <fmt:message key="command.update"/>
                    </a>
                </c:when>
                <c:otherwise>
                    <c:if test="${user.role eq 'ADMIN'}">
                        <c:if test="${userProfile.status eq 'BANNED'}">
                            <a href="${pageContext.request.contextPath}/controller?command=update_user_status&userId=${userProfile.id}&newStatus=active&currentPage=${currentPage}">
                                <fmt:message key="command.unban"/>
                            </a>
                        </c:if>
                        <c:if test="${userProfile.status eq 'ACTIVE'}">
                            <a href="${pageContext.request.contextPath}/controller?command=update_user_status&userId=${userProfile.id}&&newStatus=banned&currentPage=${currentPage}">
                                <fmt:message key="command.ban"/>
                            </a>
                        </c:if>
                    </c:if>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <c:if test="${not empty userProfile.reviews}">
        <div class="container mt-5">
            <c:forEach var="review" items="${userProfile.reviews}">
                <div class="row mt-4">
                    <div class="col-1">
                        <a href="${pageContext.request.contextPath}/controller?command=open_movie&movieId=${review.movieId}">
                            <img class="img-thumbnail" src="${review.imagePath}" alt="${review.title}">
                        </a>
                    </div>
                    <div class="col">
                            ${review.title}<br>
                        <fmt:message key="review.score"/>: ${review.score}<br>
                            ${review.text}
                    </div>
                    <c:if test="${user.role eq 'ADMIN'}">
                        <div class="col-1">
                            <a href="${pageContext.request.contextPath}/controller?command=delete_movie_review&movieReviewId=${review.id}&currentPage=${currentPage}">
                                Delete
                            </a>
                        </div>
                    </c:if>
                </div>
            </c:forEach>
        </div>
    </c:if>
</div>
</body>
</html>