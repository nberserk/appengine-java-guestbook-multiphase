/**
 * Copyright 2014 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//[START all]
package com.example.guestbook;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class VoteServlet extends HttpServlet {
    private static final String KIND = "vote";
    private static Logger sLogger = Logger.getLogger("VoteServlet");
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if (user==null){
            resp.sendRedirect("/status.jsp");
            return;
        }

        String userName = user.getEmail();

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
			String votedTime=null;
			boolean occupied = false;
            Map<String,Object> map = entity.getProperties();
            for(String k : map.keySet()){
            	if(userName.equals(map.get(k))){
            		votedTime = k;            
            	}
            	if(k.equals(timeslot))
            		occupied = true;
            } 
            
            if (!occupied) {
            	if(votedTime!=null)
            		entity.removeProperty(votedTime);
            	entity.setProperty(timeslot, userName);
                sLogger.info("updated: "+timeslot+", "+userName);
                ds.put(entity);
			}else{
				sLogger.info(timeslot + " already reserved");
			}
		} catch (EntityNotFoundException e) {
			Entity entity = new Entity(KIND, desiredDate);
			entity.setProperty(timeslot, user.getEmail());
			ds.put(entity);
			sLogger.info("added: " + entity.toString());     
		}        

        resp.sendRedirect("/guestbook.jsp");
    }
}
//[END all]
