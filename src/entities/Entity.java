package entities;

import org.json.simple.JSONObject;

/**
 * Main class to be inherited by consumers and distributors.
 */
@SuppressWarnings("deprecation")
public class Entity {
    protected final long id; // the id of an entity
    private long budget; // the budget of an entity
    private boolean bankrupt; // the financial status of an entity

    /**
     * Class constructor with one parameter.
     *
     * @param entity to be added, extracted from input data
     */
    public Entity(final JSONObject entity) {
        this.id = (long) entity.get("id");
        this.budget = (long) entity.get("initialBudget");
        this.bankrupt = false;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @return thr budget
     */
    public long getBudget() {
        return budget;
    }

    /**
     * Sets the budget with the given parameter.
     *
     * @param budget new budget
     */
    public void setBudget(long budget) {
        this.budget = budget;
    }

    /**
     * @return the financial status
     */
    public boolean isBankrupt() {
        return bankrupt;
    }

    /**
     * Sets the financial status with the given parameter.
     *
     * @param bankrupt new financial status
     */
    public void setBankrupt(boolean bankrupt) {
        this.bankrupt = bankrupt;
    }
}
