<%-- //[START all]--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%-- //[START imports]--%>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.FetchOptions" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%-- //[END imports]--%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
</head>

<body>

<%
    String desiredDate = request.getParameter("date");
    if (desiredDate == null) {
        SimpleDateFormat sd = new SimpleDateFormat("yyMMdd");
        Date now = new Date();
        desiredDate = sd.format(now);
    }
    //pageContext.setAttribute("guestbookName", guestbookName);
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) {
        pageContext.setAttribute("user", user);        
%>

<p>Hello, ${fn:escapeXml(user.nickname)}! (You can
    <a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.)</p>
<%
    } else {
%>
<p>Hello!
    <a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
    to include your name with greetings you post.</p>
<%
    }
%>

<%-- //[START datastore]--%>
<%
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Key guestbookKey = KeyFactory.createKey("vote", desiredDate);
    // Run an ancestor query to ensure we see the most up-to-date
    // view of the Greetings belonging to the selected Guestbook.
    Query query = new Query("vote", guestbookKey);
    List<Entity> greetings = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
    if (greetings.isEmpty()) {
%>
<p> no reservation yet. </p>
<%
    } else {
%>
<p>current reservations:</p>
<%
        Entity entity = greetings.get(0);
        java.util.Map<String, Object> map = entity.getProperties();
        for (String time : map.keySet()) {
            String v = (String)map.get(time);
            pageContext.setAttribute("voteTime", time);
            pageContext.setAttribute("voter", v);
%>
<p>${fn:escapeXml(voteTime)}</b> : ${fn:escapeXml(voter)}</p>
<%
        }
    }
%>

<form action="/vote" method="post">
    <select name="time">
        <option value="0"> undefined  </option>
        <option value="5:30"> 5:30  </option>
        <option value="5:50"> 5:50  </option>
    </select>
    <input type="submit" value="vote!"/>
</form>


</body>
</html>
<%-- //[END all]--%>
