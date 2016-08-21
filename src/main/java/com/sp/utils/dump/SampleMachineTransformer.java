package com.sp.utils.dump;

import org.finra.datagenerator.consumer.DataConsumer;
import org.finra.datagenerator.consumer.DataPipe;
import org.finra.datagenerator.consumer.DataTransformer;
import org.finra.datagenerator.distributor.multithreaded.DefaultDistributor;
import org.finra.datagenerator.engine.Engine;
import org.finra.datagenerator.engine.scxml.SCXMLEngine;
import org.finra.datagenerator.writer.DefaultWriter;

import java.io.InputStream;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SampleMachineTransformer implements DataTransformer {

    private static final Logger log = Logger.getLogger("");
    private final Random rand = new Random(System.currentTimeMillis());

    /**
     * The transform method for this DataTransformer
     *
     * @param cr a reference to DataPipe from which to read the current map
     */
    public void transform(DataPipe cr) {
        for (Map.Entry<String, String> entry : cr.getDataMap().entrySet()) {
            String value = entry.getValue();

            if (value.equals("#{customplaceholder}")) {
                // Generate a random number
                int ran = rand.nextInt();
                entry.setValue(String.valueOf(ran));
            }
        }
    }

    public static void main(String[] args) {

        Engine engine = new SCXMLEngine();

        //will default to samplemachine, but you could specify a different file if you choose to
        InputStream is = SampleMachineTransformer.class.getResourceAsStream("/dg.xml");

        engine.setModelByInputFileStream(is);

        // Usually, this should be more than the number of threads you intend to run
        engine.setBootstrapMin(1);

        //Prepare the consumer with the proper writer and transformer
        DataConsumer consumer = new DataConsumer();
        consumer.addDataTransformer(new SampleMachineTransformer());
        consumer.addDataWriter(new DefaultWriter(System.out,
                new String[]{"var_out_V1_1", "var_out_V1_2", "var_out_V1_3", "var_out_V2", "var_out_V3",
                        "var_out_V4"}));

        //Prepare the distributor
        DefaultDistributor defaultDistributor = new DefaultDistributor();
        defaultDistributor.setThreadCount(1);
        defaultDistributor.setDataConsumer(consumer);
        Logger.getLogger("org.apache").setLevel(Level.ALL);

        engine.process(defaultDistributor);
    }
}