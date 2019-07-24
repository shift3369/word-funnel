package common.validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Haylie
 * @since 20/07/2019.
 */
public class WordValidator implements Validator<String> {
    private String regExp;

    public WordValidator(String regExp) {
        this.regExp = regExp;
    }

    @Override
    public boolean isValid(String s) {
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(s);
        return matcher.find();
    }
}
