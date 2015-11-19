package com.nberserk.gae.lessontable.tennis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class TimeTable {
	static public class TimeSlot implements Comparable<TimeSlot>{
		String time;
		ArrayList<String> voter = new ArrayList<String>();
		public TimeSlot(){}
		public TimeSlot(String time, String name){
			this.time = time;
			voter.add(name);
		}
		
		public void addVoter(String v){
			voter.add(v);
		}
		
		public List<String> getVoter(){
			return voter;
		}
		
		public String getTime(){
			return time;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof TimeSlot){
				TimeSlot o = (TimeSlot)obj;
				return time.equals(o.getTime());
			}
			return false;
		}
		
		@Override
		public int hashCode() {			
			return time.hashCode();
		}
		
		@Override
		public int compareTo(TimeSlot o) {
			return time.compareTo(o.getTime());
		}
	}	
	// to TreeeSet
	TreeSet<TimeSlot> slots=new TreeSet<TimeSlot>();
	
	public TimeTable(){}
	public Set<TimeSlot> getSlots() {
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
