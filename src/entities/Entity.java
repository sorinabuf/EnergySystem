package entities;

import org.json.simple.JSONObject;

/**
 * Main class to be inherited by consumers, distributors and producers.
 */
@SuppressWarnings("deprecation")
public class Entity {
    protected final long id;

    /**
     * Class constructor with one parameter.
     *
     * @param entity to be added, extracted from input data
     */
    public Entity(final JSONObject entity) {
        id = (long) entity.get("id");
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }
}
