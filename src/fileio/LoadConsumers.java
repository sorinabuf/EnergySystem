package fileio;

import consumer.Consumer;

import entities.Entity;
import entities.EntityType;
import entities.EntitiesFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public final class LoadConsumers {
    // list of consumers loaded from input file
    private final List<Consumer> loadedConsumers = new ArrayList<>();

    /**
     * Class constructor with two parameters.
     *
     * @param initialData data extracted from input file
     * @param factory class responsible with creating entities
     */
    public LoadConsumers(final JSONObject initialData, final EntitiesFactory factory) {
        // list of consumers given through input data
        JSONArray consumers = (JSONArray) initialData.get("consumers");
        for (Object consumer : consumers) {
            Entity newConsumer = factory.createEntity(EntityType.CONSUMER, (JSONObject) consumer);
            loadedConsumers.add((Consumer) newConsumer);
        }
    }

    public List<Consumer> getLoadedConsumers() {
        return loadedConsumers;
    }
}
