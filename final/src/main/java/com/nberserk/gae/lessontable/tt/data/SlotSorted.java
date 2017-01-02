package com.nberserk.gae.lessontable.tt.data;

import com.nberserk.gae.lessontable.Common;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by darren on 30/12/2016.
 */
public class SlotSorted extends Slot{
    ArrayList<String> sorted;

    public SlotSorted(){

    }

    public SlotSorted(String time, String desc) {
        super(time, desc);
        sorted  = new ArrayList<>();
    }

    @Override
    public void lottery(Random r) {
        if(time.equals(TTPoll.RESERVE))
            return;

        int size = voter.size();
        if(size==0)
            return;

        ArrayList<String> copy = new ArrayList<>(voter);
        while(copy.size()>0){
            int l = r.nextInt(copy.size());
            String pick = copy.get(l);
            copy.remove(l);
            sorted.add(pick);
        }
        luckyMan = sorted.toString();

        Common.info("sorted lottery called");
    }
}
