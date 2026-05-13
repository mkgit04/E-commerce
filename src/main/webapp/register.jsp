<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:if test="${not empty sessionScope.user or not empty cookie['SESSION_ID']}">
  <c:redirect url="ProductsMain" />
</c:if>
<!DOCTYPE html>
<html>
<head>
  <title>Sign Up</title>
  <link rel="stylesheet" href="css/styles.css" />
</head>
<body>

<main class="page-shell">
  <div class="page-container">
    <section class="hero-card stack">
      <div>
        <p class="pill">Create account</p>
        <h1 class="hero-title">Start using the store</h1>
        <p class="muted">Register a new user account, then sign in to manage products.</p>
      </div>

      <section class="panel">
        <div class="panel-header">
          <h2 class="section-title">Sign up</h2>
          <p class="muted">Create your username and password.</p>
        </div>
        <c:if test="${not empty error and fn:trim(error) ne ''}">
          <div style="padding: 12px 16px; border-radius: 10px; background: rgba(239, 68, 68, 0.1); border: 1px solid rgba(239, 68, 68, 0.3); color: #dc2626; font-weight: 600; margin-bottom: 16px;">
            ${error}
          </div>
        </c:if>
        <form method="post" action="register" class="form-stack">
          <div class="field-row">
            <input type="text" name="username" placeholder="Enter username" required />
            <small class="muted">Use 3 to 50 characters with letters, numbers, or underscore.</small>
          </div>
          <div class="field-row">
            <input type="password" name="password" placeholder="Enter password" required />
            <small class="muted">Use at least 6 characters.</small>
          </div>
          <button type="submit">Create account</button>
        </form>
        <div>
          <a class="button-link ghost" href="${pageContext.request.contextPath}/login.jsp">Back to login</a>
        </div>
      </section>
    </section>
  </div>
</main>

</body>
</html>