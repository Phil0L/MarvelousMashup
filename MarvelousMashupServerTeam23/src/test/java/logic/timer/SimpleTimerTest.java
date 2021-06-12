package logic.timer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for the SimpleTimerTest
 *
 * @author Matthias Ruf
 *
 * IMPORTANT: Do not run this test while other tests are running!!!!!!
 */
public class SimpleTimerTest {

    /**
     * toggles when execute method is called
     */
    private boolean checkPoint = false;
    /**
     * SimpleTimer instance for the following test cases
     */
    private SimpleTimer simpleTimer;


    /**
     * Creates the setup for the SimpleTimer test
     * @author Matthias Ruf
     */
    @BeforeEach
    public void setUp(){
        simpleTimer = new SimpleTimer(1000) {
            @Override
            public void execute() {
                checkPoint = !checkPoint;
            }
        };
    }


    /**
     * Test for a normal use of the timer, without any exceptions.
     * checks following function
     *
     *  start
     *  pause
     *  resume
     *
     *
     * @autor Matthias Ruf
     */
    @Test
    public void timertest(){



        try {
            // timer start    time ~ 0 --- timerDelay 1000
            simpleTimer.start();
            Thread.sleep(100);

            // timer pause time ~ 100 ---  timerDelay 900
            assert simpleTimer.pause();
            assert !checkPoint;
            Thread.sleep(1000);

            // timer resume time ~ 1100  ---  timerDelay ~ 900
             assert !checkPoint;
             assert simpleTimer.resume();

             Thread.sleep(1000);
             assert checkPoint;


        }catch (InterruptedException ie){
            fail("InterruptException was thrown: " + ie.getMessage());
            ie.printStackTrace();
        }

    }



    /**
     * Test for a normal use of the timer, without any exceptions.
     * checks following function
     *
     * stop
     *
     * @autor Matthias Ruf
     */
    @Test
    public void stopTest(){

        simpleTimer.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
        simpleTimer.stop();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
        assert !checkPoint;



    }

    /**
     * tests a few exceptions (dt. "Ausnahmen") of the timer
     *
     */
    @Test
    public void testExceptions(){

        // a timer which hasn't started already cannot be close

        assertFalse(simpleTimer.stop());
        // a timer which hasn't started already cannot be resume
        assertFalse(simpleTimer.resume());
        // a timer which hasn't started already cannot be pause
        assertFalse(simpleTimer.pause());

        // start the timer
        assertTrue(simpleTimer.start());

        // a running timer cannot be started twice
        assertFalse(simpleTimer.start());

        //pause the timer
        assertTrue(simpleTimer.pause());
        // a paused timer cannot be started
        assertFalse(simpleTimer.start());

    }
}
