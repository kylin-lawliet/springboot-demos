<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>徽章样式示例</title>
    <link href="/bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="/bootstrap/css/font-awesome.css" rel="stylesheet">
</head>
<body>

<div class="container">
    <h1>测试标题 <span class="badge badge-secondary">New</span></h1>
    <h2>测试标题 <span class="badge badge-secondary">New</span></h2>
    <h3>测试标题 <span class="badge badge-secondary">New</span></h3>
    <h4>测试标题 <span class="badge badge-secondary">New</span></h4>
    <h5>测试标题 <span class="badge badge-secondary">New</span></h5>
    <h6>测试标题 <span class="badge badge-secondary">New</span></h6>
</div>
<br>

<div class="container">
    <h1 class="page-header">
        布局<small>小标题</small>
    </h1>
    <p>这是一段描述文字。。。</p>

    <div class="row">
        <div class="col-lg-4">
            <h2 class="page-header">副标题1</h2>
            <p>这是一段描述文字。。。</p>
        </div>

        <div class="col-lg-4">
            <h2 class="page-header">副标题2</h2>
            <p>这是一段描述文字。。。</p>
        </div>

        <div class="col-lg-4">
            <h2 class="page-header">副标题3</h2>
            <p>这是一段描述文字。。。</p>
        </div>
    </div>
</div>
<br>

<div class="container">
    <span class="label label-primary">主要</span><br><br><br>
    <span class="label label-success fa-2x">成功</span><br><br><br>
    <span class="label label-danger  col-lg-1">危险</span><br><br><br>
    <span class="label label-warning fa-lg">警告</span><br><br>
    <span class="label label-info">信息</span><br><br><br>
</div>
<br>

<div class="container">
    <button type="button" class="btn btn-primary">
        Messages <span class="badge badge-light">4</span>
    </button>
    <button type="button" class="btn btn-danger">
        Notifications <span class="badge badge-light">7</span>
    </button>
</div>
<br>

</body>
<script src="${basePath}/jquery/jquery-2.1.4.min.js" type="text/javascript"></script>
<script src="/bootstrap/js/bootstrap.js" type="text/javascript"></script>
</html>
