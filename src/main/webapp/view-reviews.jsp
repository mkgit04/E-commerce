<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>View Reviews</title>
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
        a { color: #2563eb; text-decoration: none; }
        a:hover { text-decoration: underline; }
        .panel-header { margin-bottom: 18px; }
        .section-title { font-size: clamp(1.25rem, 2.2vw, 1.75rem); margin: 0 0 10px; color: #0f172a; letter-spacing: -0.02em; }
        .review-grid { display: grid; gap: 12px; }
        .review-card { padding: 14px; border-radius: 14px; border: 1px solid rgba(148, 163, 184, 0.22); background: rgba(255, 255, 255, 0.7); display: grid; gap: 10px; }
        .review-header { display: flex; align-items: center; justify-content: space-between; gap: 12px; }
        .review-rating { color: #f59e0b; font-size: 0.9rem; font-weight: 600; margin-left: 6px; }
        .review-title { margin: 0; color: #0f172a; font-size: 1rem; font-weight: 700; }
        .review-comment { margin: 0; color: #475569; font-size: 0.95rem; line-height: 1.5; }
        .toolbar-group { display: flex; flex-wrap: wrap; gap: 12px; align-items: center; width: 100%; }
        .button-link { display: inline-flex; align-items: center; justify-content: center; gap: 8px; min-height: 46px; padding: 0 18px; border: 0; border-radius: 999px; background: linear-gradient(135deg, #2563eb, #4f46e5); color: #ffffff; font-weight: 700; cursor: pointer; text-decoration: none; width: 100%; }
        .button-link:hover { transform: translateY(-1px); box-shadow: 0 16px 30px rgba(37, 99, 235, 0.2); }
        .button-link.ghost { background: rgba(15, 23, 42, 0.08); color: #0f172a; box-shadow: none; }
        .button-link.ghost:hover { background: rgba(15, 23, 42, 0.12); }
        @media (max-width: 640px) { .page-shell { padding: 20px 12px; } .detail-card, .panel { padding: 20px; border-radius: 20px; } .button-link { width: 100%; } }
    </style>
</head>
<body>

<main class="page-shell">
    <div class="page-container detail-layout">
        <section class="detail-card stack">
            <div>
                <p class="pill">Product reviews</p>
                <h1 class="detail-title"><c:out value="${product.name}"/></h1>
                <p class="muted">Latest feedback from users.</p>
            </div>

            <section class="panel stack">
                <div class="panel-header">
                    <h2 class="section-title">Customer reviews</h2>
                    <p class="muted">See what other users think about this product.</p>
                </div>
                <c:choose>
                    <c:when test="${empty reviews}">
                        <p class="muted">No reviews yet for this product. <a href="${pageContext.request.contextPath}/reviews/new?productId=${product.id}">Be the first to review</a>.</p>
                    </c:when>
                    <c:otherwise>
                        <div class="review-grid">
                            <c:forEach var="review" items="${reviews}">
                                <article class="review-card">
                                    <div class="review-header">
                                        <div>
                                            <strong><c:out value="${review.username}"/></strong>
                                            <span class="review-rating">★<c:out value="${review.rating}"/>.0</span>
                                        </div>
                                        <span class="pill"><c:out value="${review.rating}"/>/5</span>
                                    </div>
                                    <c:if test="${not empty review.title}">
                                        <h4 class="review-title"><c:out value="${review.title}"/></h4>
                                    </c:if>
                                    <c:if test="${not empty review.comment}">
                                        <p class="review-comment"><c:out value="${review.comment}"/></p>
                                    </c:if>
                                </article>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </section>

            <div class="toolbar-group">
                <a class="button-link ghost" href="${pageContext.request.contextPath}/product?id=${product.id}">Back to product</a>
                <a class="button-link" href="${pageContext.request.contextPath}/reviews/new?productId=${product.id}">Submit review</a>
            </div>
        </section>
    </div>
</main>

</body>
</html>