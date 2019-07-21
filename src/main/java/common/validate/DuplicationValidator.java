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

    @Override
    public boolean isValid(Object o) {
        System.out.println("Check dupe");
        FileData fileData = (FileData)o;
        String fileName = fileData.getFileName();
        String value = fileData.getValue();
        String standardizedValue = value.toLowerCase();
        List<String> histories = writeHistoryMap.get(fileData.getFileName());

        if(histories != null) {
            if(!histories.contains(standardizedValue)) {
                histories.add(standardizedValue);
                writeHistoryMap.put(fileName, histories);
            } else {
                return Boolean.FALSE;
            }
        } else {
            histories = new ArrayList<>();
            histories.add(standardizedValue);
            writeHistoryMap.put(fileName, histories);
        }

        return Boolean.TRUE;
    }
}
