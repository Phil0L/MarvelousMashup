package parameter;

import com.google.gson.Gson;
import java.io.*;

/**
 * This class parses and stores the configuration files (scenario, character and match configuration). Parsing
 * should only be done once when the server starts and all the settings are stored in the
 * respective attributes.
 * @author Luka Stoehr
 */
public class Configuration {
    String scenarioConfigPath;
    String characterConfigPath;
    String matchConfigPatch;

    public ScenarioConfig scenarioConfig;
    public CharacterConfig characterConfig;
    public MatchConfig matchConfig;

    Gson gson;

    /**
     * Constructor for the Configuration class. It automatically calls the parseConfig() method.
     * @param scenarioConfigPath Path to the scenario configuration file
     * @param characterConfigPath Path to the character config file
     * @param matchConfigPatch Path to the match config file
     * @throws IOException
     * @author Luka Stoehr
     */
    public Configuration(String scenarioConfigPath, String characterConfigPath, String matchConfigPatch) throws IOException{
        this.scenarioConfigPath = scenarioConfigPath;
        this.characterConfigPath = characterConfigPath;
        this.matchConfigPatch = matchConfigPatch;

        this.gson = new Gson();

        parseConfig();
    }

    /**
     * Parses the three configuration files into their respective objects using GSON.
     * The path to each file is taken from the "path" attributes of this class, and the results
     * are stored in the other attributes of this class.
     * @author Luka Stoehr
     * @throws IOException
     */
    public void parseConfig() throws IOException{
        InputStreamReader characterReader = new InputStreamReader(new FileInputStream(this.characterConfigPath));
        this.characterConfig = gson.fromJson(characterReader, CharacterConfig.class);
        characterReader.close();

        InputStreamReader matchReader = new InputStreamReader(new FileInputStream(this.matchConfigPatch));
        this.matchConfig = gson.fromJson(matchReader, MatchConfig.class);
        matchReader.close();

        InputStreamReader scenarioReader = new InputStreamReader(new FileInputStream(this.scenarioConfigPath));
        this.scenarioConfig = gson.fromJson(scenarioReader, ScenarioConfig.class);
        scenarioReader.close();
    }
}
