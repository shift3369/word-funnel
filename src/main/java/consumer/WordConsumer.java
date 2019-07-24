package consumer;

import common.file.FileData;
import common.file.FileSyncWriter;
import common.vo.Message;
import common.vo.WordFunnelException;
import manager.MessageBroker;

import java.util.concurrent.LinkedBlockingQueue;


/**
 * @author Haylie
 * @since 20/07/2019.
 */
public class WordConsumer extends Thread {
    private static boolean isRun = Boolean.FALSE;
    private static final String NUMBER_FILE_NAME = "number";
    private MessageBroker messageBroker;
    private FileSyncWriter fileSyncWriter;
    private int partitionIndex;

    public WordConsumer(MessageBroker messageBroker, FileSyncWriter fileSyncWriter, int partitionIndex) {
        this.messageBroker = messageBroker;
        this.fileSyncWriter = fileSyncWriter;
        this.partitionIndex = partitionIndex;
    }

    @Override
    public void run() {
        isRun = Boolean.TRUE;
        consume();
    }

    private void consume() {
        LinkedBlockingQueue<Message> queue;
        try {
            queue = messageBroker.assign(partitionIndex);
            while (queue.size() > 0 || isRun) {
                Message message = queue.poll();

                if (message == null) {
                    Thread.yield(); //Context Switching
                    continue;
                }

                if (message.isEof()) {
                    isRun = Boolean.FALSE;
                    continue;
                }

                fileSyncWriter.putData(new FileData(message.getWord(), getFileName(message.getWord())));
            }
        } catch (WordFunnelException e) {
            isRun = Boolean.FALSE;
        }
    }

    private String getFileName(String word) {
        String standardizedWord = word.toLowerCase();
        char firstLetter = standardizedWord.charAt(0);
        if (Character.isAlphabetic(firstLetter)) {
            return String.valueOf(firstLetter);
        }

        return NUMBER_FILE_NAME;
    }
}
