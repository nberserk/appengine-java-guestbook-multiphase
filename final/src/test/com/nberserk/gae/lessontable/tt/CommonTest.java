package com.nberserk.gae.lessontable.tt;

import com.nberserk.gae.lessontable.Common;
import com.nberserk.gae.lessontable.tt.data.TTPoll;
import org.junit.Test;

import java.util.Calendar;

/**
 * Created by darren on 04/04/2017.
 */
public class CommonTest {

    @Test
    public void tuesday(){
        Calendar c = Common.findTuesDay();
        System.out.println(c.get(Calendar.DAY_OF_MONTH));

        TTPoll t = new TTPoll();
        System.out.println(t);
    }
}
