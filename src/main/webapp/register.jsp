<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
        <form method="post" action="register" class="form-stack">
          <input type="text" name="username" placeholder="Username" required />
          <input type="password" name="password" placeholder="Password" required />
          <button type="submit">Create account</button>
        </form>
        <div>
          <a class="button-link ghost" href="login.jsp">Back to login</a>
        </div>
      </section>
    </section>
  </div>
</main>

</body>
</html>