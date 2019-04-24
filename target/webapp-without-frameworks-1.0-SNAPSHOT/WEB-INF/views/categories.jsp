<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: fanzo
  Date: 2019-02-25
  Time: 22:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Categories</title>
</head>
<body>
    <header>
        <button><a href="<c:url value="/servlet/logout"/>">logout</a></button>
    </header>
<h1>Categories</h1>

    <c:forEach items="${categories}" var="c">
        <h3>Category name: <a href="<c:url value="/servlet/category?c_id=${c.id}"/>"><c:out value="${c.categoryName}"/></a></h3>
    </c:forEach>
</body>
</html>