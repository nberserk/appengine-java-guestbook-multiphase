package com.nberserk.gae.lessontable.tt;

import com.google.appengine.api.datastore.*;
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



        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Key key = KeyFactory.createKey(VoteServlet.KIND, week);

        TTPoll poll = null;
        Entity entity = null;

        // get entity
        try {
            entity = ds.get(key);
            poll = sGson.fromJson((String) entity.getProperty(VoteServlet.ENTITY_KEY), TTPoll.class);
        } catch (EntityNotFoundException e) {
            poll = new TTPoll();
            entity = new Entity(VoteServlet.KIND, week);

            Common.info("new entity created: " + VoteServlet.ENTITY_KEY + "/"+week);
        }


        if(cmd.equals("enable")){
            poll.startPoll();
        }else {
            poll.endPoll();
        }

        String jsonString = sGson.toJson(poll);
        entity.setProperty(VoteServlet.ENTITY_KEY, jsonString);
        ds.put(entity);

        response.getWriter().write(jsonString);
	}
	
	
	
}
// [END all]
