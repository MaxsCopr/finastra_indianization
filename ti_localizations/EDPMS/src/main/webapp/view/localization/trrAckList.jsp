<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>AD Transfer Acknowledgement</title>

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/trrAckList.css">

</head>

<body>

	<div class="main-container">

		<!-- Header -->
		<div class="top-header">

			<div class="left-logo-section">
				<img
					src="${pageContext.request.contextPath}/images/logo.png"
					class="left-logo">
			</div>

    <div class="logo-right">
        <img src="images/finastra-logo.png" alt="Union Bank"/>
    </div>

		</div>

		<!-- Left Menu -->
		<div class="left-panel">
			<a class="menu-btn" type="button" href="acknowledgement">Close</a>
			<!-- <input type="button" value="Reset" class="menu-btn"> -->
		</div>


		<!-- Main Content -->
		<div class="content-panel">

			<div class="page-title">
				AD Transfer Acknowledgement
			</div>


			<!-- Input Details -->
			<fieldset class="section-box">

				<legend>Input Details</legend>

				<table class="form-table">

					<tr>

						<td class="label">TRR Created From</td>

						<td>
							<input type="text" class="input-field">
						</td>

						<td class="label">To</td>

						<td>
							<input type="text" class="input-field">
						</td>

						<td class="label">Shipping Bill No</td>

						<td>
							<input type="text"
								   value="12345647"
								   class="input-field">
						</td>

					</tr>


					<tr>

						<td class="label">Shipping Bill Date</td>

						<td>
							<input type="text" class="input-field">
						</td>

						<td></td>
						<td></td>

						<td class="label">Port Code</td>

						<td>
							<input type="text"
								   value="569874"
								   class="input-field">
						</td>

					</tr>


					<tr>

						<td class="label">Form No</td>

						<td>
							<input type="text"
								   value="54"
								   class="small-input">
						</td>

						<td></td>
						<td></td>

						<td></td>

						<td>
							<s:submit value="Refresh"
							          action="trrAckAction"
							          cssClass="refresh-btn"/>
						</td>

					</tr>

				</table>

			</fieldset>



			<!-- TRR Details -->
			<fieldset class="section-box">

				<legend>TRR Acknowledgement Details</legend>

				<div class="table-wrapper">

					<table class="data-table">

						<thead>

							<tr>
								<th>Shipping Bill No</th>
								<th>Shipping Bill Date</th>
								<th>Port Code</th>
								<th>Form No</th>
								<th>IE Code</th>
								<th>Export Agency</th>
								<th>Export Type</th>
								<th>Existing ADCode</th>
								<th>New ADCode</th>
								<th>Error Codes</th>
							</tr>

						</thead>

						<tbody>

							<tr>
								<td colspan="10" class="no-records">
									No records found
								</td>
							</tr>

						</tbody>

					</table>

				</div>

			</fieldset>

		</div>

	</div>

</body>
</html>