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



        <hr />
        <section class="panel stack">
            <div>
                <h2 class="section-title">All Products</h2>
                <p class="muted">All products at database listed here.</p>
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
                    <div class="toolbar-group">
                        <a class="button-link ghost" href="product?id=${item.id}">Edit</a>
                        <form method="post" action="products/delete">
                            <input type="hidden" name="id" value="${item.id}" />
                            <button type="submit" class="button-link secondary">Remove</button>
                        </form>
                    </div>
                    <div class="toolbar-group">
                        <a class="button-link" href="${pageContext.request.contextPath}/reviews/new?productId=${item.id}">Submit review</a>
                        <a class="button-link ghost" href="${pageContext.request.contextPath}/reviews/view?productId=${item.id}">View reviews</a>
                    </div>
                </article>
            </c:forEach>
        </section>
        <hr />
        <section class="panel admin-panel">
            <div>
                <h2 class="section-title">Manage products</h2>
                <p class="muted">Add, delete, or open a product to edit its details.</p>
            </div>
            <form method="post" action="products/add" class="form-stack">
                <div class="field-row split-grid">
                    <input type="text" name="name" placeholder="Product name" required />
                    <input type="number" step="0.01" name="price" placeholder="Price" required />
                </div>
                <button type="submit">Add product</button>
            </form>
        </section>
    </div>
</main>
</body>
</html>
