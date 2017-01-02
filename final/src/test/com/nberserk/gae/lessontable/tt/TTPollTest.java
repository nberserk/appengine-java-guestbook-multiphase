package com.nberserk.gae.lessontable.tt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nberserk.gae.lessontable.tt.data.NosamoPoll;
import com.nberserk.gae.lessontable.tt.data.Slot;
import com.nberserk.gae.lessontable.tt.data.SlotSorted;
import com.nberserk.gae.lessontable.tt.data.TTPoll;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TTPollTest {

    @Test
    public void vote(){
        String time = TTPoll.times[0];
        String t2 = TTPoll.times[1];
        String darren = "darren";
        String andrew = "andrew";


        TTPoll poll = new TTPoll();
        poll.vote(time, darren);
        Slot s = poll.getSlot(time);
        assertEquals( true, s.isVoted(darren) );

        poll.vote(t2, darren);
        assertEquals(0, poll.getSlot(time).getVoter().size());
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

        assertTrue(poll.getSlot(time).getLuckyMan() != null);
        System.out.println(poll.getSlot(time).getLuckyMan());
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

    @Test
    public void NosamoPoll(){
        TTPoll p = new NosamoPoll();
        assertEquals(2, p.getSlots().size());

        p.startPoll();
        p.vote(NosamoPoll.NOSAMO_SLOT, "darren");
        p.vote(NosamoPoll.NOSAMO_SLOT, "andrew");
        p.vote(NosamoPoll.NOSAMO_SLOT, "rachael");
        p.vote(NosamoPoll.NOSAMO_SLOT, "a");
        p.vote(NosamoPoll.NOSAMO_SLOT, "b");
        p.vote(NosamoPoll.NOSAMO_SLOT, "c");
        p.endPoll();

        //assertEquals(6, p.getSlot(NosamoPoll.NOSAMO_SLOT) )

        assertTrue(p.getSlot(NosamoPoll.NOSAMO_SLOT) != null);
        System.out.println(p.getSlot(NosamoPoll.NOSAMO_SLOT));
    }

    @Test
    public void NosamoPollGson(){
        TTPoll p = new NosamoPoll();
        p.startPoll();
        p.vote(NosamoPoll.NOSAMO_SLOT, "darren");
        p.vote(NosamoPoll.NOSAMO_SLOT, "andrew");
        p.vote(NosamoPoll.NOSAMO_SLOT, "rachael");
        p.vote(NosamoPoll.NOSAMO_SLOT, "a");
        p.vote(NosamoPoll.NOSAMO_SLOT, "b");
        p.vote(NosamoPoll.NOSAMO_SLOT, "c");

        RuntimeTypeAdapterFactory<Slot> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
                .of(Slot.class, "type")
                .registerSubtype(SlotSorted.class);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();
        Type listType = new TypeToken<NosamoPoll>(){}.getType();

        String string = gson.toJson(p);


        NosamoPoll p2 = gson.fromJson(string, listType);
        p2.endPoll();
        System.out.println(p2);
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
