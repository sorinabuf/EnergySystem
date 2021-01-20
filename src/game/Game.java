package game;

import consumer.ConsumersDB;

import distributor.DistributorsDB;

import producer.ProducersDB;

import strategies.EnergyChoiceStrategyFactory;

/**
 * The system of the simulation inherited by different stages of the game.
 */
public abstract class Game {
    protected ConsumersDB consumersDatabase; // database of all consumers
    protected ProducersDB producersDatabase; // database of all producers
    protected DistributorsDB distributorsDatabase; // database of all distributors
    protected EnergyChoiceStrategyFactory choiceStrategyFactory; // factory of strategies

    /**
     * Class constructor with four parameters.
     *
     * @param consumersDatabase     consumers' database
     * @param producersDatabase     producers' database
     * @param distributorsDatabase  distributors' database
     * @param choiceStrategyFactory factory of strategies
     */
    public Game(final ConsumersDB consumersDatabase, final ProducersDB producersDatabase,
                final DistributorsDB distributorsDatabase,
                final EnergyChoiceStrategyFactory choiceStrategyFactory) {
        this.consumersDatabase = consumersDatabase;
        this.producersDatabase = producersDatabase;
        this.distributorsDatabase = distributorsDatabase;
        this.choiceStrategyFactory = choiceStrategyFactory;
    }

    /**
     * Method that must be implemented by the children, following the updates of the game.
     */
    public abstract void playGame();
}
