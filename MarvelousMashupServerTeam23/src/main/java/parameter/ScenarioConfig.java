package parameter;

import java.util.Arrays;
import java.util.Objects;

/**
 * This class is used when parsing the scenario config JSON file.
 * @author Luka Stoehr
 */
public class ScenarioConfig {

    public GrassRockEnum[][] scenario;
    public String name;
    public String author;

    /**
     * The constructor of the ScenarioConfig-class. It creates a ScenarioConfig-Object.
     * This constructor is only used for test purposes!
     *
     * @author Sarah Engele
     *
     * @param scenario The two dimensional GradRockEnum that describes the map (at the beginning)
     * @param name The name of the scenario config
     * @param author the author of the scenario config
     */
    public ScenarioConfig(GrassRockEnum[][] scenario, String name, String author){
        this.scenario = scenario;
        this.name = name;
        this.author = author;
    }

    /**
     * Indicates whether some other object is "equal to" this one. Two ScenarioConfigs are equal if all attributes are
     * equal.
     *
     * @author Matthias Ruf
     * @param o the reference object with which to compare.
     * @return if the two ScenarioConfigs are equal or not.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScenarioConfig that = (ScenarioConfig) o;

        //check whether the arrays are equal
        if (this.scenario.length != that.scenario.length) return false;
        for (int i = 0; i < that.scenario.length; i++) {
            if(!Arrays.equals(this.scenario[i],that.scenario[i])){
                return false;
            }
        }
        // check all other parameters
        return  Objects.equals(name, that.name) &&
                Objects.equals(author, that.author);
    }


}
