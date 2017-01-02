package com.nberserk.gae.lessontable.tt.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
* Created by darren on 30/12/2016.
*/
public class Slot implements Comparable<Slot>{
    protected static Logger sLogger = Logger.getLogger(Slot.class.getName());
    protected String time, desc;
    protected String luckyMan;
    protected ArrayList<String> voter = new ArrayList<String>();

    public Slot(){}
    public Slot(String time){
        this.time = time;
    }
    public Slot(String time, String desc){
        this.time = time;
        this.desc = desc;
    }

    public void addVoter(String v){
        voter.add(v);
    }
    public List<String> getVoter(){ return  voter;}

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

    public void lottery(Random r) {
        if(time.equals(TTPoll.RESERVE))
            return;
        int size = voter.size();
        if(size==0)
            return;
        if (size<= TTPoll.MAX_LUCKY_GUY){
            luckyMan = voter.get(0);
            for (int i = 1; i < size; i++) {
                luckyMan += ", " + voter.get(i);
            }
            return;
        }

        ArrayList<String> copy = new ArrayList<>(voter);
        int l = r.nextInt(copy.size());
        luckyMan = copy.get(l);
        copy.remove(l);
        for (int i = 1; i < TTPoll.MAX_LUCKY_GUY; i++) {
            l = r.nextInt(copy.size());
            luckyMan += ", " + copy.get(l);
            copy.remove(l);
        }

        sLogger.info("lottery called");
    }

    public String getLuckyMan(){
        return luckyMan;
    }

    @Override
    public String toString() {
        return "time:" + time + "voter:"+voter.toString() + "luckyMan:"+luckyMan;
    }
}
