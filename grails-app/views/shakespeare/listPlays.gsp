<%--
  Created by IntelliJ IDEA.
  User: cedric
  Date: 1/11/15
  Time: 9:46 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Shakespeare Plays</title>
</head>

<body>
<div id="page-body" role="main">
    <h1>Shakespeare Plays</h1>
    <p>via server <strong>${serverId}</strong></p>
    <table>
        <thead>
            <tr>
                <th>Play</th>
                <th>Lines</th>
            </tr>
        </thead>
        <tbody>
            <g:each var="bucket" in="${buckets}">
                <tr>
                    <td><a href="/shakespeare/lines?playName=${bucket.key}">${bucket.key}</a></td>
                    <td><a href="/shakespeare/lines?playName=${bucket.key}">${bucket.doc_count.intValue()}</a></td>
                </tr>
            </g:each>
        </tbody>
    </table>
</div>
</body>
</html>