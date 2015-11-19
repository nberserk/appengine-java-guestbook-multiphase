
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
     * @param date : 050202 
     * @return null if not found
     */
    public static TTPoll getTimeTable(String date){
    	 DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
         Key key = KeyFactory.createKey(KIND, date);
         
         try {
 			Entity entity = ds.get(key);
 			TTPoll tt = sGson.fromJson((String) entity.getProperty(ENTITY_KEY), TTPoll.class);
 			return tt;
         } catch (EntityNotFoundException e) {
        	 return null;
         }         
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
        
        String desiredDate = req.getParameter("date");
        if (desiredDate==null){            
            desiredDate = getThisWeek();
        }
        Common.info("desiredDate:"+desiredDate);
        
        String timeslot = req.getParameter("lessonTime");
        if (timeslot==null){
        	Common.info("lessonTime null");
            resp.sendRedirect(URL_REDIRECT);
            return;
        }        

        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Key key = KeyFactory.createKey(KIND, desiredDate);
        
        try {
			Entity entity = ds.get(key);
			TTPoll tt = sGson.fromJson((String) entity.getProperty(ENTITY_KEY), TTPoll.class);

            tt.vote(timeslot, userName);

			String jsonString = sGson.toJson(tt);
			entity.setProperty(ENTITY_KEY, jsonString);
			ds.put(entity);
			Common.info("updated: " + jsonString);
		} catch (EntityNotFoundException e) {

            TTPoll tt = new TTPoll();
			tt.vote(timeslot, userName);

			String jsonString = sGson.toJson(tt);

            Entity entity = new Entity(KIND, desiredDate);
			entity.setProperty(ENTITY_KEY, jsonString);
			ds.put(entity);
			Common.info("added: " + jsonString);     
		}        

        resp.sendRedirect(URL_REDIRECT);
    }
}
//[END all]
