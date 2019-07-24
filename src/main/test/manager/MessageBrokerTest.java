package manager;

import common.vo.Message;
import common.vo.WordFunnelException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Haylie
 * @since 24/07/2019.
 */
@RunWith(MockitoJUnitRunner.class)
public class MessageBrokerTest {
    private int partitionNumber = 5;
    @InjectMocks
    private MessageBroker messageBroker = new MessageBroker(partitionNumber);

    @Test
    public void assign하면_맞는_인덱스의_파티션_반환() throws WordFunnelException {
        int index = 4;
        LinkedBlockingQueue<Message> partition = messageBroker.assign(index);
        Assert.assertNotNull(partition);
    }

    @Test(expected = WordFunnelException.class)
    public void 잘못된_index로_assign하면_exception_발생() throws WordFunnelException {
        int index = 5;
        messageBroker.assign(index);
    }
}