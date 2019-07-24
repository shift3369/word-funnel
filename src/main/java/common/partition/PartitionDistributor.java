package common.partition;

/**
 * @author Haylie
 * @since 24/07/2019.
 */
public class PartitionDistributor {
    public PartitionDistributor() {}
    public int getDistributedPartitionIndex(String input, int partitionNumber) {
        String standardizedInput = input.toLowerCase();
        char firstChar = input.charAt(0);
        if(Character.isDigit(firstChar)) {
            return 0;
        }

        int hashedInput = standardizedInput.hashCode();
        return Math.abs(hashedInput) % partitionNumber;
    }

}
