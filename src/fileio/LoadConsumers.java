package fileio;

import consumer.Consumer;

import entities.EntityType;
import entities.EntitiesFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@SuppressWarnings("deprecation")
public final class LoadConsumers {
    // list of consumers loaded from input file
    private final List<Consumer> loadedConsumers = new ArrayList<>();

    /**
     * Class constructor with two parameters.
     *
     * @param initialData data extracted from input file
     * @param factory class responsible of creating entities
     */
    public LoadConsumers(final JSONObject initialData, final EntitiesFactory factory) {
        // list of consumers given through input data
        JSONArray consumers = (JSONArray) initialData.get("consumers");
        IntStream.range(0, consumers.size()).mapToObj(consumers::get)
                .map(consumer -> factory.createEntity(EntityType.CONSUMER, (JSONObject) consumer))
                .map(newConsumer -> (Consumer) newConsumer).forEach(loadedConsumers::add);
    }

    public List<Consumer> getLoadedConsumers() {
        return loadedConsumers;
    }
}
