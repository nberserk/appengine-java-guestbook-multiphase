package com.nberserk.gae.lessontable.tt;

import com.nberserk.gae.lessontable.tt.data.Slot;
import com.nberserk.gae.lessontable.tt.data.TTPoll;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TTPollTest {

    @Test
    public void vote(){
        String time = "6:30";
        String t2 = "7:30";
        String darren = "darren";
        String andrew = "andrew";


        TTPoll poll = new TTPoll();
        poll.vote(time, darren);
        Slot s = poll.getSlot(time);
        assertEquals( true, s.isVoted(darren) );

        poll.vote(t2, darren);
        assertEquals(0, poll.getSlot(time).voter.size());
        assertEquals(true, poll.getSlot(t2).isVoted(darren));
    }

    @Test
    public void lottery(){
        String time = TTPoll.times[0];
        String t2 = TTPoll.times[1];

        String darren = "darren";
        String andrew = "andrew";


        TTPoll poll = new TTPoll();
        for (int i = 0; i < 7; i++) {
            poll.vote(time, "darren"+i);
        }
        poll.vote(time, andrew);
        poll.vote(time, "rachael");

        assertEquals(false, poll.isPollDone());

        poll.endPoll();
        assertEquals(true, poll.isPollDone());

        assertTrue(poll.getSlot(time).luckyMan != null);
        System.out.println(poll.getSlot(time).luckyMan);
    }

    @Test
    public void randomTest(){
        Random r = new Random(System.currentTimeMillis());

        int size = 3;
        for (int i = 0; i < 16; i++) {
            int lucky = r.nextInt(size);
            System.out.println(lucky);
        }
    }


    public void lotteryDistribution(){
        for (int k = 0; k < 10; k++){
            TTPoll poll = new TTPoll();
            int idx = 0;
            for (Slot s : poll.getSlots()){

                for (int i = 0; i < 2; i++) {
                    poll.vote(s.getTime(), new Integer(idx).toString());
                    idx++;
                }
            }

            poll.startPoll();
            poll.endPoll();

            int even = 0;
            int odd = 0;

            for (Slot s : poll.getSlots()){
                if (s.getTime().equals(TTPoll.RESERVE))
                    continue;
                String m = s.getLuckyMan();
                if ( Integer.valueOf(m).intValue() % 2 ==0)
                    even ++;
                else odd++;
            }

            System.out.println("even: " + even);
            System.out.println("odd: "+ odd);
        }

    }




}
