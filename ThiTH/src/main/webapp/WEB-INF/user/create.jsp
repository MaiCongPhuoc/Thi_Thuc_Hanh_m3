<%--
  Created by IntelliJ IDEA.
  User: ADMIN
  Date: 7/13/2022
  Time: 9:00 PM
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
        <a href="student?action=student">List All Product</a>
    </h2>
</center>
<div>
    <c:if test="${requestScope.successful != null}">
        <h1>${requestScope.successful}</h1>
    </c:if>
</div>
<div align="center">
    <form method="post" action="/product?action=create">
        <table border="1" cellpadding="5">
            <caption>
                <h2>
                    Chinh sua Product
                </h2>
            </caption>
            <c:if test="${requestScope.user != null}">
                <input type="hidden" name="id" value="<c:out value='${product.getId()}' />"/>
            </c:if>
            <tr>
                <th>Name:</th>
                <td>
                    <input type="text" name="productName" size="45"
                           value=""/>
                    <span style="color: red;font-size: 10px;">${errorName}</span>
                </td>
            </tr>
            <tr>
                <th>Price:</th>
                <td>
                    <input type="number" name="price" size="45" value=""/>
                    <span style="color: red;font-size: 10px;">${errorPrice}</span>
                </td>
            </tr>
            <tr>
                <th>Quantity:</th>
                <td>
                    <input type="number" name="quantity" size="45"
                           value=""
                    />
                    <span style="color: red;font-size: 10px;">${errorQuantity}</span>
                </td>
            </tr>
            <tr>
                <th>Color:</th>
                <td>
                    <input type="text" name="color" size="45"
                           value=""
                    />
                    <span style="color: red;font-size: 10px;">${errorCountry}</span>
                </td>
            </tr>
            <tr>
                <th>Description:</th>
                <td>
                    <input type="text" name="description" size="45"
                           value=""
                    />
                    <span style="color: red;font-size: 10px;">${errorCountry}</span>
                </td>
            </tr>
            <tr>
                <th>Category:</th>
                <td>
                    <select name="category">
                        <c:forEach var="category" items="${applicationScope.listColor}">
                            <option value="${category.getId()}">${category.getCategory()}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <input type="submit" value="Add"/>
                </td>
            </tr>
        </table>
        <div>
            ${errors}
        </div>
    </form>
</div>
</body>
</html>
