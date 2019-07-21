package consumer;

import common.file.FileSyncWriter;
import common.file.FileData;
import common.vo.Message;
import manager.MessageCluster;

import java.util.concurrent.LinkedBlockingQueue;


/**
 * @author Haylie
 * @since 20/07/2019.
 */
public class WordConsumer extends Thread {
    private static final String NUMBER_FILE_NAME = "number";
    private MessageCluster cluster;
    private FileSyncWriter fileSyncWriter;
    private int partitionIndex;

    public WordConsumer(MessageCluster cluster, FileSyncWriter fileSyncWriter, int partitionIndex) {
        this.cluster = cluster;
        this.fileSyncWriter = fileSyncWriter;
        this.partitionIndex = partitionIndex;
    }

    @Override
    public void run() {
        consume();
    }

    private void consume() {
        LinkedBlockingQueue<Message> queue = cluster.assign(partitionIndex);

        try {
            boolean isRun = Boolean.TRUE;
            while(queue.size() > 0 || isRun) {
                Message message = queue.take();

                if(message.isEof()) {
                    isRun = Boolean.FALSE;
                }

                fileSyncWriter.putData(new FileData(message.getWord(), getFileName(message.getWord())));
                System.out.println("Put Data" + message.getWord());
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String getFileName(String word) {
        String standardizedWord = word.toLowerCase();
        char firstLetter = standardizedWord.charAt(0);
        if(Character.isAlphabetic(firstLetter)) {
            return String.valueOf(firstLetter);
        }

        return NUMBER_FILE_NAME;
    }
}
