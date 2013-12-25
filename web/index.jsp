<%-- Created by IntelliJ IDEA. --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Indexer</title>
    <script type="text/javascript" src="resources/jquery-1.10.2.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            var params = {"page": 1, "limit": 20, "kw": "肝炎 病毒"}
            $.post("/api/com.tinyms.hospital.doc/search", params, function (data) {
                console.log(data);
            }, "json");
        });
    </script>
</head>
<body>

</body>
</html>