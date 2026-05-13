<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>Error</title>
  <style>
    *{box-sizing:border-box}
    html,body{min-height:100%}
    body{margin:0;font-family:"Segoe UI", Tahoma, Geneva, Verdana, sans-serif;color:#1f2937;background:radial-gradient(circle at top left, rgba(255,255,255,0.75), transparent 34%), linear-gradient(180deg,#f8fafc 0%,#eef2ff 100%)}
    a{color:#2563eb;text-decoration:none}
    a:hover{text-decoration:underline}
    .page-shell{min-height:100vh;padding:32px 16px}
    .page-container{width:min(1120px,100%);margin:0 auto}
    .detail-card{background:rgba(255,255,255,0.88);border:1px solid rgba(148,163,184,0.22);border-radius:24px;box-shadow:0 24px 60px rgba(15,23,42,0.08);backdrop-filter:blur(12px);padding:28px}
    .detail-title{margin:0 0 10px;color:#0f172a;letter-spacing:-0.02em;font-size:clamp(1.25rem,2.2vw,1.75rem)}
    .muted{margin:0;color:#64748b}
    .stack{display:grid;gap:20px}
    .toolbar-group{display:flex;flex-wrap:wrap;gap:12px;align-items:center}
    .button-link,button{display:inline-flex;align-items:center;justify-content:center;gap:8px;min-height:46px;padding:0 18px;border:0;border-radius:999px;background:linear-gradient(135deg,#2563eb,#4f46e5);color:#fff;font-weight:700;cursor:pointer;transition:transform 160ms ease,box-shadow 160ms ease,filter 160ms ease}
    .button-link.ghost{background:rgba(15,23,42,0.08);color:#0f172a;box-shadow:none}
    .pill{display:inline-flex;align-items:center;gap:8px;padding:8px 14px;border-radius:999px;background:rgba(37,99,235,0.08);color:#1d4ed8;font-weight:700}
    .detail-layout{width:min(720px,100%);margin:0 auto}
    pre{white-space:pre-wrap;background:#f7f7f7;padding:10px;border-radius:4px}
    @media (max-width:640px){.page-shell{padding:20px 12px}.detail-card{padding:20px;border-radius:20px}}
  </style>
</head>
<body>

<main class="page-shell">
  <div class="page-container detail-layout">
    <section class="detail-card stack">
      <div>
        <c:set var="statusCode" value="${requestScope['jakarta.servlet.error.status_code']}" />
        <c:set var="message" value="${requestScope['jakarta.servlet.error.message']}" />
        <c:set var="exception" value="${requestScope['jakarta.servlet.error.exception']}" />

        <p class="pill">
          <c:choose>
            <c:when test="${not empty statusCode}">${statusCode}</c:when>
            <c:otherwise>Error</c:otherwise>
          </c:choose>
        </p>

        <h1 class="detail-title">Something went wrong</h1>

        <p class="muted">
          <c:choose>
            <c:when test="${statusCode == 429}">You have sent too many requests in a short period. Please wait a moment and try again.</c:when>
            <c:when test="${not empty message}">${message}</c:when>
            <c:when test="${not empty exception}">${exception.message}</c:when>
            <c:otherwise>We could not complete your request.</c:otherwise>
          </c:choose>
        </p>


      </div>

      <div class="toolbar-group">
        <a class="button-link" href="${pageContext.request.contextPath}/ProductsMain">Back to products</a>
      </div>
    </section>
  </div>
</main>

</body>
</html>