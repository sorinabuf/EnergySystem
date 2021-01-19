package entities;

import consumer.Consumer;

import distributor.Distributor;

import org.json.simple.JSONObject;

/**
 * Factory class designed using Singleton pattern that creates all the instances of the project.
 */
@SuppressWarnings("deprecation")
public final class EntitiesFactory {
    private static EntitiesFactory factory = null; // initially null private instance

    /**
     * Return a new factory instance.
     *
     * @return the instance will be created at first call of getInstance()
     */
    public static EntitiesFactory getInstance() {
        if (factory == null) {
            factory = new EntitiesFactory();
        }
        return factory;
    }

    /**
     * Private class constructor without parameters particular to Singleton pattern.
     */
    private EntitiesFactory() {
    }

    /**
     * Private method particular to the factory class that returns a consumer object.
     *
     * @param consumer to be created given through input data
     * @return a new consumer
     */
    private Entity createConsumer(final JSONObject consumer) {
        return new Consumer(consumer);
    }

    /**
     * Private method particular to the factory class that returns a distributor object.
     *
     * @param distributor to be created given through input data
     * @return a new distributor
     */
    private Entity createDistributor(final JSONObject distributor) {
        return new Distributor(distributor);
    }

    /**
     * Create method that return an entity specific to the type given as parameter.
     *
     * @param type   of the new entity object
     * @param entity to be created given through input data
     * @return a new entity
     */
    public Entity createEntity(final EntityType type, final JSONObject entity) {
        return switch (type) {
            case CONSUMER -> createConsumer(entity);
            case DISTRIBUTOR -> createDistributor(entity);
        };
    }
}
