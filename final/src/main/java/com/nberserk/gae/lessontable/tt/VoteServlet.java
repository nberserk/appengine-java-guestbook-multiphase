
package com.nberserk.gae.lessontable.tt;

import com.google.appengine.api.datastore.*;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.nberserk.gae.lessontable.Common;
import com.nberserk.gae.lessontable.tt.data.TTPoll;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class VoteServlet extends HttpServlet {    
	private static final long serialVersionUID = 1435452240677102479L;
	public static final String KIND = "vote";
    public static final String ENTITY_KEY = "json";
    public static final String PARAM_DATE = "date";
    public static final String URL_REDIRECT = "/lesson.html";    

    private static Gson sGson = new Gson();    
    
    public static String getThisWeek(){
    	Date now = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("yyww");
        TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
        sd.setTimeZone(tz);
        return sd.format(now);
    }
    
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
            Text text = (Text) entity.getProperty(ENTITY_KEY);
            TTPoll tt = sGson.fromJson(text.getValue(), TTPoll.class);
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
            entity = new Entity(VoteServlet.KIND, week);
        }
        String jsonString = sGson.toJson(poll);
        Text text = new Text(jsonString);
        entity.setProperty(VoteServlet.ENTITY_KEY, text);
        ds.put(entity);
        return jsonString;
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
            week = getThisWeek();
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

