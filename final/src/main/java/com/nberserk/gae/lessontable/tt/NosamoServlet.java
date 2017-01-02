package com.nberserk.gae.lessontable.tt;

import com.google.appengine.api.datastore.*;
import com.google.gson.reflect.TypeToken;
import com.nberserk.gae.lessontable.Common;
import com.nberserk.gae.lessontable.tt.data.NosamoPoll;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;

public class NosamoServlet extends HttpServlet {
	private static final long serialVersionUID = -6535578691334962972L;
    public static final String ENTITY_KEY = "nosamo";
    public static final String PARAM_DATE = "date";
    public static final String URL_REDIRECT = "/nosamo.html";


    public static NosamoPoll getData(String week) {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Key key = KeyFactory.createKey(VoteServlet.KIND, week);

        try {
            Entity entity = ds.get(key);
            if(!entity.hasProperty(ENTITY_KEY))
                return null;
            Text text = (Text) entity.getProperty(ENTITY_KEY);
            Type listType = new TypeToken<NosamoPoll>(){}.getType();
            NosamoPoll tt = Common.getGson().fromJson(text.getValue(), listType);
            return tt;
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    public static String saveData(String week, NosamoPoll poll){
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Key key = KeyFactory.createKey(VoteServlet.KIND, week);

        Entity entity = null;
        try {
            entity = ds.get(key);
        } catch (EntityNotFoundException e) {
            entity = new Entity(VoteServlet.KIND, week);
        }
        String jsonString = Common.getGson().toJson(poll);
        Text text = new Text(jsonString);
        entity.setProperty(ENTITY_KEY, text);
        ds.put(entity);
        return jsonString;
    }
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		
		String week = req.getParameter(PARAM_DATE);
		if(week==null)
			week = Common.getThisWeek();
		
		//response.addHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        NosamoPoll tt = getData(week);
		if(tt==null) {
            tt = new NosamoPoll();
            saveData(week, tt);
        }
        String jsonString = Common.getGson().toJson(tt);
        Common.info("nosamo: " + jsonString);
        response.getWriter().write(jsonString);		
	}

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String userName = req.getParameter("name");
        if(userName==null ){
            Common.info("name null or too short");
            response.sendRedirect(URL_REDIRECT);
            return;
        }

        userName = userName.trim();
        if(userName.length() <=0 ){
            Common.info("empty userName");
            response.sendRedirect(URL_REDIRECT);
            return;
        }


        String week = req.getParameter("date");
        if (week==null){
            week = Common.getThisWeek();
        }
        Common.info("desiredDate:"+week);

        String time = req.getParameter("lessonTime");
        if (time==null){
            Common.info("lessonTime null");
            response.sendRedirect(URL_REDIRECT);
            return;
        }

        NosamoPoll poll = getData(week);
        if (poll==null){
            Common.info("nosamo: entity not found, strange. when this can be happen?");
            poll = new NosamoPoll();
        }

        if(poll.canVoteAvailable()){
            poll.vote(time, userName);
            String json = saveData(week, poll);
            Common.info("updated: " + json);

            response.getWriter().write(json);
        }else{
            Common.info("nosamo ignored: vote not available.");

            response.sendError(500, "vote not available.");
        }
    }
}

