package fileio;

import consumer.Consumer;
import consumer.ConsumersDB;

import distributor.Distributor;
import distributor.DistributorsDB;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

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
     * @param consumersDatabase database of all consumers left in game
     * @param distributorsDatabase database of all distributors left in game
     * @throws IOException input/output exception
     */
    public void writeData(final ConsumersDB consumersDatabase,
                          final DistributorsDB distributorsDatabase) throws IOException {
        JSONObject output = new JSONObject(); // the output object that will be written
        JSONArray consumersOutput = new JSONArray(); // array of resulted consumers
        JSONArray distributorsOutput = new JSONArray(); // array of resulted distributors

        for (Consumer c : consumersDatabase.getConsumers()) {
            // consumer to be displayed with all its characteristics
            JSONObject consumerOutput = new JSONObject();
            consumerOutput.put("id", c.getId());
            consumerOutput.put("isBankrupt", c.isBankrupt());
            consumerOutput.put("budget", c.getBudget());
            consumersOutput.add(consumerOutput);
        }

        for (Distributor d : distributorsDatabase.getDistributors()) {
            // distributor to be displayed with all its characteristics
            JSONObject distributorOutput = new JSONObject();
            distributorOutput.put("id", d.getId());
            distributorOutput.put("isBankrupt", d.isBankrupt());
            distributorOutput.put("budget", d.getBudget());

            JSONArray contracts = new JSONArray(); // array of distributor contracts
            for (Consumer c : d.getClients()) {
                // clients to be displayed with all its characteristics
                JSONObject client = new JSONObject();
                client.put("consumerId", c.getId());
                client.put("price", c.getContract().getMonthlyRate());
                client.put("remainedContractMonths", c.getContract().getLength());
                contracts.add(client);
            }
            distributorOutput.put("contracts", contracts);
            distributorsOutput.add(distributorOutput);
        }
        output.put("consumers", consumersOutput);
        output.put("distributors", distributorsOutput);

        file.write(output.toJSONString());
        file.flush();
        file.close();
    }
}
