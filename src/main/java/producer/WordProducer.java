package producer;

import common.validate.WordValidator;
import common.vo.Message;
import common.vo.WordFunnelException;
import manager.MessageBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Haylie
 * @since 20/07/2019.
 */
public class WordProducer extends Thread {
    private Logger logger = LoggerFactory.getLogger(WordProducer.class);
    private static final String DIR_SEPARATOR = "/";
    private static final String CURRENT_DIR = ".";
    private String fileName;
    private MessageBroker messageBroker;
    private WordValidator validator;

    public WordProducer(MessageBroker messageBroker, WordValidator validator, String fileName) {
        this.messageBroker = messageBroker;
        this.fileName = fileName;
        this.validator = validator;
    }

    @Override
    public void run() {
        produce();
    }

    private void produce() {
        File inputFile = new File(getAbsoluteFileName(fileName));

        try (FileReader fileReader = new FileReader(inputFile);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) { //try with resource
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (validator.isValid(line)) send(new Message(line));
            }

            messageBroker.sendEof();
        } catch (IOException e) {
            throw new WordFunnelException("입력파일이 존재하지 않습니다.", WordFunnelException.ExceptionType.FILE_NOT_FOUND);
        }
    }

    private String getAbsoluteFileName(String fileName) {
        return CURRENT_DIR + DIR_SEPARATOR + fileName;
    }

    private void send(Message message) {
        messageBroker.accumulate(message);
    }
}
