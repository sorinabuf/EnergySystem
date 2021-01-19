package strategies.comparators;

import producer.Producer;

import java.util.Comparator;

/**
 * Comparator used for sorting the producers based on green strategy's criteria. Sorts the
 * producers by prioritizing the ones offering renewable energy, by the smallest price per KW
 * given, by the biggest amount of energy offered monthly and ultimately by ascending ids.
 */
public final class GreenEnergyChoiceSort implements Comparator<Producer> {

    @Override
    public int compare(final Producer producer1, final Producer producer2) {
        // compares the producers by renewable energy
        int result = Boolean.compare(producer1.getEnergyType().isRenewable(),
                producer2.getEnergyType().isRenewable());

        // in case of equality, compares the producers by price per KW
        if (result == 0) {
            result = Double.compare(producer1.getPriceKW(), producer2.getPriceKW());
            // in case of equality, compares the producers by energy provided
            if (result == 0) {
                result = Long.compare(producer1.getEnergyPerDistributor(),
                        producer2.getEnergyPerDistributor());
                // in case of equality, compares the producers by ids
                if (result == 0) {
                    return Long.compare(producer1.getId(), producer2.getId());
                }
                // descending order of energy provided
                result = -result;
                return result;
            }
            return result;
        }

        // prioritizes the renewable energy
        result = -result;
        return result;
    }
}
