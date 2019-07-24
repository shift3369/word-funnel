package common.partition;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Spy;

import static org.junit.Assert.*;

/**
 * @author Haylie
 * @since 24/07/2019.
 */
public class PartitionDistributorTest {
    private int partitionNumber = 5;
    @Spy
    private PartitionDistributor dut = new PartitionDistributor();

    @Test
    public void input이_같으면_같은_파티션_index_반환() {
        String input1 = "abc";
        String input2 = "abc";

        int actual1 = dut.getDistributedPartitionIndex(input1, partitionNumber);
        int actual2 = dut.getDistributedPartitionIndex(input2, partitionNumber);
        Assert.assertEquals(actual1, actual2);
    }

    @Test
    public void index는_partitionNumber보다_작다() {
        String input1 = "abc";
        int actual1 = dut.getDistributedPartitionIndex(input1, partitionNumber);
        Assert.assertTrue(actual1 <= partitionNumber);
    }
}