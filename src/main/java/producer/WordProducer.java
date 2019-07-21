package producer;

import common.vo.Message;
import manager.MessageCluster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Haylie
 * @since 20/07/2019.
 */
public class WordProducer extends Thread {
    private static final String REG_EXP = "^[a-zA-Z0-9]+$";
    private static final String DIR_SEPARATOR = "/";
    private String filePath;
    private String fileName;
    private MessageCluster cluster;

    public WordProducer(MessageCluster cluster, String filePath, String fileName) {
        this.cluster = cluster;
        this.filePath = filePath;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        produce();
    }

    private void produce() {
        // filePath, fileName이 null일때의 처리
        File inputFile = new File(getAbsoluteFileName(filePath, fileName));

        try {
            FileReader fileReader = new FileReader(inputFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            WordValidator validator = new WordValidator(REG_EXP);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if(validator.isValid(line)) send(new Message(line));
            }

            cluster.sendEof();
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            //TODO custom exception 만들어서 처리
            e.printStackTrace();
        }
    }

    private String getAbsoluteFileName(String filePath, String fileName) {
        return filePath + DIR_SEPARATOR + fileName;
    }

    private void send(Message message) {
        cluster.accumulate(message);
        System.out.println("Send message: " + message.getWord());
    }
}
