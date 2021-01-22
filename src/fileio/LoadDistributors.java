package fileio;

import distributor.Distributor;

import entities.EntityType;
import entities.EntitiesFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@SuppressWarnings("deprecation")
public final class LoadDistributors {
    // list of distributors loaded from input file
    private final List<Distributor> loadedDistributors = new ArrayList<>();

    /**
     * Class constructor with two parameters.
     *
     * @param initialData data extracted from input file
     * @param factory     class responsible of creating entities
     */
    public LoadDistributors(final JSONObject initialData, final EntitiesFactory factory) {
        // list of distributors given through input data
        JSONArray distributors = (JSONArray) initialData.get("distributors");
        IntStream.range(0, distributors.size()).mapToObj(distributors::get)
                .map(distributor -> factory.createEntity(EntityType.DISTRIBUTOR,
                (JSONObject) distributor))
                .map(newDistributor -> (Distributor) newDistributor)
                .forEach(loadedDistributors::add);
    }

    public List<Distributor> getLoadedDistributors() {
        return loadedDistributors;
    }
}
