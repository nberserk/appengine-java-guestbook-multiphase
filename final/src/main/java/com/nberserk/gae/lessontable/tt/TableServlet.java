package com.nberserk.gae.lessontable.tt;

import com.google.appengine.repackaged.com.google.gson.Gson;
import com.nberserk.gae.lessontable.Common;
import com.nberserk.gae.lessontable.tt.data.TTPoll;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TableServlet extends HttpServlet {	
	private static final long serialVersionUID = -6535578691334962972L;
	private static Gson sGson = new Gson();
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		
		String week = req.getParameter(VoteServlet.PARAM_DATE);
		if(week==null)
			week = VoteServlet.getThisWeek();
		
		//response.addHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        TTPoll tt = VoteServlet.getTimeTable(week);
		if(tt==null) {
            tt = new TTPoll();
            VoteServlet.updateTimeTable(week, tt);
        }
        String jsonString = sGson.toJson(tt);
        Common.info("tt_table: " + jsonString);
        response.getWriter().write(jsonString);		
	}
	
}

