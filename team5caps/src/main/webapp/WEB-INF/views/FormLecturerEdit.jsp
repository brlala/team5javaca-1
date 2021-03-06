<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<link href="<c:url value='/styles/style.css'/>" rel="stylesheet"
	type="text/css" />
	<script src="webjars/bootstrap/4.1.3/js/bootstrap.min.js"></script>
<link href="webjars/bootstrap/4.1.3/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body>
	<div class="container">
<form:form method="POST" modelAttribute="lecturer"
	action="${pageContext.request.contextPath}/admin/lecturer/edit/${lecturer.lecturerID}">
		<center>
			<table cellpadding=4 cellspacing=2 border=0>
				<tr>
					<th width="45%">Description</th>
					<th width="55%">Detail</th>
				</tr>
				<tr>
				   <td><s:message code="LecturerID" /> *</td>
				   <td><form:input path="lecturerID" readonly="true" disabled="true"/></td>
				 </tr>
				<tr>
				   <td><s:message code="FirstName" /></td>
				   <td><form:input path="user.firstName"/></td>
				 </tr>
				
				<tr>
				   <td><s:message code="LastName" /></td>
				   <td><form:input path="user.LastName"/></td>
				 </tr>
				 
				<tr>
				   <td><s:message code="EmailAddress" /></td>
				   <td><form:input path="user.emailAddress"/></td>
				 </tr>
				<tr>
				   <td><s:message code="Position" /></td>
				   <td><form:input path="position"/></td>
				 </tr>
				
				 <tr>
				 <td><input type="submit" value="Submit"> </td>
				 <td><input type="reset" value="Reset"></td>
				 </tr>
		</table>
		</center>
	
	</form:form>
	</div>
</body>
</html>