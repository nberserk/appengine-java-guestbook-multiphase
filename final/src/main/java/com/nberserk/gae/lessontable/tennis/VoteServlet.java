
package com.nberserk.gae.lessontable.tennis;

import com.google.appengine.api.datastore.*;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.nberserk.gae.lessontable.Common;
import com.nberserk.gae.lessontable.tennis.TimeTable.TimeSlot;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class VoteServlet extends HttpServlet {	
	private static final long serialVersionUID = -7139861581374794024L;
	public static final String KIND = "tennisVote";
    public static final String ENTITY_KEY = "json";
    public static final String PARAM_DATE = "date";
    public static final String URL_REDIRECT = "/weeklylesson.html";
    
    
    private static Gson sGson = new Gson();    
    
    public static String getTodayDate(){
    	Date now = new Date();
		// change week every Friday noon
		now.setTime((long) (now.getTime() + 1000 * 60 * 60 * 24 * 1.5));
        SimpleDateFormat sd = new SimpleDateFormat("yyww");
        TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
        sd.setTimeZone(tz);
		String r = sd.format(now);
		Common.info(" Today is:" + now.toString() + " " + r);
		return r;
    }
    
    /**
     * 
     * @param date : 0508 , yyww
     * @return null if not found
     */
    public static TimeTable getTimeTable(String date){
    	 DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
         Key key = KeyFactory.createKey(KIND, date);
         
         try {
 			Entity entity = ds.get(key);
 			TimeTable tt = sGson.fromJson((String) entity.getProperty(ENTITY_KEY), TimeTable.class);
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
            desiredDate = getTodayDate();
        }
        Common.info("desiredDate:"+desiredDate);
        
        String timeslot = req.getParameter("lessonTime");
        if (timeslot==null){
        	Common.info("lessonTime null");
            resp.sendRedirect(URL_REDIRECT);
            return;
        }
        
        boolean isRemove = req.getParameter("iscancel").equals("1");
        Common.info("iscancel is " + isRemove);

        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Key key = KeyFactory.createKey(KIND, desiredDate);        
        try {
			Entity entity = ds.get(key);
			TimeTable tt = sGson.fromJson(
					(String) entity.getProperty(ENTITY_KEY), TimeTable.class);
			if(isRemove){
				TimeSlot ts = tt.getSlot(timeslot);
				if (ts.getVoter().contains(userName)) {
					ts.getVoter().remove(userName);
					Common.info(ts.getTime() + " is removed");
				} else {
					Common.info(ts.getTime() + "doesn't have " + userName);
				}
			}else{
				TimeSlot ts = tt.getSlot(timeslot);
				if (ts==null){
					ts = new TimeSlot(timeslot, userName);
					tt.addSlot(ts);
				}else{
					ts.addVoter(userName);
				}	
			}			
			
			String jsonString = sGson.toJson(tt);
			entity.setProperty(ENTITY_KEY, jsonString);
			ds.put(entity); // TODO, put when changes 
			Common.info("updated: " + jsonString);
		} catch (EntityNotFoundException e) {
			Common.info("entity not found for "+desiredDate);
			if(isRemove==false){
				Entity entity = new Entity(KIND, desiredDate);
				TimeTable tt = new TimeTable();
				tt.getSlots().add(new TimeSlot(timeslot, userName));
				String jsonString = sGson.toJson(tt);
				entity.setProperty(ENTITY_KEY, jsonString);
				ds.put(entity);
				Common.info("added: " + jsonString);	
			}			     
		}        

        resp.sendRedirect(URL_REDIRECT);
    }
}
