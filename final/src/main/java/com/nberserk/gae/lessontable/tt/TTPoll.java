package com.nberserk.gae.lessontable.tt;

import java.util.*;

public class TTPoll {

    static public class Slot implements Comparable<Slot>{
        String time;
        String luckyMan;
        ArrayList<String> voter = new ArrayList<String>();

        public Slot(){}
        public Slot(String time, String name){
            this.time = time;
            voter.add(name);
        }

        public void addVoter(String v){
            voter.add(v);
        }

        public boolean isVoted(String name){
            if ( voter.contains(name))
                return true;
            return false;
        }

        public void removeVoter(String name){
            voter.remove(name);
        }

        public boolean isEmpty(){
            if(voter.size()==0) return true;
            return false;
        }

        public String getTime(){
            return time;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Slot){
                Slot o = (Slot)obj;
                return time.equals(o.getTime());
            }
            return false;
        }

        @Override
        public int hashCode() {
            return time.hashCode();
        }

        @Override
        public int compareTo(Slot o) {
            return time.compareTo(o.getTime());
        }

        public void lottery() {
            int size = voter.size();
            int lucky = new Random().nextInt(size);
            luckyMan = voter.get(lucky);
        }

        public String getLuckyMan(){
            return luckyMan;
        }

        @Override
        public String toString() {
            return "time:" + time + "voter:"+voter.toString() + "luckyMan:"+luckyMan;
        }
    }
    // to TreeeSet
    TreeSet<Slot> slots = new TreeSet<Slot>();

    int state = 0; //0:preparing,  1: open, 2:closed
    String dateString;
    public void startPoll() {
        state = 1;
    }
    public void endPoll(){
        for (Slot s : slots) {
            s.lottery();
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

    public static Calendar findTuesDay() {
        Calendar date = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
        int diff = Calendar.TUESDAY - date.get(Calendar.DAY_OF_WEEK);
//        if (!(diff > 0)) {
//            diff += 7;
//        }
        date.add(Calendar.DAY_OF_MONTH, diff);
        return date;
    }

    public TTPoll(){
        Calendar date = findTuesDay();
        dateString = String.format("%d년 %d월 %d일 - 레슨시간표.", date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
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
                if(s.isEmpty())
                    slots.remove(s);
                break;
            }
        }

        //
        Slot s = getSlot(time);
        if (s==null){
            s = new Slot(time, name);
            slots.add(s);
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
