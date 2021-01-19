package consumer;

import entities.Entity;

import org.json.simple.JSONObject;

@SuppressWarnings("deprecation")
public final class Consumer extends Entity {
    protected final long monthlyIncome; // monthly income of a consumer
    protected final Contract contract; // contract signed by a consumer with a distributor

    /**
     * Class constructor with one parameter.
     *
     * @param consumer to be added, extracted from input data
     */
    public Consumer(final JSONObject consumer) {
        super(consumer); // super constructor call
        this.monthlyIncome = (long) consumer.get("monthlyIncome");
        this.contract = new Contract();
    }

    public long getMonthlyIncome() {
        return monthlyIncome;
    }

    public Contract getContract() {
        return contract;
    }
}
