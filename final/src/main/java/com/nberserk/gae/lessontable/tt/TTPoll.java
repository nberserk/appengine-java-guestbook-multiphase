package com.nberserk.gae.lessontable.tt;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class TTPoll {
    int state = 1; // 1: open, 2:closed
    public void startPoll() {
        state = 1;
    }

    public void endPoll(){
        state = 0;
    }

    static public class Slot implements Comparable<Slot>{
        String time;
        String luckyMan;
        ArrayList<String> voter = new ArrayList<String>();

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
    }
    // to TreeeSet
    TreeSet<Slot> slots = new TreeSet<Slot>();
    boolean lotteryDone;

    public TTPoll(){}
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

    public void lottery(){
        for (Slot s : slots) {
            s.lottery();
        }
        lotteryDone =true;
    }

    public boolean isLotteryDone(){
        return lotteryDone;
    }
}
