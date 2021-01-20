import java.io.FileReader;

import consumer.ConsumersDB;

import distributor.DistributorsDB;

import entities.EntitiesFactory;

import fileio.LoadConsumers;
import fileio.LoadDistributors;
import fileio.LoadProducers;
import fileio.Writer;

import game.Game;
import game.InitialMonth;
import game.TurnMonth;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import producer.ProducersDB;

import strategies.EnergyChoiceStrategyFactory;

/**
 * Entry point of the simulation.
 */
@SuppressWarnings("deprecation")
public final class Main {

    private Main() {
    }

    /**
     * Main function which reads the input file and starts simulation
     *
     * @param args input and output files
     * @throws Exception might error when reading/writing/opening files, parsing JSON
     */
    public static void main(final String[] args) throws Exception {
        JSONParser jsonParser = new JSONParser(); // parser object used for reading
        // json object used for parsing the input file found in args[0]
        JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(args[0]));

        // number of turns extracted from input file
        long numberOfTurns = (long) jsonObject.get("numberOfTurns");
        // initial data set extracted from input file
        JSONObject initialData = (JSONObject) jsonObject.get("initialData");
        // monthly updates set extracted from input file
        JSONArray monthlyUpdates = (JSONArray) jsonObject.get("monthlyUpdates");

        // entities factory instance
        EntitiesFactory entitiesFactory = EntitiesFactory.getInstance();

        // array of producers loaded from initial data set
        LoadProducers loadedProducers = new LoadProducers(initialData, entitiesFactory);
        // array of consumers loaded from initial data set
        LoadConsumers loadedConsumers = new LoadConsumers(initialData, entitiesFactory);
        // array of distributors loaded from initial data set
        LoadDistributors loadedDistributors = new LoadDistributors(initialData, entitiesFactory);

        // consumers database created with the consumers given through input file
        ConsumersDB consumersDatabase = new ConsumersDB(loadedConsumers.getLoadedConsumers());
        // distributors database created with the distributors given through input file
        DistributorsDB distributorsDatabase =
                new DistributorsDB(loadedDistributors.getLoadedDistributors());
        // producers database created with the producers given through input file
        ProducersDB producersDatabase = new ProducersDB(loadedProducers.getLoadedProducers());

        // strategies factory instance
        EnergyChoiceStrategyFactory choiceStrategyFactory =
                EnergyChoiceStrategyFactory.getInstance();

        // initial month object of the game
        Game initialMonthGame = new InitialMonth(consumersDatabase, producersDatabase,
                distributorsDatabase, choiceStrategyFactory);
        initialMonthGame.playGame(); // updates the entities based on first month rules

        // for each turn of the game, a new update is extracted from the input file and
        // accordingly are added new consumers and changed the game's data
        for (int i = 0; i < numberOfTurns; ++i) {
            JSONObject update = (JSONObject) monthlyUpdates.get(i);
            // current month object of the game
            Game turnMonth = new TurnMonth(consumersDatabase, producersDatabase,
                    distributorsDatabase, update, choiceStrategyFactory, entitiesFactory, i);
            turnMonth.playGame(); // updates the entities based on the rules of a new turn
        }

        // the writer object used for displaying the results in the output file found in args[1]
        Writer outfile = new Writer(args[1]);
        outfile.writeData(consumersDatabase, distributorsDatabase, producersDatabase,
                numberOfTurns);
    }
}
