<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.EntityNotFoundException" %>
<%@ page import="com.google.appengine.api.datastore.FetchOptions" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.example.guestbook.TimeTable" %>
<%@ page import="com.example.guestbook.TimeTable.TimeSlot" %>
<%@ page import="com.google.appengine.repackaged.com.google.gson.Gson" %>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html> 
<html>
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="http://code.jquery.com/mobile/1.2.0/jquery.mobile-1.2.0.min.css" />
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <script src="http://code.jquery.com/mobile/1.2.0/jquery.mobile-1.2.0.min.js"></script>
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
    //UserService userService = UserServiceFactory.getUserService();
    //User user = userService.getCurrentUser();
    //if (user != null) {
    //    pageContext.setAttribute("user", user);        
%>

<%-- <p>Hello, ${fn:escapeXml(user.nickname)}! (You can
    <a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.)</p>
<%
    } else {
%>
<p>Hello!
    <a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
    to include your name with greetings you post.</p>
<%
    }
%> --%>


<%
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Key key = KeyFactory.createKey("vote", desiredDate);
    
    try{
    	Entity entity = datastore.get(key);
		Gson gson = new Gson();
		TimeTable tt = gson.fromJson((String) entity.getProperty("json"), TimeTable.class);        
        for (TimeSlot ts : tt.getSlots()) {            
            pageContext.setAttribute("voteTime", ts.getTime());
            pageContext.setAttribute("voter", ts.getVoter().toString());
%>
            <p>${fn:escapeXml(voteTime)}</b> : ${fn:escapeXml(voter)}</p>
<% 
        }
    } catch (EntityNotFoundException e) {
    	
    }    
%>

<form action="/vote" method="post">
    <select name="time">
        <option value="0"> 예비  </option>
        <option value="5:30"> 5:30  </option>
        <option value="5:50"> 5:50  </option>
        <option value="6:10"> 6:10  </option>
        <option value="6:30"> 6:30  </option>
        <option value="6:50"> 6:50  </option>
        <option value="7:10"> 7:10  </option>
    </select>
    <input type="text" name="name"/>
    <input type="submit" value="투표" onclick="checkInput()"/>    
</form>


</body>
</html>
<%-- //[END all]--%>
