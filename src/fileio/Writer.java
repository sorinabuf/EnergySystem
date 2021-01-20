package fileio;

import consumer.Consumer;
import consumer.ConsumersDB;

import distributor.Distributor;
import distributor.DistributorsDB;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import producer.Producer;
import producer.ProducersDB;

import java.io.FileWriter;
import java.io.IOException;

import java.util.Comparator;

@SuppressWarnings({"unchecked", "deprecation"})
public final class Writer {
    private final FileWriter file; // the output file writer

    /**
     * Class constructor with two parameters.
     *
     * @param path of the given output file
     * @throws IOException input/output exception
     */
    public Writer(final String path) throws IOException {
        this.file = new FileWriter(path);
    }

    /**
     * Method used for writing the current state of the simulation at the end of the game.
     *
     * @param consumersDatabase    database of all consumers left in game
     * @param distributorsDatabase database of all distributors left in game
     * @param producersDatabase    database of all producers left in game
     * @param numberOfTurns        total number of turns of the game
     * @throws IOException input/output exception
     */
    public void writeData(final ConsumersDB consumersDatabase,
                          final DistributorsDB distributorsDatabase,
                          final ProducersDB producersDatabase,
                          final long numberOfTurns) throws IOException {
        JSONObject output = new JSONObject(); // the output object that will be written
        JSONArray consumersOutput = new JSONArray(); // array of resulted consumers
        JSONArray distributorsOutput = new JSONArray(); // array of resulted distributors
        JSONArray producersOutput = new JSONArray(); // array of resulted producers

        for (Consumer c : consumersDatabase.getConsumers()) {
            // consumer to be displayed with all his characteristics
            JSONObject consumerOutput = new JSONObject();
            consumerOutput.put("id", c.getId());
            consumerOutput.put("isBankrupt", c.isBankrupt());
            consumerOutput.put("budget", c.getBudget());
            consumersOutput.add(consumerOutput);
        }

        for (Distributor d : distributorsDatabase.getDistributors()) {
            // distributor to be displayed with all his characteristics
            JSONObject distributorOutput = new JSONObject();
            distributorOutput.put("id", d.getId());
            distributorOutput.put("energyNeededKW", d.getEnergyNeededKW());
            distributorOutput.put("contractCost", d.getMonthlyRate());
            distributorOutput.put("budget", d.getBudget());
            distributorOutput.put("producerStrategy", d.getProducerStrategy().getLabel());
            distributorOutput.put("isBankrupt", d.isBankrupt());

            JSONArray contracts = new JSONArray(); // array of distributor contracts
            for (Consumer c : d.getClients()) {
                // clients to be displayed with all their characteristics
                JSONObject client = new JSONObject();
                client.put("consumerId", c.getId());
                client.put("price", c.getContract().getMonthlyRate());
                client.put("remainedContractMonths", c.getContract().getLength());
                contracts.add(client);
            }
            distributorOutput.put("contracts", contracts);
            distributorsOutput.add(distributorOutput);
        }

        for (Producer p : producersDatabase.getProducers()) {
            // producer to be displayed with all his characteristics
            JSONObject producerOutput = new JSONObject();
            producerOutput.put("id", p.getId());
            producerOutput.put("maxDistributors", p.getMaxDistributors());
            producerOutput.put("priceKW", p.getPriceKW());
            producerOutput.put("energyType", p.getEnergyType().getLabel());
            producerOutput.put("energyPerDistributor", p.getEnergyPerDistributor());

            JSONArray monthlyStats = new JSONArray(); // array of producers' monthly distributors
            for (long i = 1; i <= numberOfTurns; ++i) {
                // month to be displayed with the respective supplied distributors
                JSONObject month = new JSONObject();
                month.put("month", i);

                JSONArray distributorsIds = new JSONArray(); // array of distributors' ids
                int monthNr = (int) (i - 1); // number of the turn

                for (Distributor d : p.getAllDistributors().get(monthNr)) {
                    distributorsIds.add(d.getId());
                    // sorts the ids before display
                    distributorsIds.sort(Comparator.comparingLong(x -> (long) x));
                }
                month.put("distributorsIds", distributorsIds);
                monthlyStats.add(month);
            }
            producerOutput.put("monthlyStats", monthlyStats);
            producersOutput.add(producerOutput);
        }
        output.put("consumers", consumersOutput);
        output.put("distributors", distributorsOutput);
        output.put("energyProducers", producersOutput);

        file.write(output.toJSONString());
        file.flush();
        file.close();
    }
}
