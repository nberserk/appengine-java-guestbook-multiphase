package com.nberserk.gae.lessontable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nberserk.gae.lessontable.tt.RuntimeTypeAdapterFactory;
import com.nberserk.gae.lessontable.tt.data.Slot;
import com.nberserk.gae.lessontable.tt.data.SlotSorted;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;

public class Common {
	private static Logger sLogger = Logger.getLogger(Common.class.getName());
	private static Gson gson;

    static {
        RuntimeTypeAdapterFactory<Slot> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
                .of(Slot.class, "type")
                .registerSubtype(Slot.class)
                .registerSubtype(SlotSorted.class);

        gson = new GsonBuilder().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();
    }

    public static Gson getGson(){
        return gson;
    }

	public static void info(String s){
		sLogger.info(s);
	}

    public static Calendar findTuesDay() {
        TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
        Calendar date = Calendar.getInstance(tz);
        int diff = Calendar.WEDNESDAY - date.get(Calendar.DAY_OF_WEEK);
//        if (!(diff > 0)) {
//            diff += 7;
//        }
        date.add(Calendar.DAY_OF_MONTH, diff);
        date.setTimeZone(tz);
        return date;
    }

    public static Calendar findMonDay() {
        Calendar date = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
        int diff = Calendar.MONDAY - date.get(Calendar.DAY_OF_WEEK);

        date.add(Calendar.DAY_OF_MONTH, diff);
        return date;
    }

    public static String getThisWeek(){
    	Date now = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("yyww");
        TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
        sd.setTimeZone(tz);
        return sd.format(now);
    }
}
