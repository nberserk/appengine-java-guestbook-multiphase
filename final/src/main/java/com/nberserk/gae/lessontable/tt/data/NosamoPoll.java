package com.nberserk.gae.lessontable.tt.data;

import java.util.*;

public class NosamoPoll {
    public static String RESERVE = "취소";

    // to TreeeSet
    TreeSet<Slot> slots = new TreeSet<>();

    int state = 0; //0:preparing,  1: open, 2:closed
    String dateString;
    public void startPoll() {
        state = 1;
    }
    public void endPoll(){
        Random r = new Random(System.currentTimeMillis());
        for (Slot s : slots) {
            s.lottery(r);
        }
        state = 2;
    }

    public boolean canVoteAvailable(){
        if (state==1)
            return true;
        return false;
    }

    public boolean isPollDone(){
        if(state==2)
            return true;
        return false;
    }

    public static Calendar findMonDay() {
        Calendar date = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
        int diff = Calendar.MONDAY - date.get(Calendar.DAY_OF_WEEK);
//        if (!(diff > 0)) {
//            diff += 7;
//        }
        date.add(Calendar.DAY_OF_MONTH, diff);
        return date;
    }

    public NosamoPoll(){
        Calendar date = findMonDay();
        dateString = String.format("%d년 %d월 %d일 - 레슨시간표.", date.get(Calendar.YEAR), date.get(Calendar.MONTH)+1, date.get(Calendar.DAY_OF_MONTH));

        slots.add(new SlotSort("노사모 17:00", ""));
    }

    public Set<Slot> getSlots() {
        return slots;
    }

    public Slot getSlot(String time){
        for (Slot timeSlot : slots) {
            if (timeSlot.getTime().equals(time))
                return timeSlot;
        }
        return null;
    }

    public void vote(String time, String name){
        // remove if already voted
        for (Slot s : slots) {
            if (s.isVoted(name)){
                s.removeVoter(name);
                break;
            }
        }

        //
        Slot s = getSlot(time);
        if (s==null){
            //s = new Slot(time, name);
            //slots.add(s);
        }else{
            s.addVoter(name);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("state:" + state +"n");

        for(Slot s: slots){
            builder.append(s.toString());
        }

        return builder.toString();
    }
}
