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
	<meta charset="utf-8"> 
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="http://code.jquery.com/mobile/1.2.0/jquery.mobile-1.2.0.min.css" />
<script src="http://code.jquery.com/jquery-1.8.2.min.js"></script>
<script	src="http://code.jquery.com/mobile/1.2.0/jquery.mobile-1.2.0.min.js"></script>
<script>
 $(document).ready(function(){
	 $("form").submit(function(event){
		 
	     var errorCount=0;
	     var name = $("#name").val().trim();	    	
	     if(!name){
	    	 $("#popupDialog").popup("open");
	    	 return false;
	     }
	     
	     var orgUrl =  window.location.href;
	     $.post("/vote", $( "form" ).serialize(), function(){
	    	window.location = orgUrl; 
	     });
	     return false;
	 });
 });
 $(document).ready(function(){
	 $.getJSON("/table", function(tt){	
		 var list = $("#timetable");
		 console.log(tt.slots);
		 $.each(tt.slots, function(index, data){
			 list.append($('<li>').append( 
					 $('<h3>').text(data.voter)).append( 
							 $('<p>', {class:"ui-li-aside"}).append(
									 $('<strong>').text(data.time)
							 )							
					 )
			 );
		 });	
		$("#timetable").listview("refresh");
		});
	});
</script>
</head>

<body>

<div data-role="page" id="home">
		<div data-role="header">
			<h1>탁구 레슨 시간표</h1>
		</div>
		<!-- /header -->

		<div data-role="content">
            <div class="ui-bar ui-bar-c">
			<ul data-role="listview" id="timetable">			
				<!-- <li><h3>darren, andrew</h3><p class="ui-li-aside"><strong>6:30</strong></p> -->
				</li>					
			</ul>
            </div>
            
            <br> <br>			
            <div class="ui-bar ui-bar-b">
			<form action="/vote" method="post" id="form" data-theme="b">
				<label for="lessonTime" class="select">시간 :</label>
                <select name="lessonTime" id="lessonTime" data-native-menu="false">
							<option value="5:30">5:30</option>
							<option value="5:50">5:50</option>
							<option value="6:10">6:10</option>
				</select>
                <input type="text" name="name" id="name" value="" placeholder="이름" /> 
				<input type="submit" value="투표" />
	        </form>            
			</div>
		</div>		<!-- /content -->
		<div data-role="popup" id="popupDialog" data-overlay-theme="a" data-theme="c" style="max-width:400px;" class="ui-corner-all">
				<div data-role="header" data-theme="a" class="ui-corner-top">
					<h1>이름</h1>
				</div>
				<div data-role="content" data-theme="d" class="ui-corner-bottom ui-content">
					<h3 class="ui-title">이름을 입력하세요.</h3>					
					<a href="#" data-role="button" data-inline="true" data-rel="back" data-theme="c">확인</a>					
				</div>
		</div> <!-- popup -->
			
		<div data-role="footer" data-id="foo1" data-position="fixed"></div>
		<!-- /footer -->

	</div>
	<!-- /page -->


</body>
</html>
<%-- //[END all]--%>
