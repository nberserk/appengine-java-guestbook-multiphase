package com.nberserk.gae.lessontable.tt;


import com.google.gson.Gson;
import com.nberserk.gae.lessontable.Common;
import com.nberserk.gae.lessontable.tt.data.NosamoPoll;
import com.nberserk.gae.lessontable.tt.data.TTPoll;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TriggerServlet extends HttpServlet {
	private static Gson sGson = new Gson();
    public static final String URL_REDIRECT = "/lesson.html";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {

        // cmd: enable or disable
		String cmd = req.getParameter("cmd");
        if(cmd==null){
            Common.info("cmd is null");
            response.sendRedirect(URL_REDIRECT);
            return;
        }

        String key = req.getParameter("key");
        if(key==null){
            key = VoteServlet.ENTITY_KEY;
        }

        String week = Common.getThisWeek();

        TTPoll poll = null;
        if(key.equals(VoteServlet.ENTITY_KEY)){
            poll = VoteServlet.getTimeTable(week);
            if(poll==null){
                poll = new TTPoll();
            }
        }
        else {
            poll = NosamoServlet.getData(week);
            if(poll==null)
                poll = new NosamoPoll();
        }

        if(cmd.equals("enable")){
            poll.startPoll();
        }else {
            poll.endPoll();
        }

        String jsonString = sGson.toJson(poll);
        if(key.equals(VoteServlet.ENTITY_KEY)){
            VoteServlet.updateTimeTable(week, poll);
        } else {
            NosamoServlet.saveData(week, (NosamoPoll) poll);
        }
        response.getWriter().write(jsonString);
	}
	
	
	
}
// [END all]
