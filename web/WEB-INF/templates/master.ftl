<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>{% block title %} - CallOrder</title>
    <meta name="description" content="CallOrder">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="${SiteUrl}/resources/theme/v.3.2.1/css/bootstrap.css">
    <link rel="stylesheet" href="${SiteUrl}/resources/theme/v.3.2.1/css/font-awesome.min.css">
    <link rel="stylesheet" href="${SiteUrl}/resources/theme/v.3.2.1/css/style.css">
    <link rel="stylesheet" href="${SiteUrl}/resources/theme/v.3.2.1/css/plugin.css">
    <link rel="stylesheet" href="${SiteUrl}/resources/theme/v.3.2.1/css/landing.css">
    <!--[if lt IE 9]>
    <script src="${SiteUrl}/resources/theme/v.3.2.1/js/ie/respond.min.js"></script>
    <script src="${SiteUrl}/resources/theme/v.3.2.1/js/ie/html5.js"></script>
    <script src="${SiteUrl}/resources/theme/v.3.2.1/js/ie/excanvas.js"></script>
    <![endif]-->
    {% block header %}
    <style>
        body {
            font-family: '微软雅黑', Verdana, sans-serif, '宋体';
        }
    </style>
</head>
<body>
<!-- header -->
<header id="header" class="navbar bg bg-black">
    <ul class="nav navbar-nav navbar-avatar pull-right">
        <li class="dropdown">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                <span class="hidden-xs-only">管理员</span>
                <span class="thumb-small avatar inline"><img src="${SiteUrl}/resources/theme/v.3.2.1/images/avatar.jpg" alt="Mika Sokeil" class="img-circle"></span>
                <b class="caret hidden-xs-only"></b>
            </a>
            <ul class="dropdown-menu">
                <li><a href="#">修改密码</a></li>
                <li><a href="signin.html">退出</a></li>
            </ul>
        </li>
    </ul>
    <a class="navbar-brand" href="#">EasyCall</a>
    <button type="button" class="btn btn-link pull-left nav-toggle visible-xs" data-toggle="class:slide-nav slide-nav-left" data-target="body">
        <i class="icon-reorder icon-xlarge text-default"></i>
    </button>
    <form class="navbar-form pull-left shift" action="" data-toggle="shift:appendTo" data-target=".nav-primary">
        <i class="icon-search text-muted"></i>
        <input type="text" class="input-sm form-control" placeholder="Search">
    </form>
</header>
<!-- / header -->
<!-- nav -->
<nav id="nav" class="nav-primary hidden-xs nav-vertical bg-light">
    <ul class="nav" data-spy="affix" data-offset-top="50">
        <li class="active"><a href="/callorder/index.html"><i class="icon-dashboard icon-xlarge"></i><span>控制面板</span></a></li>
        <li><a href="form.html"><i class="icon-edit icon-xlarge"></i><span>公司管理</span></a></li>
        <li><a href="form.html"><i class="icon-edit icon-xlarge"></i><span>回馈</span></a></li>
        <li><a href="chart.html"><i class="icon-signal icon-xlarge"></i><span>用户</span></a></li>
    </ul>
</nav>
<!-- / nav -->
<section id="content">
<section class="main padder">
{% block workspace %}
</section>
</section>
<!-- footer -->
<footer id="footer">
    <div class="text-center padder clearfix">
        <p>
            <small>&copy; CallOrder 2013</small><br>
        </p>
    </div>
</footer>
<a href="#" class="hide slide-nav-block" data-toggle="class:slide-nav slide-nav-left" data-target="body"></a>
<!-- / footer -->
<script src="${SiteUrl}/resources/theme/v.3.2.1/js/jquery.min.js"></script>
<!-- Bootstrap -->
<script src="${SiteUrl}/resources/theme/v.3.2.1/js/bootstrap.js"></script>
<!-- app -->
<script src="${SiteUrl}/resources/theme/v.3.2.1/js/app.js"></script>
<script src="${SiteUrl}/resources/theme/v.3.2.1/js/app.plugin.js"></script>
<script src="${SiteUrl}/resources/theme/v.3.2.1/js/app.data.js"></script>
{% block footer %}
</body>
</html>