<!DOCTYPE html>  
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<html lang="en">
  <head>
    <title>Hello Action</title>
    <meta charset="UTF-8"> 
  </head>
  <body>
    <h1>Hello Action</h1>
    <p>Hello, <c:out value="${name}" default="World" />!</p>
    <html:form action="/hello" onsubmit="disp_msg()">
      Input your name :
      <html:text property="name" size="15" maxlength="15" />
      <html:submit property="submit" value="Submit" />
    </html:form>
  </body>
</html>