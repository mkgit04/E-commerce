<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>Error</title>
  <link rel="stylesheet" href="css/styles.css" />
</head>
<body>

<main class="page-shell">
  <div class="page-container detail-layout">
    <section class="detail-card stack">
      <div>
        <c:set var="statusCode" value="${requestScope['jakarta.servlet.error.status_code']}" />
        <c:set var="message" value="${requestScope['jakarta.servlet.error.message']}" />
        <c:set var="exception" value="${requestScope['jakarta.servlet.error.exception']}" />

        <p class="pill">
          <c:choose>
            <c:when test="${not empty statusCode}">${statusCode}</c:when>
            <c:otherwise>Error</c:otherwise>
          </c:choose>
        </p>
        <h1 class="detail-title">Something went wrong</h1>
        <p class="muted">
          <c:choose>
            <c:when test="${not empty message}">${message}</c:when>
            <c:when test="${not empty exception}">${exception.message}</c:when>
            <c:otherwise>We could not complete your request.</c:otherwise>
          </c:choose>
        </p>
      </div>

      <div class="toolbar-group">
        <a class="button-link" href="ProductsMain">Back to products</a>
        <a class="button-link ghost" href="login.jsp">Go to login</a>
      </div>
    </section>
  </div>
</main>

</body>
</html>