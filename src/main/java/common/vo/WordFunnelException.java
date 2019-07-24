package common.vo;

/**
 * @author Haylie
 * @since 20/07/2019.
 */
public class WordFunnelException extends RuntimeException{
    private WordFunnelException.ExceptionType exceptionType;

    public WordFunnelException.ExceptionType getExceptionType() {
        return exceptionType;
    }

    public WordFunnelException(String message) {
        super(message);
    }

    public WordFunnelException(String message, WordFunnelException.ExceptionType exceptionType) {
        super(message);
        this.exceptionType = exceptionType;
    }

    public enum ExceptionType {
        FILE_NOT_FOUND,
        PARTITION_ERROR
    }
}
