<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Match</title>
    <link rel="stylesheet" href="/resources/yui/cssgrids/cssgrids-min.css">
    <link rel="stylesheet" href="/resources/yui/cssbutton/cssbutton.css">
    <style type="text/css">
        body{
            font-family: "微软雅黑";
            font-size: 14px;
            margin: auto; /* center in viewport */
            width: 960px;
        }
        .font_red{
            color: red;
        }
        #matchs tbody tr{
            cursor: pointer;
        }
        .yui3-skin-sam .yui3-datatable tr.myhilite td {
            background-color: #C0ffc0;
        }
        .yui3-skin-sam .yui3-datatable tr.jfhilite td {
            background-color: #FFFFCC;
        }
    </style>
    <script src="/resources/json2.js"></script>
    <script src="/resources/yui/yui/yui-min.js"></script>
    <script src="/resources/jquery-1.10.2.min.js"></script>
    <script type="text/javascript">
        var timer = null;
        var simple;
        var dataset = [];
        var main_team="",client_team="";
        function get_results(){
            $.post("/api/com.tinyms.app.310win/get",{},function(data){
                if(data.result.length>=14){
                    dataset = [];
                    $("#parse_btn").prop("disabled",false);
                    clearInterval(timer);
                    $.each(data.result,function(i,row){
                        dataset.push({
                            id:row.id,
                            main:row.main,
                            client:row.client,
                            score:row.score,
                            season:row.season,
                            jf:row.jf,
                            data:eval(row.data)
                        });

                    });
                    //console.log(dataset);
                    simple.setAttrs({data:dataset});
                }

            },"json");
        }
        function color_odds(start, end){
            var home = parseFloat(start.home) - parseFloat(end.home);
            var draw = parseFloat(start.draw) - parseFloat(end.draw);
            var away = parseFloat(start.away) - parseFloat(end.away);
            if(home>0){
                end.home = '<span style="color: green">'+end.home+'</span>';
            }else if(home<0){
                end.home = '<span style="color: red">'+end.home+'</span>';
            }
            if(draw>0){
                end.draw = '<span style="color: green">'+end.draw+'</span>';
            }else if(draw<0){
                end.draw = '<span style="color: red">'+end.draw+'</span>';
            }
            if(away>0){
                end.away = '<span style="color: green">'+end.away+'</span>';
            }else if(away<0){
                end.away = '<span style="color: red">'+end.away+'</span>';
            }
        }
        function color_row(chk){
            if($(chk).prop("checked")){
                $(chk).parent().parent().addClass("jfhilite");
            }else{
                $(chk).parent().parent().removeClass("jfhilite");
            }
        }
        function color_jf_row(){
            var main_ = false;
            var client_ = false;
            $("#jf_list tbody.yui3-datatable-data tr").each(function(i,tr){
                if((i==2||i==3)&&!main_){
                    var td = $(tr).children(":eq(2)");
                    if(main_team.indexOf(td.text())!=-1){
                        $(tr).addClass("jfhilite");
                        main_ = true;
                    }
                }
                if((i==4||i==5)&&!client_){
                    var td = $(tr).children(":eq(2)");
                    if(client_team.indexOf(td.text())!=-1){
                        $(tr).addClass("jfhilite");
                        client_=true;
                    }
                }
            });
        }
        function startup_parse_thread(){
            $.post("/api/com.tinyms.app.310win/run",{"url":$("#url").val()},function(data){
                //console.log(data);
                $("#parse_btn").prop("disabled",true);
                timer = setInterval(get_results, 5000);
            },"json");
        }
        function include(no){
            var makers = ["14","82","94","35","84","27","65","59","18"]
            for(var i=0;i<makers.length;i++){
                if(no==makers[i]){
                    return true;
                }
            }
            return false;
        }
        $(document).ready(function(){

        });
        YUI().use("datatable", function (Y) {

            // A table from data with keys that work fine as column names
            simple = new Y.DataTable({
                columns: [{key:"season",label:"联赛"},
                    {key:"main",label:"主队"},
                    {key:"score",allowHTML:true,label:"比分"},
                    {key:"client",label:"客队"}],
                data   : [],
                caption: "足彩胜负14场"
            });

            var dt_start_end = new Y.DataTable({
                columns : [
                    { key:'name', label:'名称'},
                    { key:'start', label:'初陪'},
                    { key:'end', label:'终陪', allowHTML:true}
                ],
                data : [],
                strings : {
                    emptyMessage : "No records were found!"
                },
                caption: '五家初陪终陪'
            }).render("#start_end");

            var dt_jflist = new Y.DataTable({
                columns : [
                    {key:'select',allowHTML:true,label:"选",formatter: '<input type="checkbox" onclick="color_row(this);"/>'},
                    { key:'order', label:'排名'},
                    { key:'team', label:'球队'},
                    { key:'battle', label:'赛'},
                    { key:'home', label:'胜'},
                    { key:'draw', label:'平'},
                    { key:'away', label:'负'},
                    { key:'score', label:'分'}
                ],
                data : [],
                strings : {
                    emptyMessage : "No records were found!"
                },
                caption: '积分榜'
            }).render("#jf_list");

            simple.render("#matchs");
            simple.addAttr("selectedRow",{value:null});
            simple.delegate("click",function(e){
                this.set("selectedRow", e.currentTarget);
            },'.yui3-datatable-data tr',simple);

            simple.after('selectedRowChange', function (e) {

                var tr = e.newVal,              // the Node for the TR clicked ...
                last_tr = e.prevVal,        //  "   "   "   the last TR clicked ...
                rec = this.getRecord(tr);   // the current Record for the clicked TR
                main_team = rec.get("main");
                client_team = rec.get("client");
                if ( !last_tr ) {
                    // first time thru ... display the Detail DT DIV that was hidden
                    //Y.one("#chars").show();
                } else {
                    last_tr.removeClass("myhilite");
                }
                tr.addClass("myhilite");
                //start and end
                var start_and_end_odds = [];
                if(rec.get("data")){
                    Y.Array.each(rec.get("data"),function(item){
                        if(include(item.MakerID)){
                            color_odds(item.Start, item.End);
                            start_and_end_odds.push({
                                        name:item.CompanyName,
                                        start:item.Start.home+" "+item.Start.draw+" "+item.Start.away,
                                        end:item.End.home+" "+item.End.draw+" "+item.End.away
                                    }
                            );

                        }

                    });
                }
                dt_start_end.setAttrs({
                    data: start_and_end_odds,
                    caption: '<strong>' + rec.get('main') + " "+rec.get("client")+'</strong>'
                });

                var jflist = [];
                if(rec.get("jf")){
                    Y.Array.each(rec.get("jf"),function(item){
                        var jf = {};
                        jf.order = item[0];
                        jf.team = item[1];
                        jf.battle = item[2];
                        jf.home = item[3];
                        jf.draw = item[4];
                        jf.away = item[5];
                        jf.score = item[6];
                        jflist.push(jf);
                    });
                }
                dt_jflist.setAttrs({
                    data: jflist,
                    caption: '<strong>' + rec.get('main') + " "+rec.get("client")+'</strong>'
                });
                color_jf_row();
            });
        });
    </script>
</head>
<body class="yui3-skin-sam">

<div class="yui3-g">
    <div class="yui3-u-1" style="margin: 25px;"><input type="text" id="url" style="width: 500px;" value="http://www.okooo.com/livecenter/zucai/"/>
        <button class='yui3-button' onclick="startup_parse_thread();" id="parse_btn">分析</button></div>
</div>
<div class="yui3-g">
    <div class="yui3-u-1-3"><div id="matchs"></div></div>
    <div class="yui3-u-2-3">
        <div id="jf_list"></div>
        <div id="start_end"></div>
    </div>
</div>
</body>
</html>