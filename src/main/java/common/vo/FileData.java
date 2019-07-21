package common.vo;

/**
 * @author Haylie
 * @since 21/07/2019.
 */
public class FileData {
    private Message message;
    private String absoluteFileName;

    public FileData() {}
    public FileData(Message message, String absoluteFileName) {
        this.message = message;
        this.absoluteFileName = absoluteFileName;
    }

    public Message getMessage() {
        return message;
    }

    public String getAbsoluteFileName() {
        return absoluteFileName;
    }
}
