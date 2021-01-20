package game;

import consumer.Consumer;
import consumer.ConsumersDB;

import distributor.DistributorsDB;

import entities.EntitiesFactory;
import entities.Entity;
import entities.EntityType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import producer.ProducersDB;

import strategies.EnergyChoiceStrategyFactory;

public final class TurnMonth extends Game {
    private final EntitiesFactory entitiesFactory; // factory of entities
    private final JSONObject update; // monthly update
    private final int currentMonth; // current month of the game

    /**
     * Class constructor with multiple parameters
     *
     * @param consumersDatabase     consumers' database
     * @param producersDatabase     producers' database
     * @param distributorsDatabase  distributors' database
     * @param update                monthly update
     * @param choiceStrategyFactory factory of strategies
     * @param entitiesFactory       factory of entities
     * @param currentMonth          current month of the simulation
     */
    public TurnMonth(final ConsumersDB consumersDatabase, final ProducersDB producersDatabase,
                     final DistributorsDB distributorsDatabase, final JSONObject update,
                     final EnergyChoiceStrategyFactory choiceStrategyFactory,
                     final EntitiesFactory entitiesFactory, final int currentMonth) {
        // super constructor call
        super(consumersDatabase, producersDatabase, distributorsDatabase, choiceStrategyFactory);
        this.entitiesFactory = entitiesFactory;
        this.currentMonth = currentMonth;
        this.update = update;
    }

    /**
     * Reads the updates from the input file and sets the modified fields to the entities through
     * specific methods. Private method designed for the current class.
     */
    private void readUpdates() {
        if (((JSONArray) update.get("newConsumers")).size() != 0) {
            for (Object consumer : ((JSONArray) update.get("newConsumers"))) {
                Entity newConsumer = entitiesFactory.createEntity(EntityType.CONSUMER,
                        (JSONObject) consumer);
                consumersDatabase.getConsumers().add((Consumer) newConsumer);
            }
        }
        if (((JSONArray) update.get("distributorChanges")).size() != 0) {
            for (Object distributor : ((JSONArray) update.get("distributorChanges"))) {
                long id = (long) ((JSONObject) distributor).get("id");
                distributorsDatabase.getDistributors().get((int) id)
                        .updateDistributor((JSONObject) distributor);
            }
        }
        if (((JSONArray) update.get("producerChanges")).size() != 0) {
            for (Object producer : ((JSONArray) update.get("producerChanges"))) {
                producersDatabase.updateProducers((JSONObject) producer);
            }
        }
    }

    /**
     * Updates the fields of all entities based on the characteristics of the current month of
     * the game. Private method designed for the current class.
     */
    private void updateEntities() {
        // sets updates for the current month of the game for all the entities
        distributorsDatabase.calculateMonthlyRate();
        distributorsDatabase.removeInvalidContracts();
        consumersDatabase.updateConsumers(distributorsDatabase);
        distributorsDatabase.updateDistributors();
        distributorsDatabase.updateEnergySources(choiceStrategyFactory, producersDatabase);
        producersDatabase.updateMonthlyDistributorsLists(currentMonth);
    }

    /**
     * Method that effectuates all the changes and updates characteristic to the current month of
     * the game to all entities.
     */
    @Override
    public void playGame() {
        readUpdates();
        updateEntities();
    }
}
