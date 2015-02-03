
package com.example.guestbook;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.guestbook.TimeTable.TimeSlot;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.repackaged.com.google.gson.Gson;

public class VoteServlet extends HttpServlet {    
	private static final long serialVersionUID = 1435452240677102479L;
	public static final String KIND = "vote";
    public static final String ENTITY_KEY = "json";
    public static final String PARAM_DATE = "date";
    public static final String URL_REDIRECT = "/lesson.html";
    
    public static Logger sLogger = Logger.getLogger("VoteServlet");
    private static Gson sGson = new Gson();    
    
    public static String getTodayDate(){
    	Date now = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("yyMMdd");
        sd.setTimeZone(TimeZone.getTimeZone("asia/seoul"));
        return sd.format(now);
    }
    
    /**
     * 
     * @param date : 050202 
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
        	sLogger.info("name null");
        	resp.sendRedirect(URL_REDIRECT);
        	return;
        }

        
        String desiredDate = req.getParameter("date");
        if (desiredDate==null){            
            desiredDate = getTodayDate();
        }
        sLogger.info("desiredDate:"+desiredDate);
        
        String timeslot = req.getParameter("lessonTime");
        if (timeslot==null){
        	sLogger.info("lessonTime null");
            resp.sendRedirect(URL_REDIRECT);
            return;
        }        

        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Key key = KeyFactory.createKey(KIND, desiredDate);
        
        try {
			Entity entity = ds.get(key);
			TimeTable tt = sGson.fromJson((String) entity.getProperty(ENTITY_KEY), TimeTable.class);
			// remove if already voted
			Set<TimeSlot> tss = tt.getSlots();
			for (TimeSlot t : tss) {
				if (t.getVoter().contains(userName)){
					t.getVoter().remove(userName);
					if(t.getVoter().size()==0){
						sLogger.info(t.getTime() + " is removed");
						tss.remove(t);
					}
					break;
				}
			}
			
			TimeSlot ts = tt.getSlot(timeslot);
			if (ts==null){
				ts = new TimeSlot(timeslot, userName);
				tt.addSlot(ts);
			}else{
				ts.addVoter(userName);
			}
			
			String jsonString = sGson.toJson(tt);
			entity.setProperty(ENTITY_KEY, jsonString);
			ds.put(entity);
			sLogger.info("updated: " + jsonString);
		} catch (EntityNotFoundException e) {
			Entity entity = new Entity(KIND, desiredDate);
			TimeTable tt = new TimeTable();
			tt.getSlots().add(new TimeSlot(timeslot, userName));
			String jsonString = sGson.toJson(tt);
			entity.setProperty(ENTITY_KEY, jsonString);
			ds.put(entity);
			sLogger.info("added: " + jsonString);     
		}        

        resp.sendRedirect(URL_REDIRECT);
    }
}
//[END all]
