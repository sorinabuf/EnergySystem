package fileio;

import entities.EntitiesFactory;
import entities.Entity;
import entities.EntityType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import producer.Producer;

import java.util.ArrayList;
import java.util.List;

public final class LoadProducers {
    // list of producers loaded from input file
    private final List<Producer> loadedProducers = new ArrayList<>();

    /**
     * Class constructor with two parameters.
     *
     * @param initialData data extracted from input file
     * @param factory     class responsible of creating entities
     */
    public LoadProducers(final JSONObject initialData, final EntitiesFactory factory) {
        // list of producers given through input data
        JSONArray producers = (JSONArray) initialData.get("producers");
        for (Object producer : producers) {
            Entity newProducer = factory.createEntity(EntityType.PRODUCER, (JSONObject) producer);
            loadedProducers.add((Producer) newProducer);
        }
    }

    public List<Producer> getLoadedProducers() {
        return loadedProducers;
    }
}
