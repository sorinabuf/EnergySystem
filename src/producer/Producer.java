package producer;

import distributor.Distributor;

import entities.EnergyType;
import entities.Entity;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Producer extends Entity {
    protected long maxDistributors; // maximum number of distributors supplied by the producer
    protected EnergyType energyType; // type of energy supplied
    protected long energyPerDistributor; // energy supplied to each distributor
    protected double priceKW; // price per KW
    // list of distributors supplied  in the current month
    protected List<Distributor> currentDistributors;
    // lists of all distributors supplied over all turns of the game
    protected Map<Integer, List<Distributor>> allDistributors;

    /**
     * Class constructor with one parameter.
     *
     * @param producer to be added, extracted from input data
     */
    public Producer(final JSONObject producer) {
        super(producer); // super constructor call
        this.energyType = EnergyType.valueOf((String) producer.get("energyType"));
        this.energyPerDistributor = (long) producer.get("energyPerDistributor");
        this.priceKW = (double) producer.get("priceKW");
        this.maxDistributors = (long) producer.get("maxDistributors");
        currentDistributors = new ArrayList<>();
        allDistributors = new HashMap<>();
    }

    /**
     * Adds a new distributor to the list of distributors supplied by the producer during the
     * current month of the game.
     *
     * @param distributor to be added
     */
    public void addDistributor(final Distributor distributor) {
        currentDistributors.add(distributor);
    }

    public long getMaxDistributors() {
        return maxDistributors;
    }

    public EnergyType getEnergyType() {
        return energyType;
    }

    public long getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    public double getPriceKW() {
        return priceKW;
    }

    public List<Distributor> getDistributors() {
        return currentDistributors;
    }

    public Map<Integer, List<Distributor>> getAllDistributors() {
        return allDistributors;
    }
}
