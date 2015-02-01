
package com.example.guestbook;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

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
    private static final String KIND = "vote";
    private static final String ENTITY_KEY = "json";
    private static Logger sLogger = Logger.getLogger("VoteServlet");
    private static Gson sGson = new Gson();
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
//        UserService userService = UserServiceFactory.getUserService();
//        User user = userService.getCurrentUser();
//        if (user==null){
//            resp.sendRedirect("/status.jsp");
//            return;
//        }

        String userName = req.getParameter("name");
        if(userName==null){
        	resp.sendRedirect("/status.jsp");
        	return;
        }

        Date now = new Date();
        String desiredDate = req.getParameter("date");
        if (desiredDate==null){
            SimpleDateFormat sd = new SimpleDateFormat("yyMMdd");
            desiredDate = sd.format(now);
        }

        String timeslot = req.getParameter("time");
        if (timeslot==null){
            resp.sendRedirect("/status.jsp");
            return;
        }

        sLogger.info("desiredDate:"+desiredDate);

        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Key key = KeyFactory.createKey(KIND, desiredDate);
        
        try {
			Entity entity = ds.get(key);
			TimeTable tt = sGson.fromJson((String) entity.getProperty(ENTITY_KEY), TimeTable.class);
			// remove if already voted
			ArrayList<TimeSlot> tss = tt.getSlots();
			for (TimeSlot t : tss) {
				if (t.getVoter().contains(userName)){
					t.getVoter().remove(userName);
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
		} catch (EntityNotFoundException e) {
			Entity entity = new Entity(KIND, desiredDate);
			TimeTable tt = new TimeTable();
			tt.getSlots().add(new TimeSlot(timeslot, userName));
			String jsonString = sGson.toJson(tt);
			entity.setProperty(ENTITY_KEY, jsonString);
			ds.put(entity);
			sLogger.info("added: " + jsonString);     
		}        

        resp.sendRedirect("/guestbook.jsp");
    }
}
//[END all]
