package common.validate;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Haylie
 * @since 23/07/2019.
 */
public class WordValidatorTest {
    private WordValidator dut = new WordValidator("^[a-zA-Z0-9]+$");

    @Test
    public void 만약_매칭되는_단어가_있으면_true를_반환한다() {
        //given
        String validInput = "aBc123";
        //when
        Boolean actual = dut.isValid(validInput);
        //then
        Assert.assertEquals(actual, true);


    }

    @Test
    public void 매칭되는_단어가_없으면_false를_반환한다() {
        String invalidInput = "$fas!@";

        Boolean actual = dut.isValid(invalidInput);

        Assert.assertEquals(actual, false);
    }
}