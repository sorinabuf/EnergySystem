package consumer;

import entities.Entity;

import org.json.simple.JSONObject;

@SuppressWarnings("deprecation")
public final class Consumer extends Entity {
    protected final long monthlyIncome; // monthly income of a consumer
    protected final Contract contract; // contract signed by a consumer with a distributor
    protected long budget; // monthly budget of a consumer
    protected boolean bankrupt; // bankruptcy status of a consumer

    /**
     * Class constructor with one parameter.
     *
     * @param consumer to be added, extracted from input data
     */
    public Consumer(final JSONObject consumer) {
        super(consumer); // super constructor call
        budget = (long) consumer.get("initialBudget");
        monthlyIncome = (long) consumer.get("monthlyIncome");
        bankrupt = false;
        contract = new Contract();
    }

    public long getMonthlyIncome() {
        return monthlyIncome;
    }

    public long getBudget() {
        return budget;
    }

    public boolean isBankrupt() {
        return bankrupt;
    }

    public Contract getContract() {
        return contract;
    }
}
