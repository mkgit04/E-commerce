<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>Invalid User</title>
  <link rel="stylesheet" href="css/styles.css" />
</head>
<body>

<main class="page-shell">
  <div class="page-container detail-layout">
    <section class="detail-card stack">
      <div>
        <p class="pill">Access denied</p>
        <h1 class="detail-title">Invalid user</h1>
        <p class="muted">
          <c:choose>
            <c:when test="${param.reason eq 'invalidCredentials'}">The username or password you entered is not valid.</c:when>
            <c:when test="${param.reason eq 'accountExists'}">That account already exists.</c:when>
            <c:when test="${param.reason eq 'missingFields'}">Please fill in both username and password.</c:when>
            <c:otherwise>We could not complete your request.</c:otherwise>
          </c:choose>
        </p>
      </div>

      <div class="toolbar-group">
        <a class="button-link" href="login.jsp">Go to login</a>
        <a class="button-link ghost" href="register.jsp">Go to sign up</a>
      </div>
    </section>
  </div>
</main>

</body>
</html>