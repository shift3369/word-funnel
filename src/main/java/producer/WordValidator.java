package producer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Haylie
 * @since 20/07/2019.
 */
public class WordValidator {
    private String regExp;
    public WordValidator(String regExp) {
        this.regExp = regExp;
    }

    public boolean isValid(String input) {
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }

}
