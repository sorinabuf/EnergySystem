package distributor;

import consumer.Consumer;

import entities.Entity;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public final class Distributor extends Entity {
    protected final long contractLength; // the length of the contract
    protected long infrastructureCost; // the infrastructure cost
    protected long productionCost; // the production cost
    protected long nrClients; // number of clients of a distributor
    protected long totalCost; // total monthly costs of a distributor
    protected long monthlyRate; // monthly rate set for clients by a distributor
    protected List<Consumer> clients; // list of clients
    protected static final double PROFIT = 0.2; // the percent of the profit

    /**
     * Returns the initial monthly rate of a distributor with no clients.
     *
     * @param distributor for whom the rate is calculated
     * @return the first rate to be paid by the distributor's clients
     */
    private long calculateInitialMonthlyRate(final JSONObject distributor) {
        long newInfrastructureCost = (long) (distributor).get("initialInfrastructureCost");
        long newProductionCost = (long) (distributor).get("initialProductionCost");
        long profit = Math.round(Math.floor(PROFIT * newProductionCost)); // the profit of the month
        return newInfrastructureCost + newProductionCost + profit; // the initial monthly rate
    }

    /**
     * Class constructor with one parameter.
     *
     * @param distributor to be added, extracted from input data
     */
    public Distributor(final JSONObject distributor) {
        super(distributor); // super constructor call
        this.contractLength = (long) distributor.get("contractLength");
        this.infrastructureCost = (long) distributor.get("initialInfrastructureCost");
        this.productionCost = (long) distributor.get("initialProductionCost");
        this.monthlyRate = calculateInitialMonthlyRate(distributor);
        this.clients = new ArrayList<>();
    }

    /**
     * Updates the infrastructure and production costs of a distributor given through a monthly
     * update.
     *
     * @param distributor to be updated, extracted from input data
     */
    public void updateDistributor(final JSONObject distributor) {
        infrastructureCost = (long) distributor.get("infrastructureCost");
        productionCost = (long) distributor.get("productionCost");
    }

    public long getContractLength() {
        return contractLength;
    }

    public long getInfrastructureCost() {
        return infrastructureCost;
    }

    public long getProductionCost() {
        return productionCost;
    }

    public long getMonthlyRate() {
        return monthlyRate;
    }

    public List<Consumer> getClients() {
        return clients;
    }
}
