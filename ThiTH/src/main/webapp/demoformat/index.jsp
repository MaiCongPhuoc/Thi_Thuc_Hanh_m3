<%--
  Created by IntelliJ IDEA.
  User: ADMIN
  Date: 7/24/2022
  Time: 8:33 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale value="de_CH" scope="session"/>
<html>

<head>
    <title>Title</title>
</head>
<body>
<h1>${time}</h1>
<h1>${money}</h1>
<fmt:formatNumber value="${nf}" type="currency" />

</body>
</html>
