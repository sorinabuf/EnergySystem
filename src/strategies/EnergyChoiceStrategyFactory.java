package strategies;

import distributor.Distributor;

import producer.ProducersDB;

/**
 * Factory class designed using Singleton pattern that creates all the instances of the project
 * responsible for strategies creation.
 */
public final class EnergyChoiceStrategyFactory {
    private static EnergyChoiceStrategyFactory factory = null; // initially null private instance

    /**
     * Returns a new factory instance.
     *
     * @return the instance will be created at first call of getInstance()
     */
    public static EnergyChoiceStrategyFactory getInstance() {
        if (factory == null) {
            factory = new EnergyChoiceStrategyFactory();
        }
        return factory;
    }

    /**
     * Private class constructor without parameters particular to Singleton pattern.
     */
    private EnergyChoiceStrategyFactory() {

    }

    /**
     * Private method particular to the factory class that returns a green energy choice strategy
     * object.
     *
     * @param producersDatabase database of all producers
     * @param distributor       for whom strategy is applied
     * @return a new green strategy
     */
    private EnergyChoiceStrategy createGreenEnergyChoiceStrategy(
            final ProducersDB producersDatabase, final Distributor distributor) {
        return new GreenEnergyChoiceStrategy(producersDatabase, distributor);
    }

    /**
     * Private method particular to the factory class that returns a price energy choice strategy
     * object.
     *
     * @param producersDatabase database of all producers
     * @param distributor       for whom strategy is applied
     * @return a new price strategy
     */
    private EnergyChoiceStrategy createPriceEnergyChoiceStrategy(
            final ProducersDB producersDatabase, final Distributor distributor) {
        return new PriceEnergyChoiceStrategy(producersDatabase, distributor);
    }

    /**
     * Private method particular to the factory class that returns an quantity energy choice
     * strategy object.
     *
     * @param producersDatabase database of all producers
     * @param distributor       for whom strategy is applied
     * @return a new quantity strategy
     */
    private EnergyChoiceStrategy createQuantityEnergyChoiceStrategy(
            final ProducersDB producersDatabase, final Distributor distributor) {
        return new QuantityEnergyChoiceStrategy(producersDatabase, distributor);
    }

    /**
     * Create method that returns a strategy specific to the type given as parameter.
     *
     * @param type              of the new strategy object
     * @param producersDatabase database of all producers
     * @param distributor       for whom strategy is applied
     * @return a new strategy
     */
    public EnergyChoiceStrategy createStrategy(final EnergyChoiceStrategyType type,
                                               final ProducersDB producersDatabase,
                                               final Distributor distributor) {
        return switch (type) {
            case GREEN -> createGreenEnergyChoiceStrategy(producersDatabase, distributor);
            case PRICE -> createPriceEnergyChoiceStrategy(producersDatabase, distributor);
            case QUANTITY -> createQuantityEnergyChoiceStrategy(producersDatabase, distributor);
        };
    }
}
