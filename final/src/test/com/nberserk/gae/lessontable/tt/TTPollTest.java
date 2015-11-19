package com.nberserk.gae.lessontable.tt;

import org.junit.Test;

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
        TTPoll.Slot s = poll.getSlot(time);
        assertEquals( true, s.isVoted(darren) );

        poll.vote(t2, darren);
        assertEquals(null, poll.getSlot(time));
        assertEquals(true, poll.getSlot(t2).isVoted(darren));
    }

    @Test
    public void lottery(){
        String time = "6:30";
        String t2 = "7:30";
        String darren = "darren";
        String andrew = "andrew";


        TTPoll poll = new TTPoll();
        poll.vote(time, darren);
        poll.vote(time, andrew);
        poll.vote(time, "rachael");

        assertEquals(false, poll.isLotteryDone());

        poll.lottery();
        assertEquals(true, poll.isLotteryDone());

        assertTrue(poll.getSlot(time).luckyMan != null);
    }
}
