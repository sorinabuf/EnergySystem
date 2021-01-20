package consumer;

import distributor.Distributor;
import distributor.DistributorsDB;

import game.Utils;

import java.util.ArrayList;
import java.util.List;

public final class ConsumersDB {
    private final List<Consumer> consumers; // list of all consumers

    /**
     * Class constructor with one parameter.
     *
     * @param consumers to be added to the database list
     */
    public ConsumersDB(final List<Consumer> consumers) {
        this.consumers = new ArrayList<>(consumers);
    }

    /**
     * Sets the contracts of the consumers for month number 0.
     *
     * @param distributorsDatabase the database of all distributors used for calculating the lowest
     *                             monthly rate of a new contract
     */
    public void setInitialMonth(final DistributorsDB distributorsDatabase) {
        // the distributor with the smallest requested monthly rate will initially set contracts
        // with all consumers
        Distributor preferredDistributor = distributorsDatabase.getMinPrice();

        for (Consumer consumer : consumers) {
            consumer.contract.updateContract(preferredDistributor); // sets the new contracts
        }
    }

    /**
     * Updates the contracts and budgets of all consumers at the beginning of a new month
     * accordingly. If the contract of a consumer reaches the end, a new distributor will be
     * chosen, otherwise the contract continues without being updated. If a consumer does not
     * afford the monthly rate, a remaining debt will be set consequently. If a consumer is
     * already in debt, without being able to pay the remaining sum and the new month, he will be
     * declared bankrupt and excluded from the game.
     *
     * @param distributorsDatabase the database of all distributors used for calculating the lowest
     *                             monthly rate of a new contract
     */
    public void updateConsumers(final DistributorsDB distributorsDatabase) {
        long newBudget; // updated budget of a consumer
        for (Consumer consumer : consumers) {
            if (consumer.isBankrupt()) { // bankrupt consumers are not updated anymore
                continue;
            }

            // if a consumer reaches the end of contract, he will choose a new contract with the
            // lowest monthly rate
            if (consumer.contract.length == 0) {
                // the distributor with the smallest requested monthly rate
                Distributor preferredDistributor = distributorsDatabase.getMinPrice();
                consumer.contract.updateContract(preferredDistributor); // updates the contract
                // adds the consumer to the new distributor's list of clients
                preferredDistributor.getClients().add(consumer);
            }

            newBudget = consumer.budget + consumer.monthlyIncome
                    - consumer.contract.monthlyRate; // budget of a consumer in a new month

            if (consumer.contract.debt == 0) { // verify whether a consumer is not in debt
                if (newBudget < 0) {
                    // if the consumer can not afford to pay the new month, his remaining debt
                    // sum is set
                    newBudget = newBudget + consumer.contract.monthlyRate;
                    consumer.contract.debt = Math.round(Math.floor(Utils.DEBT
                            * consumer.contract.monthlyRate));
                }
                consumer.budget = newBudget; // the new budget is set
                // the length of the contract is decremented
                consumer.contract.length = consumer.contract.length - 1;
            } else {
                newBudget = newBudget - consumer.contract.debt;
                // if the consumer can not afford to pay the new month and the existing debt, he
                // will be declared bankrupt
                if (newBudget <= 0) {
                    newBudget = consumer.getBudget() + consumer.monthlyIncome;
                    consumer.bankrupt = true;
                } else {
                    // the length of the contract is decremented
                    consumer.contract.length = consumer.contract.length - 1;
                }
                consumer.budget = newBudget; // the new budget is set
            }
        }
    }

    public List<Consumer> getConsumers() {
        return consumers;
    }
}
