package logic;

import org.java_websocket.server.WebSocketServer;
import communication.NetworkHandler;
import parameter.Configuration;

import java.io.*;

public class Main {

    //  -m matchconfig_1.json -c marvelheros.json -s asgard.json
    // scenario: asgard.json
    // character: marvelheros.json
    // match: matchconfig_1.json
    static String scenarioConfigPath = "";
    static String characterConfigPath = "";
    static String matchConfigPath = "";

    static int port = 1218;


    /**
     * This methode starts the server. It needs at least the path to the match config, to the scenario config and to the
     * character config (e.g. -m matchconfig_1.json -c marvelheros.json -s asgard.json)
     *
     * @param args This Array contains the arguments String splitted at the whitespaces
     */
    public static void main(String[] args) {

        Configuration configuration;
        compute(args);

        //Reads config files
        try {
            configuration = new Configuration(scenarioConfigPath, characterConfigPath, matchConfigPath);

            /**
             * Source of next block: Gomoku template
             */
            // create WebSocketServer
            final WebSocketServer networkHandler = new NetworkHandler(configuration, port);
            // see: https://github.com/TooTallNate/Java-WebSocket/wiki/Enable-SO_REUSEADDR
            networkHandler.setReuseAddr(true);
            // see: https://github.com/TooTallNate/Java-WebSocket/wiki/Enable-TCP_NODELAY
            networkHandler.setTcpNoDelay(true);
            // start the WebSocketServer
            networkHandler.start();
            // create ShutdownHook to catch CTRL+C and shutdown networkHandler peacefully
            // see: https://docs.oracle.com/javase/8/docs/technotes/guides/lang/hook-design.html
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        System.out.println("ShutdownHook executed.");
                        Thread.sleep(500);
                        System.out.println("Application shutting down.");
                        // shutdown networkHandler
                        networkHandler.stop();
                    } catch (InterruptedException ie) {
                        System.out.printf("InterruptedException: %s", ie);
                        Thread.currentThread().interrupt();
                    } catch (IOException ioe) {
                        System.out.printf("IOException: %s", ioe);
                    }
                }
            });
        }catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * This methode computes the args[] the main methode receives when starting the server. Those arguments
     * are necessary to start the server successfully. The server needs at least the paths to all 3 config
     * files.
     * @param inputArray This Array contains the arguments String splitted at the whitespaces
     */
    public static void compute(String[] inputArray){


        for(int i = 0; i < inputArray.length; i++){

            switch(inputArray[i]){
                case "--help":
                case "-h":
                    receiveHelp();
                    break;
                case "--log-level":
                case "-l":
                    if(inputArray[i+1] != null && inputArray[i+1].matches("\\d+")){
                        setLogLevel(Integer.parseInt(inputArray[i+1]));
                        i++;
                    }
                    break;
                case "-- verbose":
                case "-v":
                    verboseLogLevel();
                    break;
                case "--port":
                case "-p":
                    if(inputArray[i+1] != null && inputArray[i+1].matches("\\d+")){
                        sendServerPort(Integer.parseInt(inputArray[i+1]));
                        i++;
                    }
                    break;
                case "--conf-match":
                case "-m":
                    if(inputArray[i+1] != null) {
                        File fileMatch = new File(inputArray[i + 1]);
                        if (fileMatch.exists()) {
                            pathMatchFile(inputArray[i+1]);
                            i++;
                        }
                    }
                    break;
                case "--conf-chars":
                case "-c":
                    if(inputArray[i+1] != null) {
                        File fileChars = new File(inputArray[i + 1]);
                        if (fileChars.exists()) {
                            pathCharsFile(inputArray[i+1]);
                            i++;
                        }
                    }
                    break;
                case "--conf-scenario":
                case "-s":
                    if(inputArray[i+1] != null) {
                        File fileScenario = new File(inputArray[i + 1]);
                        if (fileScenario.exists()) {
                            pathScenarioFile(inputArray[i+1]);
                            i++;
                        }
                    }
                    break;
                case "--check-conf":
                case "-C":
                    checkConfigFiles();
                    break;
                case "--replay":
                case "-r":
                    if(inputArray[i+1] != null)
                    pathReplay(inputArray[i+1]);
                    break;
                default:
                    System.out.println("Wrong input. If you need help, please type in \"--help\" or \"-h\"");
            }

        }


        if(scenarioConfigPath.equals("") || matchConfigPath.equals("") || characterConfigPath.equals("")){
            System.out.println("There were paths missing. Please try again with 3 paths to each config file.");
            System.exit(0);
        }

    }

    /**
     * This methode prints out every action and its commands a user can do with the command line
     *
     * @author Sarah Engele
     */
    public static void receiveHelp(){

        System.out.println("Help: \r\n\r\n To start the server, these arguments are mandatory: \r\n\r\n" +
                "--conf-match file or -m file\r\n" +
                "--conf-chars file or -c file\r\n" +
                "--conf-scenario file or -s file\r\n\r\n" +
                "The following arguments can be used with this server:\r\n\r\n" +
                "--help or -h: This argument prints out all valid parameters including their tasks. With using this, " +
                "the server stops running.\r\n\r\n" +
                "--log-level n or -l n: This argument sets the log level which means how much information will be logged" +
                " by the server. If n is 0, no information is logged. If n is 1, errors are logged.\r\n\r\n" +
                "--verbose or -v: This arguments sets the log level on the highest log level that is possible. It " +
                "overwrites all other log level parameters.\r\n\r\n" +
                "--port n or -p n: This argument sets the server port if the user does not want to use the standard " +
                "port which is 1218.\r\n\r\n" +
                "--conf-match file or -m file: This argument sets the path to the match config file. This argument" +
                "is mandatory.\r\n\r\n" +
                "--conf-chars file or -c file: This argument sets the path to the character config file. This " +
                "argument is mandatory.\r\n\r\n" +
                "--conf-scenario file or -s file: This argument sets the path to the scenario config file. This argument" +
                "is mandatory.\r\n\r\n" +
                "--check-conf or -C: This argument checks the config files and stops the server.\r\n\r\n" +
                "--replay dir or -r dir: This argument sets the path to the replay directory.\r\n\r\n");
        System.exit(0);
    }

    /**
     * This methode sets how much (different) information is logged (0: nothing, 1: errors, ...)
     *
     * @author Sarah Engele
     * @param n how much information is supposed to be locked
     */
    public static void setLogLevel(int n){
        System.out.println("log-level is not supported by this server.");

    }

    /**
     * This method sets the log level to the highest level that is possible
     *
     * @author Sarah Engele
     */
    public static void verboseLogLevel(){
        System.out.println("verbose is not supported by this server.");
    }

    /**
     * This methode sets the port the server uses if its not the standard port which is 1218
     *
     * @author Sarah Engele
     *
     * @param networkHandlerPort the port of the NetworkHandler
     */
    public static void sendServerPort(int networkHandlerPort){
        port = networkHandlerPort;
    }

    /**
     * This methode sets the path to the match config file
     *
     * @author Sarah Engele
     */
    private static void pathMatchFile(String path) {
        matchConfigPath = path;
    }

    /**
     * This methode sets the path to the character config file
     *
     * @author Sarah Engele
     */
    private static void pathCharsFile(String path) {
        characterConfigPath = path;
    }

    /**
     * This methode sets the path to the scenario config file
     *
     * @author Sarah Engele
     */
    private static void pathScenarioFile(String path) {
        scenarioConfigPath = path;
    }

    /**
     * This methode checks all config files and then stops the Server
     *
     * @author Sarah Engele
     */
    private static void checkConfigFiles() {
        System.out.println("check-conf is not supported by this server.");
        System.exit(0);
    }

    /**
     * this method sets the path to the replay file
     *
     * @author Sarah Engele
     */
    private static void pathReplay(String path) {
        System.out.println("replay is not supported by this server.");
    }

}
