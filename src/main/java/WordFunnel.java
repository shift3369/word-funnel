import common.file.FileSyncWriter;
import consumer.WordConsumer;
import manager.MessageCluster;
import producer.WordProducer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Haylie
 * @since 20/07/2019.
 */
public class WordFunnel {
    public static void main(String args[]) {
        String filePath = args[0];
        String inputFile = args[1];
        int partitionNumber = Integer.valueOf(args[2]);

        MessageCluster cluster = new MessageCluster(partitionNumber);
        WordProducer producer = new WordProducer(cluster, inputFile);
        producer.start();

        ExecutorService executorService = Executors.newFixedThreadPool(partitionNumber);
        FileSyncWriter fileSyncWriter = new FileSyncWriter(filePath);
        fileSyncWriter.start();

        for (int idx = 0; idx < partitionNumber; idx++) {
            executorService.submit(new WordConsumer(cluster, fileSyncWriter, idx));
        }

        try {
            producer.join();
            System.out.println("Producer join");

            executorService.shutdown();
            System.out.println("Executor shutdown");
            while (!executorService.awaitTermination(20, TimeUnit.MINUTES)) ;

            fileSyncWriter.close();
            System.out.println("writer close");
            fileSyncWriter.join();
            System.out.println("writer join");
            System.out.println("FINISH WORD FUNNEL");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
