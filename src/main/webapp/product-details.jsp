<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Product Details</title>
    <link rel="stylesheet" href="css/styles.css" />
</head>
<body>

<main class="page-shell">
    <div class="page-container detail-layout">
        <section class="detail-card stack">
            <div>
                <p class="pill">Product details</p>
                <h1 class="detail-title">${product.name}</h1>
                <p class="muted">A quick view of the selected catalog item.</p>
            </div>

            <div class="detail-list">
                <div class="detail-row">
                    <span class="detail-label">ID</span>
                    <span class="detail-value">${product.id}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Name</span>
                    <span class="detail-value">${product.name}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Price</span>
                    <span class="detail-value">${product.price}</span>
                </div>
            </div>

            <section class="panel">
                <div class="panel-header">
                    <h2 class="section-title">Edit product</h2>
                    <p class="muted">Update the name and price, then save your changes.</p>
                </div>
                <form method="post" action="${pageContext.request.contextPath}/products/update" class="form-stack">
                    <input type="hidden" name="id" value="${product.id}" />
                    <input type="text" name="name" value="${product.name}" required />
                    <input type="number" step="0.01" name="price" value="${product.price}" required />
                    <button type="submit">Save changes</button>
                </form>
            </section>

            <div class="toolbar-group">
                <a class="button-link ghost" href="${pageContext.request.contextPath}/ProductsMain">Back to products</a>
                <form method="post" action="${pageContext.request.contextPath}/products/delete">
                    <input type="hidden" name="id" value="${product.id}" />
                    <button type="submit" class="button-link secondary">Delete product</button>
                </form>
            </div>
        </section>
    </div>
</main>
</body>
</html>
