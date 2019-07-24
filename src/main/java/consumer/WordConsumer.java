package consumer;

import common.file.FileData;
import common.validate.DuplicationValidator;
import common.vo.Message;
import common.vo.WordFunnelException;
import manager.MessageBroker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * @author Haylie
 * @since 20/07/2019.
 */
public class WordConsumer extends Thread {
    private static final String DIR_SEPARATOR = "/";
    private static final String TXT_EXTENSION = ".txt";
    private static boolean isRun = Boolean.FALSE;
    private static final String NUMBER_FILE_NAME = "number";
    private static DuplicationValidator duplicationValidator = new DuplicationValidator();
    private Map<String, BufferedWriter> writerMap = new HashMap<>();
    private MessageBroker messageBroker;
    private String filePath;
    private int partitionIndex;

    public WordConsumer(MessageBroker messageBroker, String filePath, int partitionIndex) {
        this.messageBroker = messageBroker;
        this.filePath = filePath;
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

                writeWord(message.getWord());
            }
            closeWriter();

        } catch (WordFunnelException e) {
            isRun = Boolean.FALSE;
        }
    }

    private void writeWord(String word) {
        File fileDir = new File(filePath);
        fileDir.mkdirs();

        String fileName = getFileName(word);
        FileData fileData = new FileData(word, fileName);
        if (duplicationValidator.isValid(fileData)) {
            try {
                BufferedWriter bufferedWriter = writerMap.get(fileName);
                if (bufferedWriter == null) {
                    FileWriter fileWriter = new FileWriter(getAbsoluteFileName(fileName), Boolean.TRUE);
                    bufferedWriter = new BufferedWriter(fileWriter);
                    writerMap.put(fileName, bufferedWriter);
                }
                bufferedWriter.append(word);
                System.out.println(word);
            } catch (IOException e) {
                throw new WordFunnelException(WordFunnelException.ExceptionType.FILE_IO_EXCEPTION);
            }
        }
    }

    private void closeWriter() {
        for (BufferedWriter writer : writerMap.values()) {
            try {
                writer.flush();
                writer.close();
                writerMap = new HashMap<>();
            } catch (IOException e) {
                throw new WordFunnelException(WordFunnelException.ExceptionType.FILE_IO_EXCEPTION);
            }
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

    private String getAbsoluteFileName(String fileName) {
        return filePath + DIR_SEPARATOR + fileName + TXT_EXTENSION;
    }
}
