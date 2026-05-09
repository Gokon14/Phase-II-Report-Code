/**
 * DynamicArray.java
 * A resizable array that grows automatically when capacity is reached.
 * Built entirely from scratch without using any Java collection classes.
 */
public class DynamicArray {

    private PatientRecord[] data;
    private int             size;
    private static final int INITIAL_CAPACITY = 16;

    // ----- constructor -----

    public DynamicArray() {
        data = new PatientRecord[INITIAL_CAPACITY];
        size = 0;
    }

    // ----- private helper -----

    /**
     * Doubles the internal array capacity when it is full.
     */
    private void resize() {
        int newCapacity = data.length * 2;
        PatientRecord[] larger = new PatientRecord[newCapacity];
        for (int i = 0; i < size; i++) {
            larger[i] = data[i];
        }
        data = larger;
    }

    // ----- public operations -----

    /**
     * Inserts a new record at the end of the array.
     * Resizes first if the array is already full.
     * Time complexity: O(1) amortised, O(n) on resize.
     */
    public void insertRecord(PatientRecord record) {
        if (size == data.length) {
            resize();
        }
        data[size] = record;
        size++;
    }

    /**
     * Searches for a record by patient ID using linear scan.
     * Returns the record if found, null otherwise.
     * Time complexity: O(n).
     */
    public PatientRecord searchRecord(int id) {
        for (int i = 0; i < size; i++) {
            if (data[i].getId() == id) {
                return data[i];
            }
        }
        return null;
    }

    /**
     * Deletes the record with the given ID.
     * Shifts all elements after the deleted position one place to the left.
     * Returns true if a record was removed, false if the ID was not found.
     * Time complexity: O(n).
     */
    public boolean deleteRecord(int id) {
        for (int i = 0; i < size; i++) {
            if (data[i].getId() == id) {
                for (int j = i; j < size - 1; j++) {
                    data[j] = data[j + 1];
                }
                data[size - 1] = null;
                size--;
                return true;
            }
        }
        return false;
    }

    /**
     * Prints every record currently stored in the array.
     * Time complexity: O(n).
     */
    public void traverseRecords() {
        if (size == 0) {
            System.out.println("  (array is empty)");
            return;
        }
        for (int i = 0; i < size; i++) {
            System.out.println("  [" + i + "] " + data[i]);
        }
    }

    public int getSize() { return size; }
}
