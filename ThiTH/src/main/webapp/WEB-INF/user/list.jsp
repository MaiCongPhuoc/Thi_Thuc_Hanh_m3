<%--
  Created by IntelliJ IDEA.
  User: ADMIN
  Date: 7/13/2022
  Time: 9:01 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link href="user/css/style.css" type="text/css" rel="stylesheet">
    <title>Product Management Application</title>
</head>
<body>
<center>
    <h1>Quản lí Product</h1>
    <h2>
        <a href="/product?action=create">Thêm Product mới</a>
    </h2>
</center>
<div align="center">
    <%-- search--%>
    <form method="post" action="/student">
        <label>Search: </label>
        <input type="text" hint="search" value="${requestScope.s}" name="s"/>
        <label>Submit: </label>
        <button type="get">Submit</button>
    </form>


    <table border="1" cellpadding="5">
        <caption><h2>List of Product</h2></caption>
        <tr>
            <th>#</th>
            <th>Product Name</th>
            <th>Price</th>
            <th>Quantity</th>
            <th>Color</th>
            <th>Category</th>
            <th>Actions</th>
        </tr>
        <c:forEach var="product" items="${requestScope.listProduct}">
        <tr>
            <td><c:out value="${product.getId()}"/></td>
            <td><c:out value="${product.getProductName()}"/></td>
            <td><c:out value="${product.getPrice()}"/></td>
            <td><c:out value="${product.getQuantity()}"/></td>
            <td><c:out value="${product.getColor()}"/></td>
            <td>
                <c:forEach items="${applicationScope.listColor}" var="color">
                    <c:if test="${color.getId() == product.getIdcategory()}">
                        <c:out value="${color.getCategory()}"/>
                    </c:if>
                </c:forEach>
            </td>
            <td>
                <a href="/product?action=edit&id=${product.getId()}">Edit</a>
                <a href="/product?action=delete&id=${product.getId()}">Delete</a>
            </td>
        </tr>
        </c:forEach>
</div>
</body>
</html>
