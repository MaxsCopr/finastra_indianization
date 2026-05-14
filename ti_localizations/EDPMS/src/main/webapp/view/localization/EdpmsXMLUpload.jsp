<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="boe" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<title>XML File Upload</title>

<link rel="stylesheet" href="css/dashboard.css" />
<link rel="stylesheet" type="text/css" href="css/upload.css" />

<script type="text/javascript">

function windowClose()
{
    top.close();
}

</script>

</head>

<body>

<!-- ================= HEADER ================= -->

<div class="header">

<!--     <div class="logo-left">
        <img src="images/logo.png" alt="Logo"/>
    </div> -->

    <div class="system-date">
        System Date : 09-10-2025
    </div>

    <div class="logo-right">
        <img src="images/finastra-logo.png" alt="Union Bank"/>
    </div>

</div>

<!-- ================= LEFT SIDE ================= -->

<div class="left-panel">

    <div class="close-btn">
            <a href="home">Close</a>
    </div>

    <div class="left-line"></div>

</div>

<!-- ================= MAIN CONTENT ================= -->

<div class="main-content">

    <div class="content-box">

        <div class="inner-panel">

            <div class="title">
                XML File Upload Process
            </div>

            <a href="#" class="error-link">
                Out Error List
            </a>

            <div class="form-section">

                <form action="xmlFileUpload" method="post"
                    enctype="multipart/form-data">

                    <input type="file" name="uploadFile"
                        class="file-input"/>

                    <br/>

                    <input type="submit" value="submit"
                        class="submit-btn"/>

                </form>

            </div>

        </div>

    </div>

</div>

</body>
</html>