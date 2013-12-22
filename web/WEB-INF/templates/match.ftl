<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Match</title>
    <link rel="stylesheet" href="/resources/yui/cssgrids/cssgrids-min.css">
    <style type="text/css">
        body{
            font-family: "微软雅黑";
            font-size: 14px;
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
    </style>
    <script src="/resources/json2.js"></script>
    <script src="/resources/yui/yui/yui-min.js"></script>
    <script src="/resources/jquery-1.10.2.min.js"></script>
    <script type="text/javascript">
        var timer = null;
        var simple;
        var dataset = [];
        function get_results(){
            $.post("/api/com.tinyms.app.310win/get",{},function(data){
                if(data.result.length>=14){
                    clearInterval(timer);
                    $.each(data.result,function(i,row){
                        dataset.push({
                            id:row.id,
                            main:row.main,
                            client:row.client,
                            score:row.score,
                            season:row.season,
                            data:eval(row.data)
                        });

                    });
                    console.log(dataset);
                    simple.setAttrs({data:dataset});
                }

            },"json");
        }
        $(document).ready(function(){
            $.post("/api/com.tinyms.app.310win/run",{"url":"http://www.okooo.com/zucai/13181/"},function(data){
                console.log(data);
                timer = setInterval(get_results, 5000);
            },"json");
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
                    { key:'end', label:'终陪'}
                ],
                data : [],
                strings : {
                    emptyMessage : "No critter characters were found!"
                },
                width: 350,
                caption: '五家初陪终陪'
            }).render("#start_end");

            simple.render("#matchs");
            simple.addAttr("selectedRow",{value:null});
            simple.delegate("click",function(e){
                this.set("selectedRow", e.currentTarget);
            },'.yui3-datatable-data tr',simple);

            simple.after('selectedRowChange', function (e) {

                var tr = e.newVal,              // the Node for the TR clicked ...
                last_tr = e.prevVal,        //  "   "   "   the last TR clicked ...
                rec = this.getRecord(tr);   // the current Record for the clicked TR
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
                        if(item.MakerID==14 || item.MakerID==82||item.MakerID==27
                                ||item.MakerID==35||item.MakerID==84){
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
            });
        });
    </script>
</head>
<body class="yui3-skin-sam">
<input type="text" id="url"/>
<div class="yui3-g yui3-u-3-4">
    <div>
        <div class="yui3-u-1-2"><div id="matchs"></div></div>
        <div class="yui3-u-2-3"><div id="start_end"></div></div>
    </div>
    <div>
        <div class="yui3-u-1-5">1</div>
        <div class="yui3-u-1-5">2</div>
        <div class="yui3-u-1-5">3</div>
        <div class="yui3-u-1-5">4</div>
        <div class="yui3-u-1-5">5</div>
    </div>
</div>
</body>
</html>