package manager;

import common.partition.PartitionDistributor;
import common.vo.Message;
import common.vo.WordFunnelException;
import common.vo.WordFunnelException.ExceptionType;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Haylie
 * @since 20/07/2019.
 */
public class MessageBroker {
    private static PartitionDistributor partitionDistributor;
    private int partitionNumber;
    private LinkedBlockingQueue<Message>[] partitions;

    public MessageBroker(int partitionNumer) {
        this.partitionNumber = partitionNumer;
        partitionDistributor = new PartitionDistributor();
        initPartitions();
    }

    private void initPartitions() {
        partitions = new LinkedBlockingQueue[partitionNumber];

        for (int idx = 0; idx < partitionNumber; idx++) {
            partitions[idx] = new LinkedBlockingQueue<>();
        }
    }

    public void accumulate(Message message) {
        String value = message.getWord();
        int index = partitionDistributor.getDistributedPartitionIndex(value, partitionNumber);
        try {
            partitions[index].add(message);
        } catch (Exception e) {
            System.out.println("index:" + index + "/size:" + partitions[index].size());
        }
    }

    public void sendEof() {
        for (int idx = 0; idx < partitionNumber; idx++) {
            partitions[idx].add(new Message(Boolean.TRUE));
        }
    }

    public LinkedBlockingQueue<Message> assign(int partitionIndex) throws WordFunnelException {
        if (partitionIndex >= partitionNumber) {
            throw new WordFunnelException("파티션의 index가 파티션 수보다 큽니다.", ExceptionType.PARTITION_ERROR);
        }

        return partitions[partitionIndex];
    }
}
