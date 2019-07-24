import common.file.FileSyncWriter;
import common.validate.WordValidator;
import consumer.WordConsumer;
import manager.MessageBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import producer.WordProducer;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Haylie
 * @since 20/07/2019.
 */
public class WordFunnel {
    private static final String REG_EXP = "^[a-zA-Z0-9]+$";
    private static WordProducer producer;
    private static ExecutorService consumerExecutorService;
    private static FileSyncWriter fileSyncWriter;
    private static Logger logger = LoggerFactory.getLogger(WordFunnel.class);

    public static void main(String args[]) {
        if (isValidParameter(args)) {
            String inputFile = args[0];
            String filePath = args[1];
            int partitionNumber = Integer.valueOf(args[2]);

            if (isValidInputFile(inputFile) && isValidPartitionNumber(partitionNumber)) {
                start(filePath, inputFile, partitionNumber);
                finish();
            }
        }

        logger.info("WordFunnel이 종료되었습니다.");
    }

    private static boolean isValidInputFile(String fileName) {
        File inputFile = new File(fileName);
        if (inputFile.exists()) {
            return Boolean.TRUE;
        } else {
            logger.info("입력 파일이 존재하지 않습니다.");
            return Boolean.FALSE;
        }
    }

    private static boolean isValidPartitionNumber(int partitionNumber) {
        if (partitionNumber > 2 && partitionNumber < 28) {
            return Boolean.TRUE;
        } else {
            logger.info("파티션 수는 2보다 크거나 28 보다 작아야합니다.");
            return Boolean.FALSE;
        }
    }

    private static boolean isValidParameter(String args[]) {
        if (args.length != 3) {
            logger.info("필요한 파라미터가 충족되지 않아 프로그램을 종료합니다.");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private static void start(String filePath, String inputFile, int partitionNumber) {
        MessageBroker messageBroker = new MessageBroker(partitionNumber);
        producer = new WordProducer(messageBroker, new WordValidator(REG_EXP), inputFile);
        producer.start();
        consumerExecutorService = Executors.newFixedThreadPool(partitionNumber);
        fileSyncWriter = new FileSyncWriter(filePath);
        fileSyncWriter.start();

        for (int idx = 0; idx < partitionNumber; idx++) {
            consumerExecutorService.submit(new WordConsumer(messageBroker, fileSyncWriter, idx));
        }

        logger.info("WordFunnel이 시작되었습니다.");
    }

    private static void finish() {
        try {
            producer.join();
            consumerExecutorService.shutdown();
            while (!consumerExecutorService.awaitTermination(20, TimeUnit.MINUTES)) ;
            fileSyncWriter.close();
            System.out.println("writer close");
            fileSyncWriter.join();
            logger.info("리소스 정리가 완료되었습니다.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
