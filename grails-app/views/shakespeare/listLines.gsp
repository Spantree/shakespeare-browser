<%--
  Created by IntelliJ IDEA.
  User: cedric
  Date: 1/11/15
  Time: 9:46 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@page defaultCodec="none" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Shakespeare Plays</title>
</head>

<body>
<div id="page-body" role="main">
    <h1><a href="/shakespeare/plays">Selected Shakespeare Plays</a> > ${playName}</h1>
    <p>via server <strong>${serverId}</strong></p>
    <form action="/shakespeare/lines" method="GET">
        <input type="hidden" name="playName" value="${playName}"/>
        <input type="text" name="searchText" placeholder="Enter search text" value="${searchText}"/>
    </form>
    <h3>${count} lines found.</h3>
    <g:each var="line" in="${lines}">
        <p>
            <strong>${line['speaker']}:</strong>
            ${line['text']}
        </p>
    </g:each>
    <g:if test="${count > lines.size()}">
        <p>...and <strong>${count - lines.size()} more lines</strong></p>
    </g:if>
</div>
</body>
</html>
