<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>prnAckView</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/prnAckView.css" />
</head>
<body>
<div class="page-shell">
  <div class="page-header">
    <h2>prnAckView</h2>
  </div>
  <div class="page-body">
    <fieldset class="module-box">
      <legend>Input Details</legend>
      <div class="grid-row">
        <label>From</label><input type="text" />
        <label>To</label><input type="text" />
        <label>Reference No</label><input type="text" />
      </div>
      <div class="btn-row">
        <s:submit value="Refresh" cssClass="refresh-btn"/>
      </div>
    </fieldset>
    <fieldset class="module-box">
      <legend>Details</legend>
      <table class="module-table">
        <thead><tr><th>Status</th><th>Message</th><th>Date</th></tr></thead>
        <tbody><tr><td colspan="3">No records found</td></tr></tbody>
      </table>
    </fieldset>
  </div>
</div>
</body>
</html>
