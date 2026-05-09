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

            <div>
                <a class="button-link ghost" href="ProductsMain">Back to products</a>
            </div>
        </section>
    </div>
</main>
</body>
</html>
