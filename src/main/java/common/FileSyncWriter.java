package common;

import common.vo.FileData;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Haylie
 * @since 21/07/2019.
 */
public class FileSyncWriter extends Thread {
    private LinkedBlockingQueue<FileData> dataQueue = new LinkedBlockingQueue<>();
    private boolean isRun = Boolean.FALSE;

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
            try {
                FileData data = dataQueue.take();
                String value = data.getMessage().getWord();
                String fileName = data.getAbsoluteFileName();
                System.out.println("File Write:" + value);

                try {
                    FileWriter fileWriter = new FileWriter(fileName, Boolean.TRUE);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.append(value);
                    bufferedWriter.close();
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
