<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Login</title>
  <link rel="stylesheet" href="css/styles.css" />
</head>

<body>

<main class="page-shell">
  <div class="page-container">
    <section class="hero-card stack">
      <div>
        <p class="pill">Welcome back</p>
        <h1 class="hero-title">Access your product dashboard</h1>
        <p class="muted">Sign in to manage products, or create a new account to get started.</p>
      </div>

      <div class="auth-grid" style="grid-template-columns: minmax(0, 1fr);">
        <section class="panel">
          <div class="panel-header">
            <h2 class="section-title">Login</h2>
            <p class="muted">Use your existing account credentials.</p>
          </div>
          <c:if test="${not empty error and fn:trim(error) ne ''}">
            <div style="padding: 12px 16px; border-radius: 10px; background: rgba(239, 68, 68, 0.1); border: 1px solid rgba(239, 68, 68, 0.3); color: #dc2626; font-weight: 600; margin-bottom: 16px;">
              ${error}
            </div>
          </c:if>
          <form method="post" action="login" class="form-stack">
            <input type="text" name="username" placeholder="Enter username" required />
            <input type="password" name="password" placeholder="Enter password" required />
            <button type="submit">Login</button>
          </form>
          <div>
            <a class="button-link ghost" href="${pageContext.request.contextPath}/register.jsp">Create account</a>
          </div>
        </section>
      </div>
    </section>
  </div>
</main>

</body>
</html>
