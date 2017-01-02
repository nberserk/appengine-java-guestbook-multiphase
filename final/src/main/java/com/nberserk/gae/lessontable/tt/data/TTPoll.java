package com.nberserk.gae.lessontable.tt.data;

import com.nberserk.gae.lessontable.Common;

import java.util.Calendar;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class TTPoll {
    public static String RESERVE = "취소";
    public static int MAX_LUCKY_GUY = 5;

    public static String[] times = {
            "A타임 5:30",
            "B타임 6:00",
            "C타임 6:30",
            "D타임 7:00",
            RESERVE
    };

    public static String[] descs = {
            "7부",
            "7부",
            "6부",
            "5부이상",
            ""
    };

    // to TreeeSet
    TreeSet<Slot> slots = new TreeSet<Slot>();

    int state = 0; //0:preparing,  1: open, 2:closed
    String dateString;
    public void startPoll() {
        state = 1;
    }
    public void endPoll(){
        Random r = new Random(System.currentTimeMillis());
        for (Slot s : slots) {
            if (s instanceof SlotSorted ){
                SlotSorted ss = (SlotSorted)s;
                ss.lottery(r);
            }else
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

    public TTPoll(){
        Calendar date = Common.findTuesDay();
        dateString = String.format("%d년 %d월 %d일 - 레슨시간표.", date.get(Calendar.YEAR), date.get(Calendar.MONTH)+1, date.get(Calendar.DAY_OF_MONTH));

        for (int i = 0; i < times.length; i++) {
            slots.add(new Slot(times[i], descs[i]));
        }
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
