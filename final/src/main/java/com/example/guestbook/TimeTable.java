package com.example.guestbook;

import java.util.ArrayList;
import java.util.List;

public class TimeTable {
	static public class TimeSlot{
		String time;
		ArrayList<String> voter = new ArrayList<String>();
		
		TimeSlot(String time, String name){
			this.time = time;
			voter.add(name);
		}
		
		void addVoter(String v){
			voter.add(v);
		}
		
		List<String> getVoter(){
			return voter;
		}
		
		String getTime(){
			return time;
		}
	}	
	ArrayList<TimeSlot> slots=new ArrayList<TimeSlot>();
	
	public ArrayList<TimeSlot> getSlots() {
		return slots;
	}	
		
	public TimeSlot getSlot(String time){
		for (TimeSlot timeSlot : slots) {
			if (timeSlot.getTime().equals(time))
				return timeSlot;
		}
		return null;
	}
	
	public void addSlot(TimeSlot ts){		
		slots.add(ts);
	}
}
