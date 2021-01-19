package strategies;

import producer.Producer;

import java.util.List;

/**
 * Interface implemented by all energy choice strategies of the game.
 */
public interface EnergyChoiceStrategy {
    /**
     * Sorts the database of producers based on strategy's criteria and returns the list of
     * suppliers of the given distributor according to his required energy.
     *
     * @return list of producers chosen as suppliers
     */
    List<Producer> getEnergyProducers();
}
