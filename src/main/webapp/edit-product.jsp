<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Product</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif; background: radial-gradient(circle at top, rgba(255,255,255,0.8), rgba(230,240,255,0.4)), linear-gradient(135deg, #ffffff 0%, #f3f4f6 100%); color: #1f2937; min-height: 100vh; }
        .page-shell { min-height: 100vh; padding: 32px 16px; }
        .page-container { width: min(1120px, 100%); margin: 0 auto; }
        .detail-layout { width: min(720px, 100%); margin: 0 auto; }
        .stack { display: grid; gap: 20px; }
        .detail-card, .panel { background: rgba(255, 255, 255, 0.88); border: 1px solid rgba(148, 163, 184, 0.22); border-radius: 24px; box-shadow: 0 24px 60px rgba(15, 23, 42, 0.08); backdrop-filter: blur(12px); padding: 28px; }
        .pill { display: inline-flex; align-items: center; gap: 8px; padding: 8px 14px; border-radius: 999px; background: rgba(37, 99, 235, 0.08); color: #1d4ed8; font-weight: 700; width: fit-content; }
        .detail-title { font-size: clamp(1.25rem, 2.2vw, 1.75rem); margin: 0 0 10px; color: #0f172a; letter-spacing: -0.02em; }
        .muted { margin: 0; color: #64748b; }
        .section-title { font-size: clamp(1.25rem, 2.2vw, 1.75rem); margin: 0 0 10px; color: #0f172a; letter-spacing: -0.02em; }
        .panel-header { margin-bottom: 18px; }
        .form-stack { display: grid; gap: 12px; }
        .form-label { display: block; color: #0f172a; font-size: 0.95rem; font-weight: 600; margin-bottom: 8px; }
        input, select, textarea { width: 100%; padding: 14px 16px; border: 1px solid #cbd5e1; border-radius: 14px; background: rgba(255, 255, 255, 0.95); color: #0f172a; font: inherit; transition: border-color 160ms ease, box-shadow 160ms ease; }
        input:focus, select:focus, textarea:focus { outline: none; border-color: #2563eb; box-shadow: 0 0 0 4px rgba(37, 99, 235, 0.12); }
        button, .button-link { display: inline-flex; align-items: center; justify-content: center; gap: 8px; min-height: 46px; padding: 0 18px; border: 0; border-radius: 999px; background: linear-gradient(135deg, #2563eb, #4f46e5); color: #ffffff; font-weight: 700; cursor: pointer; text-decoration: none; transition: transform 160ms ease, box-shadow 160ms ease; }
        button:hover, .button-link:hover { text-decoration: none; transform: translateY(-1px); box-shadow: 0 16px 30px rgba(37, 99, 235, 0.2); }
        .button-link.secondary { background: linear-gradient(135deg, #0f172a, #334155); }
        .button-link.ghost { background: rgba(15, 23, 42, 0.08); color: #0f172a; box-shadow: none; }
        .toolbar-group { display: flex; flex-wrap: wrap; gap: 12px; align-items: center; width: 100%; }
        a { color: #2563eb; text-decoration: none; }
        a:hover { text-decoration: underline; }
        @media (max-width: 640px) { .page-shell { padding: 20px 12px; } .detail-card, .panel { padding: 20px; border-radius: 20px; } button, .button-link { width: 100%; } }
    </style>
</head>
<body>

<main class="page-shell">
    <div class="page-container detail-layout">
        <section class="detail-card stack">
            <div>
                <p class="pill">Edit product</p>
                <h1 class="detail-title">${product.name}</h1>
                <p class="muted">Update the product name and price, then save your changes.</p>
            </div>

            <section class="panel">
                <div class="panel-header">
                    <h2 class="section-title">Product information</h2>
                    <p class="muted">Change the name and price for this product.</p>
                </div>
                <c:if test="${not empty error}">
                  <div style="padding: 12px 16px; border-radius: 10px; background: rgba(239, 68, 68, 0.1); border: 1px solid rgba(239, 68, 68, 0.3); color: #dc2626; font-weight: 600; margin-bottom: 16px;">
                    ${error}
                  </div>
                </c:if>
                <form method="post" action="${pageContext.request.contextPath}/products/update" class="form-stack">
                    <input type="hidden" name="id" value="${product.id}" />
                    <div>
                        <label class="form-label">Product name</label>
                        <input type="text" name="name" value="${product.name}" required />
                    </div>
                    <div>
                        <label class="form-label">Price</label>
                        <input type="number" step="0.01" name="price" value="${product.price}" required />
                    </div>
                    <button type="submit">Save changes</button>
                </form>
            </section>

            <div class="toolbar-group">
                <a class="button-link ghost" href="${pageContext.request.contextPath}/product?id=${product.id}">View product</a>
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
