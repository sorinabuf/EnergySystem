package game;

import consumer.ConsumersDB;
import distributor.DistributorsDB;
import producer.ProducersDB;
import strategies.EnergyChoiceStrategyFactory;

public final class InitialMonth extends Game {

    /**
     * Class constructor with four parameters.
     *
     * @param consumersDatabase     consumers' database
     * @param producersDatabase     producers' database
     * @param distributorsDatabase  distributors' database
     * @param choiceStrategyFactory factory of strategies
     */
    public InitialMonth(final ConsumersDB consumersDatabase, final ProducersDB producersDatabase,
                        final DistributorsDB distributorsDatabase,
                        final EnergyChoiceStrategyFactory choiceStrategyFactory) {
        // super constructor call
        super(consumersDatabase, producersDatabase, distributorsDatabase, choiceStrategyFactory);
    }

    /**
     * Method that effectuates all the changes and updates characteristic to the first month of
     * the game to both consumers and distributors.
     */
    @Override
    public void playGame() {
        // sets initial energy sources and monthly rates for all distributors
        distributorsDatabase.initiateEnergySources(choiceStrategyFactory, producersDatabase);
        distributorsDatabase.setInitialMonthlyRate();

        // sets initial data fields for both consumers and distributors
        consumersDatabase.setInitialMonth(distributorsDatabase);
        distributorsDatabase.setInitialMonth(consumersDatabase);

        // sets updates of the first month of the game (month 0)
        consumersDatabase.updateConsumers(distributorsDatabase);
        distributorsDatabase.updateDistributors();
    }
}
