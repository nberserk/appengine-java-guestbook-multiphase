package com.nberserk.gae.lessontable.tt;

import com.google.appengine.repackaged.com.google.gson.Gson;
import com.nberserk.gae.lessontable.Common;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TriggerServlet extends HttpServlet {
	private static Gson sGson = new Gson();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {

        // cmd: enable or disable
		String cmd = req.getParameter("cmd");
        if(cmd==null){
            Common.info("cmd is null");
            response.sendRedirect(VoteServlet.URL_REDIRECT);
            return;
        }

        String week = VoteServlet.getThisWeek();

        TTPoll poll = VoteServlet.getTimeTable(week);
        if(poll==null){
            poll = new TTPoll();
        }

        if(cmd.equals("enable")){
            poll.startPoll();
        }else {
            poll.endPoll();
        }

        String jsonString = sGson.toJson(poll);
        VoteServlet.updateTimeTable(week, poll);
        response.getWriter().write(jsonString);
	}
	
	
	
}
// [END all]
