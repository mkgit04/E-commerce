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
          <form method="post" action="login" class="form-stack">
            <input type="text" name="username" placeholder="Username" required />
            <input type="password" name="password" placeholder="Password" required />
            <button type="submit">Login</button>
          </form>
          <div>
            <a class="button-link ghost" href="register.jsp">Create account</a>
          </div>
        </section>
      </div>
    </section>
  </div>
</main>

</body>
</html>
