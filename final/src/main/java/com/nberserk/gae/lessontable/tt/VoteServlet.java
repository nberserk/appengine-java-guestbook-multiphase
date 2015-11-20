
package com.nberserk.gae.lessontable.tt;

import com.google.appengine.api.datastore.*;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.nberserk.gae.lessontable.Common;

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
        SimpleDateFormat sd = new SimpleDateFormat("yyMM");
        TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
        sd.setTimeZone(tz);
        return sd.format(now);
    }
    
    /**
     * 
     * @param week : week
     * @return null if not found
     */
    public static TTPoll getTimeTable(String week){
    	 DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
         Key key = KeyFactory.createKey(KIND, week);
         
         try {
 			Entity entity = ds.get(key);
 			TTPoll tt = sGson.fromJson((String) entity.getProperty(ENTITY_KEY), TTPoll.class);
 			return tt;
         } catch (EntityNotFoundException e) {
        	 return null;
         }         
    }

    public static void updateTimeTable(String week, TTPoll poll){
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Key key = KeyFactory.createKey(KIND, week);

        Entity entity = null;
        try {
            entity = ds.get(key);
        } catch (EntityNotFoundException e) {
            entity = new Entity(VoteServlet.KIND, week);
        }
        String jsonString = sGson.toJson(poll);
        entity.setProperty(VoteServlet.ENTITY_KEY, jsonString);
        ds.put(entity);
    }
    
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String userName = req.getParameter("name");
        if(userName==null){
        	Common.info("name null");
        	resp.sendRedirect(URL_REDIRECT);
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
            resp.sendRedirect(URL_REDIRECT);
            return;
        }        

        TTPoll poll = getTimeTable(week);
        if (poll==null){
            Common.info("tt_vote: entity not found, strange. when this can be happen?");
            poll = new TTPoll();
        }

        if(poll.canVoteAvailable()){
            poll.vote(time, userName);
            updateTimeTable(week, poll);
            Common.info("updated: " + poll);
        }else{
            Common.info("tt_vote ignored: vote not available");
        }

        resp.sendRedirect(URL_REDIRECT);
    }
}

