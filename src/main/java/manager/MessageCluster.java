package manager;

import common.vo.Message;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Haylie
 * @since 20/07/2019.
 */
public class MessageCluster {
    private int partitionNumer;
    private LinkedBlockingQueue<Message>[] partitions;

    public MessageCluster(int partitionNumer) {
        this.partitionNumer = partitionNumer;
        initPartitions();
    }

    private void initPartitions() {
        partitions = new LinkedBlockingQueue[partitionNumer];

        for (int idx = 0; idx < partitionNumer; idx++) {
            partitions[idx] = new LinkedBlockingQueue<>();
        }
    }

    public void accumulate(Message message) {
        String value = message.getWord();
        int index = getDistributedPartitionIndex(value);
        partitions[index].add(message);
    }

    public void sendEof() {
        for(int idx = 0; idx < partitionNumer; idx++) {
            partitions[idx].add(new Message(Boolean.TRUE));
        }
    }

    public LinkedBlockingQueue<Message> assign(int partitionIndex) {
        return partitions[partitionIndex];
    }

    private int getDistributedPartitionIndex(String input) {
        String standardizedInput = input.toLowerCase();
        int hashedInput = standardizedInput.hashCode();
        return hashedInput % partitionNumer;
    }
}
