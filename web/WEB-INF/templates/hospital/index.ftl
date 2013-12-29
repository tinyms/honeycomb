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
        var search_dataview, panel, message_dialog;
        function search(page){
            var keyWords = $("#kw").val();
            var params = {"page": page+1, "limit": 10, "kw": keyWords}
            $.post("/api/com.tinyms.hospital.doc/search", params, function (data) {
                //console.log(data);
                search_dataview.setAttrs({data:data.items});
                pagnation(data.rows,page);
            }, "json");
        }
        function pageselectCallback(page_index, jq){
            search(page_index);
            return false;
        }
        function view_details(id){
            fillForm("","","","","","");
            $("#panelContent").removeClass("hide_dialog");
            $.post("/api/com.tinyms.hospital.doc/view", {id:id}, function (data) {
                //console.log(data);
                fillForm(data.product_name,data.index_no,data.func,data.build,data.bak,data.provider_name);
            }, "json");
            panel.show();
        }
        function fillForm(name,index_no,func,build,bak,provider){
            try{
                $("#name").val(name+" ("+index_no+")");
                $("#func").text(func);
                $("#build").text(build);
                $("#bak").text(bak);
                $("#provider").val(provider);
            }catch(e){
                if(message_dialog){
                    message_dialog.show();
                }
            }
        }
        function pagnation(total,current){
            $("#search_pagnation").pagination(total, {
                current_page:current,
                num_edge_entries: 2,
                num_display_entries: 5,
                callback: pageselectCallback,
                items_per_page:10
            });
        }
        $(document).ready(function () {
            pagnation(0,0);
        });
        YUI().use('datatable-mutable', 'panel', 'dd-plugin', function (Y) {

            // A table from data with keys that work fine as column names
            search_dataview = new Y.DataTable({
                columns: [
                    {key: "result", label: "搜索结果", allowHTML:true, formatter:function(row){
                        var html = "";
                        html += "<div><a style='color: blue;cursor: pointer;' onclick='view_details("+row.data.id+");'>"+row.data.product_name+"</a></div>";
                        html += "<div style='margin-top: 3px;'>";
                        html += "["+row.data.index_no+"] ";
                        html += row.data.func+" "+row.data.build+" "+row.data.bak;
                        html += "...</div>";
                        return html;
                    }}
                ],
                data: []
            }).render("#search_results");

            // Create the main modal form.
            panel = new Y.Panel({
                srcNode      : '#panelContent',
                headerContent: '药品说明',
                width        : 600,
                zIndex       : 5,
                centered     : true,
                modal        : true,
                visible      : false,
                render       : true,
                plugins      : [Y.Plugin.Drag]
            });
            message_box("Data Error.",Y);
        });
        function message_box(msg, Y){
            message_dialog = new Y.Panel({
                bodyContent: msg,
                width      : 400,
                zIndex     : 6,
                centered   : true,
                modal      : true,
                render     : '#message_dialog',
                buttons: [
                    {
                        value  : 'Yes',
                        section: Y.WidgetStdMod.FOOTER,
                        action : function (e) {
                            e.preventDefault();
                            message_dialog.hide();
                            panel.hide();
                        }
                    },
                    {
                        value  : 'No',
                        section: Y.WidgetStdMod.FOOTER,
                        action : function (e) {
                            e.preventDefault();
                            message_dialog.hide();
                        }
                    }
                ]
            });
            message_dialog.hide();
        }
    </script>
</head>
<body class="yui3-skin-sam">
    <div class="yui3-u">
        <a class='yui3-button' style="margin-top:20px;" href="/hospital/users.html">用户管理</a>
    </div>
    <div class="yui3-g">
        <div class="yui3-u-1" style="margin: 25px;"><input type="text" id="kw" style="width: 500px;"/>
            <button class='yui3-button' onclick="search(0);" id="parse_btn">搜索</button>
        </div>
    </div>
    <div class="yui3-g">
        <div class="yui3-u-1">
            <div id="search_results"></div>
            <div id="search_pagnation" style="margin-top: 5px;"></div>
        </div>
    </div>
    <!--SearchResult RecordView-->
    <div id="panelContent" class="hide_dialog">
        <div class="yui3-widget-bd">
            <form>
                <fieldset>
                    <p>
                        <label for="id">名称(字号)</label><br/>
                        <input type="text" id="name" style="width: 400px;">
                    </p>
                    <p>
                        <label for="name">功能</label><br/>
                        <textarea id="func" style="width: 500px;" rows="3"></textarea>
                    </p>
                    <p>
                        <label for="password">成分</label><br/>
                        <textarea id="build" style="width: 500px;" rows="3"></textarea>
                    </p>
                    <p>
                        <label for="password">备注</label><br/>
                        <textarea id="bak"style="width: 500px;" rows="6"></textarea>
                    </p>
                    <p>
                        <label for="password">厂家</label><br/>
                        <input type="text" id="provider" style="width: 400px;">
                    </p>
                </fieldset>
            </form>
        </div>
    </div>
    <div id="message_dialog"></div>
</body>
</html>