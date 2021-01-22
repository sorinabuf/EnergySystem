package strategies;

import distributor.Distributor;

import producer.Producer;
import producer.ProducersDB;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class QuantityEnergyChoiceStrategy implements EnergyChoiceStrategy {
    private final Distributor distributor; // distributor for whom strategy is applied
    private final ProducersDB producersDatabase; // database of all producers

    /**
     * Class constructor with two parameters.
     *
     * @param producersDatabase list of all producers
     * @param distributor       for whom strategy is applied
     */
    public QuantityEnergyChoiceStrategy(final ProducersDB producersDatabase,
                                        final Distributor distributor) {
        this.distributor = distributor;
        this.producersDatabase = producersDatabase;
    }

    /**
     * Sorts the database of producers based on quantity energy choice strategy's criteria and
     * returns the list of suppliers of the given distributor according to his required energy.
     *
     * @return list of producers chosen as suppliers
     */
    @Override
    public List<Producer> getEnergyProducers() {
        // list of producers to be returned
        List<Producer> energyProducers = new ArrayList<>();
        List<Producer> sortedProducers = new ArrayList<>(producersDatabase.getProducers());

        // sorts the producers by the biggest amount of energy offered monthly and by ascending ids
        sortedProducers.sort(Comparator.comparing(Producer::getEnergyPerDistributor,
                Comparator.reverseOrder()).thenComparing(Producer::getId));

        long distributorEnergy = distributor.getEnergyNeededKW();
        // removes producers who reached the maximum number of supplied distributors for the
        // current month from the sorted list
        sortedProducers.removeIf(producer -> producer.getDistributors().size()
                == producer.getMaxDistributors());

        // choose the list of energy suppliers for the given distributor from the sorted list
        // according to the energy needed by extracting the first "n" producers until the
        // total energy value is reached
        while (distributorEnergy > 0) {
            Producer chosenProducer = sortedProducers.get(0);
            energyProducers.add(chosenProducer); // add producer to distributors' suppliers list
            chosenProducer.addDistributor(distributor); // add distributor to producer's list
            distributorEnergy -= chosenProducer.getEnergyPerDistributor();
            sortedProducers.remove(chosenProducer);
        }

        return energyProducers;
    }
}
