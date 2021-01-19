package fileio;

import distributor.Distributor;

import entities.Entity;
import entities.EntityType;
import entities.EntitiesFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public final class LoadDistributors {
    // list of distributors loaded from input file
    private final List<Distributor> loadedDistributors = new ArrayList<>();

    /**
     * Class constructor with two parameters.
     *
     * @param initialData data extracted from input file
     * @param factory     class responsible with creating entities
     */
    public LoadDistributors(final JSONObject initialData, final EntitiesFactory factory) {
        // list of distributors given through input data
        JSONArray distributors = (JSONArray) initialData.get("distributors");
        for (Object distributor : distributors) {
            Entity newDistributor = factory.createEntity(EntityType.DISTRIBUTOR,
                    (JSONObject) distributor);
            loadedDistributors.add((Distributor) newDistributor);
        }
    }

    public List<Distributor> getLoadedDistributors() {
        return loadedDistributors;
    }
}
