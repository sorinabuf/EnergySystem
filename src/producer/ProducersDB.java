package producer;

import distributor.Distributor;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Observable, its observers are notified when new changes produce over the current month in game.
 */
public final class ProducersDB extends Observable {
    private final List<Producer> producers; // list of all producers

    /**
     * Class constructor with one parameter.
     *
     * @param producers to be added to the database list
     */
    public ProducersDB(final List<Producer> producers) {
        this.producers = producers;
    }

    /**
     * Updates the value of the energy supplied per distributor of the given producer and notifies
     * its observers.
     *
     * @param producer to be updated
     */
    public void updateProducers(final JSONObject producer) {
        long id = (long) producer.get("id");
        producers.get((int) id).energyPerDistributor = (long) producer.get("energyPerDistributor");
        // notifies observers
        setChanged();
        notifyObservers(id);
    }

    /**
     * Updates the lists of all distributors supplied in the game by adding the current list of
     * distributors.
     *
     * @param currentMonth current turn of the game
     */
    public void updateMonthlyDistributorsLists(final int currentMonth) {
        for (Producer producer : producers) {
            List<Distributor> currentDistributors = new ArrayList<>(producer.currentDistributors);
            producer.allDistributors.put(currentMonth, currentDistributors);
        }
    }

    public List<Producer> getProducers() {
        return producers;
    }
}
