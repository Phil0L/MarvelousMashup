package parameter;

/**
 * This class is used when parsing the MatchConfig using JSON.
 *
 * @author Luka Stoehr
 */
public class MatchConfig {

    public int maxRounds;
    public int maxRoundTime;
    public int maxGameTime;
    public int maxAnimationTime;
    public int spaceStoneCD;
    public int mindStoneCD;
    public int realityStoneCD;
    public int powerStoneCD;
    public int timeStoneCD;
    public int soulStoneCD;
    public int mindStoneDMG;
    public int maxPauseTime;
    public int maxResponseTime;

    /**
     * The constructor of the ScenarioConfig-class. It creates a ScenarioConfig-Object.
     * This constructor is only used for test purposes!
     *
     * @author Sarah Engele
     *
     * @param maxRounds the maximum amount of rounds
     * @param maxRoundTime the maximum time for each round
     * @param maxGameTime the maximum time for each game
     * @param maxAnimationTime the maximum animation time
     * @param spaceStoneCD Cooldown Space Stone
     * @param mindStoneCD Cooldown Mind Stone
     * @param realityStoneCD Cooldown Reality Stone
     * @param powerStoneCD Cooldown Power Stone
     * @param timeStoneCD Cooldown Time Stone
     * @param soulStoneCD Cooldown Soul Stone
     * @param mindStoneDMG Damage Mind Stone
     * @param maxPauseTime the maximum time to have a pause
     * @param maxResponseTime the maximum response time
     */
    public MatchConfig(int maxRounds, int maxRoundTime, int maxGameTime, int maxAnimationTime, int spaceStoneCD,
                       int mindStoneCD, int realityStoneCD, int powerStoneCD, int timeStoneCD, int soulStoneCD,
                       int mindStoneDMG, int maxPauseTime, int maxResponseTime) {
        this.maxRounds = maxRounds;
        this.maxRoundTime = maxRoundTime;
        this.maxGameTime = maxGameTime;
        this.maxAnimationTime = maxAnimationTime;
        this.spaceStoneCD = spaceStoneCD;
        this.mindStoneCD = mindStoneCD;
        this.realityStoneCD = realityStoneCD;
        this.powerStoneCD = powerStoneCD;
        this.timeStoneCD = timeStoneCD;
        this.soulStoneCD = soulStoneCD;
        this.mindStoneDMG = mindStoneDMG;
        this.maxPauseTime = maxPauseTime;
        this.maxResponseTime = maxResponseTime;
    }

    /**
     * Indicates whether some other object is "equal to" this one. Two MatchConfigs are equal if all attributes are
     * equal.
     *
     * @author Matthias Ruf
     * @param o the reference object with which to compare.
     * @return if the two MatchConfigs are equal or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchConfig that = (MatchConfig) o;
        return maxRounds == that.maxRounds && maxRoundTime == that.maxRoundTime && maxGameTime == that.maxGameTime &&
                maxAnimationTime == that.maxAnimationTime && spaceStoneCD == that.spaceStoneCD &&
                mindStoneCD == that.mindStoneCD && realityStoneCD == that.realityStoneCD &&
                powerStoneCD == that.powerStoneCD && timeStoneCD == that.timeStoneCD &&
                soulStoneCD == that.soulStoneCD && mindStoneDMG == that.mindStoneDMG &&
                maxPauseTime == that.maxPauseTime && maxResponseTime == that.maxResponseTime;
    }




}
