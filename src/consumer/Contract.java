package consumer;

import distributor.Distributor;

public final class Contract {
    protected long currentContract; // current distributor id that sets the contract
    protected long monthlyRate; // monthly rate of the contract
    protected long length; // number of months to be paid
    protected long debt; // remaining sum to be paid of a previous month

    public Contract() {
        this.debt = 0; // initially, debt is non-existent
    }

    /**
     * Updates the contract of a consumer accordingly to his new distributor's preferences.
     *
     * @param preferredDistributor the database of all distributors used for calculating the lowest
     *                             monthly rate of a new contract
     */
    public void updateContract(final Distributor preferredDistributor) {
        currentContract = preferredDistributor.getId(); // the id of the current distributor is set
        length = preferredDistributor.getContractLength(); // the new contract length is set
        monthlyRate = preferredDistributor.getMonthlyRate(); // the new monthly rate is set
    }

    public long getMonthlyRate() {
        return monthlyRate;
    }

    public long getLength() {
        return length;
    }

    public long getDebt() {
        return debt;
    }
}
