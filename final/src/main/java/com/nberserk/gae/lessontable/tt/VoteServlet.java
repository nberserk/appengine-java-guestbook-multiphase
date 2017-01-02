
package com.nberserk.gae.lessontable.tt;

import com.google.appengine.api.datastore.*;
import com.google.gson.reflect.TypeToken;
import com.nberserk.gae.lessontable.Common;
import com.nberserk.gae.lessontable.tt.data.TTPoll;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;

public class VoteServlet extends HttpServlet {    
	private static final long serialVersionUID = 1435452240677102479L;
	public static final String KIND = "vote";
    public static final String ENTITY_KEY = "json";
    public static final String PARAM_DATE = "date";
    public static final String URL_REDIRECT = "/lesson.html";

    /**
     * 
     * @param week : week
     * @return null if not found
     */
    public static TTPoll getTimeTable(String week) {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Key key = KeyFactory.createKey(KIND, week);

        try {
            Entity entity = ds.get(key);
            if(!entity.hasProperty(ENTITY_KEY))
                return null;
            Text text = (Text) entity.getProperty(ENTITY_KEY);
            Type listType = new TypeToken<TTPoll>(){}.getType();
            TTPoll tt = Common.getGson().fromJson(text.getValue(), listType);
            return tt;
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    public static String updateTimeTable(String week, TTPoll poll){
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Key key = KeyFactory.createKey(KIND, week);

        Entity entity = null;
        try {
            entity = ds.get(key);
        } catch (EntityNotFoundException e) {
            entity = new Entity(KIND, week);
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
        TTPoll tt = getTimeTable(week);
        if(tt==null) {
            tt = new TTPoll();
            updateTimeTable(week, tt);
        }
        String jsonString = Common.getGson().toJson(tt);
        Common.info("tt_table: " + jsonString);
        response.getWriter().write(jsonString);
    }
    
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse response)
            throws IOException {

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
            Common.info("full of spaces");
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

        TTPoll poll = getTimeTable(week);
        if (poll==null){
            Common.info("tt_vote: entity not found, strange. when this can be happen?");
            poll = new TTPoll();
        }

        if(poll.canVoteAvailable()){
            poll.vote(time, userName);
            String json = updateTimeTable(week, poll);
            Common.info("updated: " + json);

            response.getWriter().write(json);
        }else{
            Common.info("tt_vote ignored: vote not available.");

            response.sendError(500, "vote not available.");
        }
    }
}

