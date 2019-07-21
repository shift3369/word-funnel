package common.validate;

import common.file.FileData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Haylie
 * @since 20/07/2019.
 */
public class DuplicationValidator extends Validator {
    private static Map<String, List<String>> writeHistoryMap = new HashMap<>();
    //TODO 1차적으로는 List contains로 하였지만, 성능을위해 Trie로 구현해보아야함.

    public DuplicationValidator() {}

    public boolean isValid(String fileName, String value) {
        List<String> histories = writeHistoryMap.get(fileName);

        if (!histories.contains(value)) {
            histories.add(value);
            writeHistoryMap.put(fileName, histories);
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    @Override
    public boolean isValid(Object o) {
        FileData fileData = (FileData)o;
        String fileName = fileData.getFileName();
        String value = fileData.getValue();
        List<String> histories = writeHistoryMap.get(fileData.getFileName());

        if(histories != null) {
            if(!histories.contains(value)) {
                histories.add(value);
                writeHistoryMap.put(fileName, histories);
            } else {
                return Boolean.FALSE;
            }
        } else {
            histories = new ArrayList<>();
            histories.add(value);
            writeHistoryMap.put(fileName, histories);
        }

        return Boolean.TRUE;
    }
}
