package parameter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Tests for the class Configuration, which reads the configuration files.
 * @author Luka Stoehr
 */
public class ConfigurationTest {

    /**
     * Parses example config files and checks if everything is correct. As examples files from the jsonstarterkit
     * are used.
     * @author Luka Stoehr
     */
    @Test
    public void configurationTest(){
        try {
            //Create config files for this test
            String charPath = "characterConfigTest_"+System.currentTimeMillis()+".tmp";
            String scenPath = "scenarioConfigTest_"+System.currentTimeMillis()+".tmp";
            String matPath = "matchConfigTest_"+System.currentTimeMillis()+".tmp";

            FileOutputStream fopsChar = new FileOutputStream(charPath);
            String chara = "{  \"characters\": [    {      \"name\": \"Rocket Raccoon\",      \"HP\": 100,      \"MP\": 2,      \"AP\": 2,      \"meleeDamage\": 10,      \"rangeCombatDamage\": 30,      \"rangeCombatReach\": 5    },    {      \"name\": \"Quicksilver\",      \"HP\": 100,      \"MP\": 6,      \"AP\": 1,      \"meleeDamage\": 10,      \"rangeCombatDamage\": 10,      \"rangeCombatReach\": 3    },    {      \"name\": \"Hulk\",      \"HP\": 100,      \"MP\": 2,      \"AP\": 2,      \"meleeDamage\": 10,      \"rangeCombatDamage\": 30,      \"rangeCombatReach\": 5    },    {      \"name\": \"Black Widow\",      \"HP\": 100,      \"MP\": 6,      \"AP\": 1,      \"meleeDamage\": 10,      \"rangeCombatDamage\": 10,      \"rangeCombatReach\": 3    },    {      \"name\": \"Hawkeye\",      \"HP\": 100,      \"MP\": 6,      \"AP\": 1,      \"meleeDamage\": 10,      \"rangeCombatDamage\": 10,      \"rangeCombatReach\": 3    },    {      \"name\": \"Captain America\",      \"HP\": 100,      \"MP\": 2,      \"AP\": 2,      \"meleeDamage\": 10,      \"rangeCombatDamage\": 30,      \"rangeCombatReach\": 5    },    {      \"name\": \"Spiderman\",      \"HP\": 100,      \"MP\": 6,      \"AP\": 1,      \"meleeDamage\": 10,      \"rangeCombatDamage\": 10,      \"rangeCombatReach\": 3    },    {      \"name\": \"Dr. Strange\",      \"HP\": 100,      \"MP\": 2,      \"AP\": 2,      \"meleeDamage\": 10,      \"rangeCombatDamage\": 30,      \"rangeCombatReach\": 5    },    {      \"name\": \"Iron Man\",      \"HP\": 100,      \"MP\": 6,      \"AP\": 1,      \"meleeDamage\": 10,      \"rangeCombatDamage\": 10,      \"rangeCombatReach\": 3    },    {      \"name\": \"Black Panther\",      \"HP\": 100,      \"MP\": 6,      \"AP\": 1,      \"meleeDamage\": 10,      \"rangeCombatDamage\": 10,      \"rangeCombatReach\": 3    },    {      \"name\": \"Thor\",      \"HP\": 100,      \"MP\": 2,      \"AP\": 2,      \"meleeDamage\": 10,      \"rangeCombatDamage\": 30,      \"rangeCombatReach\": 5    },    {      \"name\": \"Captain Marvel\",      \"HP\": 100,      \"MP\": 6,      \"AP\": 1,      \"meleeDamage\": 10,      \"rangeCombatDamage\": 10,      \"rangeCombatReach\": 3    },    {      \"name\": \"Groot\",      \"HP\": 100,      \"MP\": 2,      \"AP\": 2,      \"meleeDamage\": 10,      \"rangeCombatDamage\": 30,      \"rangeCombatReach\": 5    },    {      \"name\": \"Starlord\",      \"HP\": 100,      \"MP\": 6,      \"AP\": 1,      \"meleeDamage\": 10,      \"rangeCombatDamage\": 10,      \"rangeCombatReach\": 3    },    {      \"name\": \"Gamora\",      \"HP\": 100,      \"MP\": 6,      \"AP\": 1,      \"meleeDamage\": 10,      \"rangeCombatDamage\": 10,      \"rangeCombatReach\": 3    },    {      \"name\": \"Ant Man\",      \"HP\": 100,      \"MP\": 2,      \"AP\": 2,      \"meleeDamage\": 10,      \"rangeCombatDamage\": 30,      \"rangeCombatReach\": 5    },    {      \"name\": \"Vision\",      \"HP\": 100,      \"MP\": 6,      \"AP\": 1,      \"meleeDamage\": 10,      \"rangeCombatDamage\": 10,      \"rangeCombatReach\": 3    },    {      \"name\": \"Deadpool\",      \"HP\": 100,      \"MP\": 2,      \"AP\": 2,      \"meleeDamage\": 10,      \"rangeCombatDamage\": 30,      \"rangeCombatReach\": 5    },    {      \"name\": \"Loki\",      \"HP\": 100,      \"MP\": 6,      \"AP\": 1,      \"meleeDamage\": 10,      \"rangeCombatDamage\": 10,      \"rangeCombatReach\": 3    },    {      \"name\": \"Silver Surfer\",      \"HP\": 100,      \"MP\": 6,      \"AP\": 1,      \"meleeDamage\": 10,      \"rangeCombatDamage\": 10,      \"rangeCombatReach\": 3    },    {      \"name\": \"Mantis\",      \"HP\": 100,      \"MP\": 2,      \"AP\": 2,      \"meleeDamage\": 10,      \"rangeCombatDamage\": 30,      \"rangeCombatReach\": 5    },    {      \"name\": \"Ghost rider \",      \"HP\": 100,      \"MP\": 6,      \"AP\": 1,      \"meleeDamage\": 10,      \"rangeCombatDamage\": 10,      \"rangeCombatReach\": 3    },    {      \"name\": \"Jesica Jones \",      \"HP\": 100,      \"MP\": 2,      \"AP\": 2,      \"meleeDamage\": 10,      \"rangeCombatDamage\": 30,      \"rangeCombatReach\": 5    },    {      \"name\": \"Scarlet witch\",      \"HP\": 100,      \"MP\": 6,      \"AP\": 1,      \"meleeDamage\": 10,      \"rangeCombatDamage\": 10,      \"rangeCombatReach\": 3    }  ]}";
            fopsChar.write(chara.getBytes());
            fopsChar.close();

            FileOutputStream fopsScen = new FileOutputStream(scenPath);
            String scen = "{\"scenario\":[[\"GRASS\",\"GRASS\", \"GRASS\", \"GRASS\", \"GRASS\", \"GRASS\", \"GRASS\"],[\"GRASS\",\"GRASS\", \"GRASS\", \"ROCK\", \"GRASS\", \"GRASS\",\"GRASS\"],[\"GRASS\",\"GRASS\", \"GRASS\", \"ROCK\", \"GRASS\", \"GRASS\", \"GRASS\"],[\"GRASS\",\"GRASS\", \"GRASS\", \"ROCK\", \"GRASS\", \"GRASS\", \"GRASS\"],[\"GRASS\",\"GRASS\", \"ROCK\", \"ROCK\", \"ROCK\", \"GRASS\", \"GRASS\"],[\"GRASS\",\"ROCK\", \"ROCK\", \"ROCK\", \"ROCK\", \"ROCK\", \"GRASS\"]],\"author\": \"jakobmh\",\"name\": \"asgard\"}";
            fopsScen.write(scen.getBytes());
            fopsScen.close();

            FileOutputStream fopsMat = new FileOutputStream(matPath);
            String mat = "{  \"maxRounds\": 30,  \"maxRoundTime\": 300,  \"maxGameTime\": 1800,  \"maxAnimationTime\": 50,  \"spaceStoneCD\": 2,  \"mindStoneCD\": 1,  \"realityStoneCD\": 3,  \"powerStoneCD\": 1,  \"timeStoneCD\": 5,  \"soulStoneCD\": 5,  \"mindStoneDMG\": 12,  \"maxPauseTime\": 60000,  \"maxResponseTime\": 3000}";
            fopsMat.write(mat.getBytes());
            fopsMat.close();

            // Parse the created config files
            Configuration config = new Configuration(scenPath,charPath,matPath);
            config.parseConfig();

            //Delete the files again, they were only used for this test
            File charFile = new File(charPath);
            charFile.delete();
            File scenFile = new File(scenPath);
            scenFile.delete();
            File matFile = new File(matPath);
            matFile.delete();

            //Check scenario config
            assertEquals("asgard", config.scenarioConfig.name);
            assertEquals("jakobmh", config.scenarioConfig.author);
            GrassRockEnum g = GrassRockEnum.GRASS;
            GrassRockEnum r = GrassRockEnum.ROCK;
            GrassRockEnum[][] array = {
                    {g,g,g,g,g,g,g},
                    {g,g,g,r,g,g,g},
                    {g,g,g,r,g,g,g},
                    {g,g,g,r,g,g,g},
                    {g,g,r,r,r,g,g},
                    {g,r,r,r,r,r,g}
            };
            for(int i = 0; i < array.length; i++){
                for(int j = 0; j < array[0].length; j++) {
                    assertEquals(array[i][j], config.scenarioConfig.scenario[i][j]);
                }
            }

            //Check match config
            MatchConfig mc = config.matchConfig;
            assertEquals(30, mc.maxRounds);
            assertEquals(300, mc.maxRoundTime);
            assertEquals(1800, mc.maxGameTime);
            assertEquals(50, mc.maxAnimationTime);
            assertEquals(2, mc.spaceStoneCD);
            assertEquals(1, mc.mindStoneCD);
            assertEquals(3, mc.realityStoneCD);
            assertEquals(1, mc.powerStoneCD);
            assertEquals(5, mc.timeStoneCD);
            assertEquals(5, mc.soulStoneCD);
            assertEquals(12, mc.mindStoneDMG);
            assertEquals(60000, mc.maxPauseTime);
            assertEquals(3000, mc.maxResponseTime);

            //Check character config
            CharacterConfig cc = config.characterConfig;
            assertEquals(24, cc.characters.length);
            for(int i = 0; i<cc.characters.length; i++){
                assertNotNull(cc.characters[i]);
            }
            //Check only three characters
            assertEquals("Rocket Raccoon", cc.characters[0].name);
            assertEquals(100, cc.characters[0].HP);
            assertEquals(2, cc.characters[0].MP);
            assertEquals(2, cc.characters[0].AP);
            assertEquals(10, cc.characters[0].meleeDamage);
            assertEquals(30, cc.characters[0].rangeCombatDamage);
            assertEquals(5, cc.characters[0].rangeCombatReach);

            assertEquals("Scarlet witch", cc.characters[23].name);
            assertEquals(100, cc.characters[23].HP);
            assertEquals(6, cc.characters[23].MP);
            assertEquals(1, cc.characters[23].AP);
            assertEquals(10, cc.characters[23].meleeDamage);
            assertEquals(10, cc.characters[23].rangeCombatDamage);
            assertEquals(3, cc.characters[23].rangeCombatReach);

            assertEquals("Hawkeye", cc.characters[4].name);
            assertEquals(100, cc.characters[4].HP);
            assertEquals(6, cc.characters[4].MP);
            assertEquals(1, cc.characters[4].AP);
            assertEquals(10, cc.characters[4].meleeDamage);
            assertEquals(10, cc.characters[4].rangeCombatDamage);
            assertEquals(3, cc.characters[4].rangeCombatReach);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
