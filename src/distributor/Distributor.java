package distributor;

import consumer.Consumer;

import entities.Entity;

import game.Utils;

import org.json.simple.JSONObject;

import producer.Producer;

import strategies.EnergyChoiceStrategyType;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Observer of the producers' changes.
 */
@SuppressWarnings("deprecation")
public final class Distributor extends Entity implements Observer {
    protected final long contractLength; // the length of the contract
    protected long infrastructureCost; // the infrastructure cost
    protected long productionCost; // the production cost
    protected long nrClients; // number of clients of a distributor
    protected long totalCost; // total monthly costs of a distributor
    protected long monthlyRate; // monthly rate set for clients by a distributor
    protected List<Consumer> clients; // list of clients
    protected long budget; // monthly budget of a distributor
    protected boolean bankrupt; // bankruptcy status of a distributor
    protected long energyNeededKW; // energy needed to be supplied
    protected EnergyChoiceStrategyType producerStrategy; // strategy used for energy choice
    protected List<Producer> energyProducers; // current list of producers supplying energy
    protected boolean hasChanged; // modifies when the energy supplied of a producer changes

    /**
     * Class constructor with one parameter.
     *
     * @param distributor to be added, extracted from input data
     */
    public Distributor(final JSONObject distributor) {
        super(distributor); // super constructor call
        bankrupt = false;
        energyNeededKW = (long) distributor.get("energyNeededKW");
        producerStrategy = EnergyChoiceStrategyType.valueOf((String)
                distributor.get("producerStrategy"));
        budget = (long) distributor.get("initialBudget");
        contractLength = (long) distributor.get("contractLength");
        infrastructureCost = (long) distributor.get("initialInfrastructureCost");
        clients = new ArrayList<>();
        hasChanged = false;
    }

    /**
     * Calculates the production cost based on the list of suppliers.
     *
     * @return the production cost of a distributor
     */
    public long calculateProductionCost() {
        long newProductionCost = energyProducers.stream().mapToLong(producer ->
                (long) (producer.getEnergyPerDistributor() * producer.getPriceKW())).sum();
        newProductionCost = Math.round(Math.floor((double) newProductionCost / Utils.COST));
        return newProductionCost;
    }

    /**
     * Sets the initial cost of a contract signed between the current distributor and potential
     * client
     */
    public void calculateInitialMonthlyRate() {
        productionCost = calculateProductionCost(); // sets the production cost
        long profit = Math.round(Math.floor(Utils.PROFIT * productionCost)); // profit of the month
        monthlyRate = infrastructureCost + productionCost + profit;
    }

    /**
     * Updates the infrastructure costs of a distributor given through a monthly update.
     *
     * @param distributor to be updated, extracted from input data
     */
    public void updateDistributor(final JSONObject distributor) {
        infrastructureCost = (long) distributor.get("infrastructureCost");
    }

    /**
     * Notifies the observer that a producer suffered changes and that he may be affected. Sets
     * the change flag on if the observer was affected by the updates.
     *
     * @param o          observable object
     * @param producerId id of the producer that suffered changes
     */
    @Override
    public void update(Observable o, Object producerId) {
        for (Producer producer : energyProducers) {
            // verifies whether the distributor is supplied by the modified producer and sets the
            // flag accordingly
            if (producer.getId() == (long) producerId) {
                hasChanged = true;
                break;
            }
        }
    }

    public long getContractLength() {
        return contractLength;
    }

    public long getInfrastructureCost() {
        return infrastructureCost;
    }

    public long getMonthlyRate() {
        return monthlyRate;
    }

    public long getBudget() {
        return budget;
    }

    public boolean isBankrupt() {
        return bankrupt;
    }

    public long getEnergyNeededKW() {
        return energyNeededKW;
    }

    public EnergyChoiceStrategyType getProducerStrategy() {
        return producerStrategy;
    }

    public List<Consumer> getClients() {
        return clients;
    }

    public boolean isHasChanged() {
        return hasChanged;
    }

    public List<Producer> getEnergyProducers() {
        return energyProducers;
    }
}
