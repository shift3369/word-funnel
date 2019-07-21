package common.file;

/**
 * @author Haylie
 * @since 21/07/2019.
 */
public class FileData {
    private String value;
    private String fileName;

    public FileData(String value, String fileName) {
        this.value = value;
        this.fileName = fileName;
    }

    public String getValue() {
        return value;
    }

    public String getFileName() {
        return fileName;
    }
}
