/**
 * PatientHashMap.java
 * A hash-based structure that maps integer patient IDs to PatientRecord objects.
 * Uses separate chaining (linked buckets) to handle collisions.
 * Built entirely from scratch without using java.util.HashMap or any other
 * Java collection class.
 */
public class PatientHashMap {

    /**
     * A single entry in a bucket chain.
     */
    private static class Entry {
        int           key;
        PatientRecord value;
        Entry         next;

        Entry(int key, PatientRecord value) {
            this.key   = key;
            this.value = value;
            this.next  = null;
        }
    }

    private Entry[] buckets;
    private int     capacity;
    private int     size;
    private static final int    DEFAULT_CAPACITY   = 128;
    private static final double LOAD_FACTOR_LIMIT  = 0.75;

    // ----- constructor -----

    public PatientHashMap() {
        this(DEFAULT_CAPACITY);
    }

    public PatientHashMap(int capacity) {
        this.capacity = capacity;
        this.buckets  = new Entry[capacity];
        this.size     = 0;
    }

    // ----- private helpers -----

    /**
     * Computes the bucket index for a given key.
     * The modulo ensures the index stays within the array bounds.
     */
    private int hash(int key) {
        return Math.abs(key) % capacity;
    }

    /**
     * Rebuilds the table with double the capacity when the load factor is exceeded.
     * Every existing entry is re-hashed into the new bucket array.
     */
    private void rehash() {
        int     newCapacity = capacity * 2;
        Entry[] newBuckets  = new Entry[newCapacity];

        for (int i = 0; i < capacity; i++) {
            Entry entry = buckets[i];
            while (entry != null) {
                Entry next      = entry.next;
                int   newIndex  = Math.abs(entry.key) % newCapacity;
                entry.next      = newBuckets[newIndex];
                newBuckets[newIndex] = entry;
                entry = next;
            }
        }
        capacity = newCapacity;
        buckets  = newBuckets;
    }

    // ----- public operations -----

    /**
     * Inserts a record using its ID as the key.
     * If a record with the same ID already exists, it is replaced.
     * Rehashes the table if the load factor exceeds the limit.
     * Time complexity: O(1) average, O(n) worst case (all in one bucket).
     */
    public void insertRecord(PatientRecord record) {
        if ((double) size / capacity >= LOAD_FACTOR_LIMIT) {
            rehash();
        }
        int   index = hash(record.getId());
        Entry head  = buckets[index];

        // Check if a record with this ID already exists in the chain.
        Entry current = head;
        while (current != null) {
            if (current.key == record.getId()) {
                current.value = record;   // Replace the existing record.
                return;
            }
            current = current.next;
        }

        // Prepend a new entry to the chain (faster than appending).
        Entry newEntry   = new Entry(record.getId(), record);
        newEntry.next    = head;
        buckets[index]   = newEntry;
        size++;
    }

    /**
     * Returns the record with the given ID, or null if not found.
     * Time complexity: O(1) average.
     */
    public PatientRecord searchRecord(int id) {
        int   index   = hash(id);
        Entry current = buckets[index];
        while (current != null) {
            if (current.key == id) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    /**
     * Removes the record with the given ID from its bucket chain.
     * Returns true if the record was found and removed, false otherwise.
     * Time complexity: O(1) average.
     */
    public boolean deleteRecord(int id) {
        int   index    = hash(id);
        Entry current  = buckets[index];
        Entry previous = null;

        while (current != null) {
            if (current.key == id) {
                if (previous == null) {
                    buckets[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                size--;
                return true;
            }
            previous = current;
            current  = current.next;
        }
        return false;
    }

    /**
     * Iterates through every bucket and prints all entries.
     * Time complexity: O(capacity + size).
     */
    public void traverseRecords() {
        if (size == 0) {
            System.out.println("  (map is empty)");
            return;
        }
        int count = 0;
        for (int i = 0; i < capacity; i++) {
            Entry current = buckets[i];
            while (current != null) {
                System.out.println("  [bucket " + i + "] " + current.value);
                current = current.next;
                count++;
            }
        }
    }

    public int getSize()     { return size; }
    public int getCapacity() { return capacity; }
}
