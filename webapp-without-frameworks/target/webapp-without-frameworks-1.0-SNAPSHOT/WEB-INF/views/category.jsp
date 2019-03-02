<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: fanzo
  Date: 2019-02-21
  Time: 22:50
  To change this template use File | Settings | File Templates.

  category?c_id=${category.categoryId}&


--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Category</title>
</head>
<body>
    <header>
        <button><a href="<c:url value="/servlet/logout"/>">logout</a></button>
    </header>
    <h3>Category name: <c:out value="${category.categoryName}"/></h3>
    <p>Description: <c:out value="${category.description}"/></p>
    <c:forEach items="${category.products}" var="p">

        <p>Products: <a href="<c:url value="/servlet/product?p_id=${p.productName}"/>"><c:out value="${p.productName}"/></a></p>
    </c:forEach>

</body>
</html>
