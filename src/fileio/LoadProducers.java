package fileio;

import entities.EntitiesFactory;
import entities.EntityType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import producer.Producer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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
        IntStream.range(0, producers.size()).mapToObj(producers::get)
                .map(producer -> factory.createEntity(EntityType.PRODUCER, (JSONObject) producer))
                .map(newProducer -> (Producer) newProducer).forEach(loadedProducers::add);
    }

    public List<Producer> getLoadedProducers() {
        return loadedProducers;
    }
}
