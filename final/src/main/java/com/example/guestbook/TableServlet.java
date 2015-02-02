package com.example.guestbook;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.repackaged.com.google.gson.Gson;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TableServlet extends HttpServlet {	
	private static final long serialVersionUID = -6535578691334962972L;
	private static Gson sGson = new Gson();
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		
		String date = req.getParameter(VoteServlet.PARAM_DATE);
		if(date==null)
			date = VoteServlet.getTodayDate();
		
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        TimeTable tt = VoteServlet.getTimeTable(date);
        if (tt==null)
        	tt = new TimeTable();
        String jsonString = sGson.toJson(tt);
        VoteServlet.sLogger.info(jsonString);
        response.getWriter().write(jsonString);		
	}
	
	
	
}
// [END all]
