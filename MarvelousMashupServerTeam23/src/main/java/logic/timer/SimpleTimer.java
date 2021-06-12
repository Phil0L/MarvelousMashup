package logic.timer;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class implememts a simple Timer, which has only four little functions:
 *
 *  * start
 *  * stop
 *  * pause
 *  * resume
 *
 *  when the timer has expired, the  method is executed.
 *
 * @author Matthias Ruf
 * @author Benno Hoelz
 */
public abstract class SimpleTimer {

    /**
     * the whole class based on instances of {@link java.util.Timer}
     */
    private Timer timer;

    /**
     * remaining runtime of the timer
     */
    private long delay;

    /**
     * time between 1.January.1970 and the last time the timer was started or resumed
     */
    private long startPoint;

    /**
     * Indicates whether the timer is currently counting down or paused
     */
    private boolean isRunning = false;

    /**
     * Indicates whether the timer is currently paused
     */
    private boolean isPaused = false;

    /**
     *
     * Constructor of the SimpleTimer class. Creates a SimpleTimer object.
     *
     * @author Matthias Ruf
     * @author Benno Hoelz
     *
     * @param delay remaining runtime of the timer
     */
    public SimpleTimer(long delay){
        timer = new Timer();
        this.delay = delay;
    }

    /**
     * stops the timer. The methode is not called
     *
     * @author Matthias Ruf
     * @author Benno Hoelz
     *
     */
    public boolean stop(){
        if(this.isRunning) {
            timer.cancel();
            this.isRunning = false;
            this.isPaused = false;
            return true;
        }else {
            return false;
        }
    }

    /**
     * starts the timer
     *
     * @author Matthias Ruf
     * @author Benno Hoelz
     *
     * @return
     */
    public boolean start(){
        if(this.isPaused || this.isRunning){
            return false;
        }

        this.startPoint = new Date().getTime();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                execute();
            }
        }, delay);
        this.isRunning = true;
        return true;
    }


    /**
     *
     * pauses the timer
     *
     * @author Matthias Ruf
     * @author Benno Hoelz
     *
     * @return whether the call was successful or not.
     */
    public boolean pause(){

        if (this.isRunning && !isPaused) {
            delay = delay + this.startPoint - (new Date()).getTime();
            timer.cancel();
            this.isPaused = true;
            return true;
        }

        return false;
    }

    /**
     * resumes the timer
     *
     * @author Matthias Ruf
     * @author Benno Hoelz
     *
     * @return
     */
    public boolean resume(){

        if(!this.isPaused || !this.isRunning){
            return false;
        }

        this.startPoint = new Date().getTime();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                execute();
            }
        }, delay);

        return true;
    }

    /**
     * This methode is executed, when the timer expires
     */
    public abstract void execute();


}