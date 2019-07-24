package common.validate;

/**
 * @author Haylie
 * @since 21/07/2019.
 */
public interface Validator<T> {
    boolean isValid(T t);
}
