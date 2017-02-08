package com.nberserk.gae.lessontable.tt.data;

import com.nberserk.gae.lessontable.Common;

import java.util.Calendar;

public class NosamoPoll extends TTPoll{
    public static String NOSAMO_SLOT = "오사모 19:00";

    public NosamoPoll(){
        Calendar date = Common.findMonDay();
        dateString = String.format("%d년 %d월 %d일 - 레슨시간표.", date.get(Calendar.YEAR), date.get(Calendar.MONTH)+1, date.get(Calendar.DAY_OF_MONTH));

        slots.clear();
        slots.add(new SlotSorted(NOSAMO_SLOT, ""));
        slots.add(new SlotSorted(RESERVE, ""));
    }
}
