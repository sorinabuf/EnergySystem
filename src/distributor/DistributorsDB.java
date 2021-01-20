package distributor;

import consumer.Consumer;
import consumer.ConsumersDB;

import game.Utils;

import producer.Producer;
import producer.ProducersDB;

import strategies.EnergyChoiceStrategyFactory;

import java.util.ArrayList;
import java.util.List;

public final class DistributorsDB {
    private final List<Distributor> distributors; // list of all distributors

    /**
     * Class constructor with one parameter.
     *
     * @param distributors to be added to the database list
     */
    public DistributorsDB(final List<Distributor> distributors) {
        this.distributors = new ArrayList<>(distributors);
    }

    /**
     * Sets the initial cost and clients list of the distributors for month number 0. The only
     * distributor with clients in the first month is the one with the smallest monthly rate.
     *
     * @param consumersDatabase the database of all consumers used for setting up the initial client
     *                          list
     */
    public void setInitialMonth(final ConsumersDB consumersDatabase) {
        // the distributor with the smallest requested monthly rate will initially set contracts
        // with all consumers
        Distributor preferredDistributor = getMinPrice();
        long totalCost; // the initial total cost
        for (Distributor distributor : distributors) {
            if (distributor == preferredDistributor) {
                distributor.nrClients = consumersDatabase.getConsumers().size();
                totalCost = distributor.infrastructureCost + distributor.productionCost
                        * distributor.nrClients; // initial cost for the preferred distributor
                distributor.clients = new ArrayList<>(consumersDatabase.getConsumers());
            } else {
                totalCost = distributor.infrastructureCost;
            }
            distributor.totalCost = totalCost; // initial cost for the rest of the distributors
        }
    }

    /**
     * Sets the initial cost of a contract signed between the current distributor and potential
     * client for the all database of distributors.
     */
    public void setInitialMonthlyRate() {
        for (Distributor distributor : distributors) {
            distributor.calculateInitialMonthlyRate();
        }
    }

    /**
     * Sets the monthly rates for all distributors based on the profit and the number of clients.
     */
    public void calculateMonthlyRate() {
        long monthlyRate; // the new monthly rate calculated
        long profit; // the current profit
        for (Distributor distributor : distributors) {
            profit = Math.round(Math.floor(Utils.PROFIT * distributor.productionCost));
            if (distributor.nrClients == 0) { // verify whether the distributor has any clients
                monthlyRate = distributor.infrastructureCost + distributor.productionCost + profit;
            } else {
                monthlyRate = Math.round(Math.floor((double) distributor.infrastructureCost
                        / distributor.nrClients) + distributor.productionCost + profit);
            }
            distributor.monthlyRate = monthlyRate; // sets the new monthly rate of the distributor
        }
    }

    /**
     * Removes all the invalid contracts consisting in clients who reached the end of the contract.
     */
    public void removeInvalidContracts() {
        for (Distributor distributor : distributors) {
            distributor.clients.removeIf(consumer -> consumer.getContract().getLength() == 0);
            distributor.nrClients = distributor.clients.size(); // sets the new number of clients
        }
    }

    /**
     * Updates the clients lists, the budget and the monthly total cost of all distributors from
     * the database based on the profit and number of clients. The budget will be calculated as
     * the old budget from which the total costs are subtracted and the monthly rates paid by the
     * clients are added. If the budget of a distributor goes negative, he will be declared
     * bankrupt. At the end of the update, the bankrupt clients will be removed from the
     * distributors' lists.
     */
    public void updateDistributors() {
        long newBudget; // the new budget of a distributor
        for (Distributor distributor : distributors) {
            if (distributor.isBankrupt()) { // bankrupt distributors are not updated anymore
                continue;
            }
            // sets the current number of valid clients from the clients list
            distributor.nrClients = distributor.clients.size();
            distributor.totalCost = distributor.infrastructureCost + distributor.nrClients
                    * distributor.productionCost;
            // initially the budget is calculated as the difference between old budget and costs
            newBudget = distributor.budget - distributor.totalCost;
            // for each consumer that paid in the current month, his monthly rate is added to the
            // distributor's budget
            for (Consumer consumer : distributor.clients) {
                if (consumer.getContract().getDebt() == 0) {
                    newBudget += consumer.getContract().getMonthlyRate();
                }
            }
            // if the budget becomes negative, the distributor is declared bankrupt
            if (newBudget < 0) {
                distributor.bankrupt = true;
            }
            distributor.budget = newBudget; // sets the new budget of a distributor
            // removes any bankrupt clients from the clients list and updates the number of clients
            distributor.getClients().removeIf(Consumer::isBankrupt);
            distributor.nrClients = distributor.getClients().size();
        }
    }

    /**
     * Returns the distributor from the database with the smallest monthly rate.
     *
     * @return the distributor who offers the lowest monthly rate
     */
    public Distributor getMinPrice() {
        Distributor preferredDistributor = null; // initializes the distributor with the lowest rate
        long minPrice = Integer.MAX_VALUE; // initializes the minimum price offered
        // search for a distributor that is not bankrupt and offers the lowest monthly rate
        for (Distributor distributor : distributors) {
            if (!distributor.isBankrupt() && distributor.monthlyRate < minPrice) {
                minPrice = distributor.monthlyRate;
                preferredDistributor = distributor;
            }
        }
        return preferredDistributor;
    }

    /**
     * Updates the list of suppliers, the change flag and the production cost of the distributors
     * that have been affected by the modifications of the observable, producers' database.
     *
     * @param choiceStrategyFactory factory that creates strategies for distributors
     * @param producersDatabase     database of all producers
     */
    public void updateEnergySources(final EnergyChoiceStrategyFactory choiceStrategyFactory,
                                    final ProducersDB producersDatabase) {
        for (Distributor distributor : distributors) {
            // verifies whether the distributor must update the list of suppliers
            if (distributor.isHasChanged()) {
                // if the list must be updated, the distributor is removed from the modified
                // producer's list of distributors
                for (Producer producer : distributor.getEnergyProducers()) {
                    producer.getDistributors().remove(distributor);
                }
                // creates the new list of producers chosen through the distributor's strategy
                distributor.energyProducers =
                        choiceStrategyFactory.createStrategy(distributor.getProducerStrategy(),
                                producersDatabase, distributor).getEnergyProducers();
                distributor.hasChanged = false; // the change flag is reset to 0
                // the production cost is updated
                distributor.productionCost = distributor.calculateProductionCost();
            }
        }
    }

    /**
     * Sets the initial lists of energy producers of all distributors from the database.
     * Additionally, adds all the initiated distributors to the observers list of the observable.
     *
     * @param choiceStrategyFactory factory that creates strategies for distributors
     * @param producersDatabase     database of all producers
     */
    public void initiateEnergySources(final EnergyChoiceStrategyFactory choiceStrategyFactory,
                                      final ProducersDB producersDatabase) {
        for (Distributor distributor : distributors) {
            // creates the initial list of producers chosen through the distributor's strategy
            distributor.energyProducers =
                    choiceStrategyFactory.createStrategy(distributor.getProducerStrategy(),
                            producersDatabase, distributor).getEnergyProducers();
            // adds the distributor to the observers list of the producers' database
            producersDatabase.addObserver(distributor);
        }
    }

    public List<Distributor> getDistributors() {
        return distributors;
    }
}
