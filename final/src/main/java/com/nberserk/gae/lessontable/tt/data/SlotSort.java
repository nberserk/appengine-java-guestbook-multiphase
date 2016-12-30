package com.nberserk.gae.lessontable.tt.data;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by darren on 30/12/2016.
 */
public class SlotSort extends Slot{
    ArrayList<String> sorted = new ArrayList<>();


    public SlotSort(String time, String desc) {
        super(time, desc);
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
    }
}
