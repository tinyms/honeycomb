<!DOCTYPE html>
<html>
<head>
    <title></title>
    <script src="/resources/json2.js"></script>
    <script src="/resources/yui/yui/yui-min.js"></script>
    <script src="/resources/jquery-1.10.2.min.js"></script>
    <script src="/resources/jquery.number.min.js"></script>
    <script src="/resources/jquery_pagination/jquery.pagination.js"></script>
    <link rel="stylesheet" href="/resources/yui/cssgrids/cssgrids-min.css">
    <link rel="stylesheet" href="/resources/yui/cssbutton/cssbutton.css">
    <link rel="stylesheet" href="/resources/jquery_pagination/pagination.css">
    <style type="text/css">
        body {
            font-family: "微软雅黑";
            font-size: 14px;
            margin: auto; /* center in viewport */
            width: 1200px;
        }

        .font_red {
            color: red;
        }

        #matchs tbody tr {
            cursor: pointer;
        }

        #start_end tbody tr {
            cursor: pointer;
        }

        .yui3-skin-sam .yui3-datatable tr.myhilite td {
            background-color: #C0ffc0;
        }

        .yui3-skin-sam .yui3-datatable tr.jfhilite td {
            background-color: #FFFFCC;
        }
        .hide_dialog{display: none;}
    </style>
    <script type="text/javascript">
        var users_dataview, user_details_panel;
        function show(id){
            if(user_details_panel!=null){
                $("#user_details").removeClass("hide_dialog");
                user_details_panel.show();
                $.post("/api/com.tinyms.api.user/get",{id:id},function(data){
                    $("#loginId").val("");
                    $("#name").val("");
                    $("#email").val("");
                    $("#status").val("");
                    $("#loginId").val(data.loginId);
                    $("#name").val(data.name);
                    $("#email").val(data.email);
                    $("#status").val(data.status);
                },"json");
            }
        }
        YUI().use('datatable-mutable', 'panel', 'dd-plugin', function (Y) {
            users_dataview = new Y.DataTable({
                columns: [
                    {key: "loginId", label: "帐号", allowHTML:true, formatter:function(o){
                        return "<a style='color: blue;cursor: pointer;' onclick='show("+ o.data.id +");'>"+ o.data.loginId +"</a>";
                    }},
                    {key: "name", label: "姓名"},
                    {key: "email", label: "邮箱"},
                    {key: "createTime", label: "加入时间"},
                    {key: "logonTime", label: "最后登录时间"},
                    {key: "status", label: "状态", formatter:function(o){
                        if(o.data.status==1){
                            return "激活";
                        }
                        return "禁用";
                    }}
                ],
                data: []
            }).render("#users_dataview");
            // Create the main modal form.
            user_details_panel = new Y.Panel({
                srcNode      : '#user_details',
                headerContent: '用户资料',
                width        : 600,
                zIndex       : 5,
                centered     : true,
                modal        : true,
                visible      : false,
                render       : true,
                plugins      : [Y.Plugin.Drag]
            });
            $.post("/api/com.tinyms.api.user/list",{},function(data){
                users_dataview.setAttrs({data:data.result});
            },"json");
        });
    </script>
</head>
<body class="yui3-skin-sam">
    <div class="yui3-u" style="margin-top: 20px;">
        <a class='yui3-button' href="/hospital/index.html">药品搜索</a>
        <a class='yui3-button' >增加用户</a>
    </div>
    <div class="yui3-g" style="margin-top: 10px;">
        <div class="yui3-u-1">
            <div id="users_dataview"></div>
            <div id="users_pagnation" style="margin-top: 5px;"></div>
        </div>
    </div>
    <!--SearchResult RecordView-->
    <div id="user_details" class="hide_dialog">
        <div class="yui3-widget-bd">
            <form>
                <fieldset>
                    <p>
                        <label for="id">帐号</label><br/>
                        <input type="text" id="loginId" style="width: 400px;">
                    </p>
                    <p>
                        <label for="name">姓名</label><br/>
                        <input type="text" id="name" style="width: 400px;">
                    </p>
                    <p>
                        <label for="password">密码</label><br/>
                        <input type="text" id="loginPwd" style="width: 400px;">
                    </p>
                    <p>
                        <label for="password">邮箱</label><br/>
                        <input type="text" id="email" style="width: 400px;">
                    </p>
                    <p>
                        <label for="password">状态</label><br/>
                        <select id="status">
                            <option value="1">激活</option>
                            <option value="0">禁用</option>
                        </select>
                    </p>
                </fieldset>
            </form>
        </div>
    </div>
</body>
</html>