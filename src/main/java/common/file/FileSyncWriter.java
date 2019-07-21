package common.file;

import common.validate.DuplicationValidator;
import common.validate.Validator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Haylie
 * @since 21/07/2019.
 */
public class FileSyncWriter extends Thread {
    private static final String DIR_SEPARATOR = "/";
    private static final String TXT_EXTENSION = ".txt";
    private static LinkedBlockingQueue<FileData> dataQueue = new LinkedBlockingQueue<>();
    private Validator duplicationValidator;
    private boolean isRun = Boolean.FALSE;
    private String filePath;

    public FileSyncWriter(String filePath) {
        this.filePath = filePath;
        this.duplicationValidator = new DuplicationValidator();
    }

    public void putData(FileData data) {
        dataQueue.offer(data);
    }

    @Override
    public void run() {
        isRun = Boolean.TRUE;
        write();
    }

    public void close() {
        isRun = Boolean.FALSE;
    }

    private void write() {
        while (dataQueue.size() > 0 || isRun) {
            FileData data = dataQueue.poll(); //TODO take() 처리시 마지막 해제 고민해보기

            if(data == null) {
                Thread.yield(); //Context Switching
                continue;
            }

            String value = data.getValue() + "";
            String fileName = data.getFileName();

            if (duplicationValidator.isValid(data)) {
                try {
                    FileWriter fileWriter = new FileWriter(getAbsoluteFileName(fileName), Boolean.TRUE);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.append(value);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getAbsoluteFileName(String fileName) {
        return filePath + DIR_SEPARATOR + fileName + TXT_EXTENSION;
    }

}
