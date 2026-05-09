<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Products</title>
    <link rel="stylesheet" href="css/styles.css" />
</head>
<body>

<main class="page-shell">
    <div class="page-container stack">
        <section class="hero-card stack">
            <div class="toolbar">
                <div>
                    <p class="pill">Product center</p>
                    <h1 class="hero-title">Product list</h1>
                    <p class="muted">Logged in as <strong>${user}</strong></p>
                </div>
                <div class="toolbar-group">
                    <a class="button-link ghost" href="logout">Sign out</a>
                    <form method="post" action="delete-account">
                        <button type="submit" class="button-link secondary">Delete account</button>
                    </form>
                </div>
            </div>
        </section>

        <c:if test="${isAdmin}">
            <section class="panel admin-panel">
                <div>
                    <h2 class="section-title">Admin actions</h2>
                    <p class="muted">Manage catalog items from one place.</p>
                </div>
                <form method="post" action="admin/products/add" class="form-stack">
                    <div class="field-row split-grid">
                        <input type="text" name="name" placeholder="Product name" required />
                        <input type="number" step="0.01" name="price" placeholder="Price" required />
                    </div>
                    <button type="submit">Add product</button>
                </form>
                <form method="post" action="admin/products/delete" class="form-stack">
                    <input type="number" name="id" placeholder="Product ID" required />
                    <button type="submit" class="button-link secondary">Delete product</button>
                </form>
            </section>
        </c:if>

        <hr />

        <section class="panel stack">
            <div>
                <h2 class="section-title">Reviews / Feedback</h2>
                <p class="muted">Review submission can be attached to a reviews table in the next backend iteration.</p>
            </div>
        </section>

        <section class="product-grid">
            <c:forEach var="item" items="${data}">
                <article class="product-card">
                    <div class="product-id">#${item.id}</div>
                    <div class="product-meta">
                        <div><strong>${item.name}</strong></div>
                        <div>$${item.price}</div>
                    </div>
                    <a class="button-link ghost" href="product?id=${item.id}">View details</a>
                </article>
            </c:forEach>
        </section>
    </div>
</main>
</body>
</html>
