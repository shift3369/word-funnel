package common.validate;

import common.file.FileData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Haylie
 * @since 24/07/2019.
 */
public class DuplicationValidatorTest {
    private DuplicationValidator dut = new DuplicationValidator();
    private FileData alreadyInput = new FileData("abcd", "a");

    @Before
    public void setUp() {
        dut.isValid(alreadyInput);
    }

    @Test
    public void 만약_file에_썼던적이_있으면_false를_반환한다() {
        //when
        Boolean actual = dut.isValid(alreadyInput);
        //then
        Assert.assertEquals(actual, false);
    }

    @Test
    public void 만약_file에_썼던적이_없으면_true를_반환한다() {
        //given
        String newInput = "bcde";
        FileData newFileData = new FileData(newInput, Character.toString(newInput.charAt(0)));
        //when
        Boolean actual = dut.isValid(newFileData);
        //then
        Assert.assertEquals(actual, true);
    }
}