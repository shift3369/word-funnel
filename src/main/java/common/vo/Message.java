package common.vo;

/**
 * @author Haylie
 * @since 20/07/2019.
 */
public class Message {
    private boolean eof;
    private String word;

    public Message(String word) {
        this.eof = Boolean.FALSE;
        this.word = word;
    }

    public Message(boolean eof) {
        this.eof = eof;
    }

    public String getWord() { return word;}

    public boolean isEof() {
        return eof;
    }
}
