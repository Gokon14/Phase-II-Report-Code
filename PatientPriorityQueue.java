/**
 * PatientPriorityQueue.java
 * A max-heap priority queue where patients with higher priority values
 * are dequeued first.
 *
 * Priority levels assigned via PatientRecord.getPriority():
 *   Emergency -> 3  (highest)
 *   Urgent    -> 2
 *   Elective  -> 1  (lowest)
 *
 * The heap is stored in a plain array.  Index relationships:
 *   parent of i  -> (i - 1) / 2
 *   left child   -> 2 * i + 1
 *   right child  -> 2 * i + 2
 *
 * Built entirely from scratch without using any Java collection classes.
 */
public class PatientPriorityQueue {

    private PatientRecord[] heap;
    private int             size;
    private static final int INITIAL_CAPACITY = 16;

    // ----- constructor -----

    public PatientPriorityQueue() {
        heap = new PatientRecord[INITIAL_CAPACITY];
        size = 0;
    }

    // ----- private helpers -----

    private void resize() {
        PatientRecord[] larger = new PatientRecord[heap.length * 2];
        for (int i = 0; i < size; i++) {
            larger[i] = heap[i];
        }
        heap = larger;
    }

    private void swap(int i, int j) {
        PatientRecord temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    /**
     * Moves the element at position i upward until the heap property is restored.
     * Called after every insertion.
     */
    private void heapifyUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (heap[i].getPriority() > heap[parent].getPriority()) {
                swap(i, parent);
                i = parent;
            } else {
                break;
            }
        }
    }

    /**
     * Moves the element at position i downward until the heap property is restored.
     * Called after every removal (dequeue).
     */
    private void heapifyDown(int i) {
        while (true) {
            int left    = 2 * i + 1;
            int right   = 2 * i + 2;
            int largest = i;

            if (left  < size && heap[left].getPriority()  > heap[largest].getPriority()) {
                largest = left;
            }
            if (right < size && heap[right].getPriority() > heap[largest].getPriority()) {
                largest = right;
            }
            if (largest != i) {
                swap(i, largest);
                i = largest;
            } else {
                break;
            }
        }
    }

    // ----- public operations -----

    /**
     * Inserts a record into the queue.
     * The record is placed at the end of the heap array and then
     * bubbled upward to its correct position.
     * Time complexity: O(log n).
     */
    public void insertRecord(PatientRecord record) {
        if (size == heap.length) {
            resize();
        }
        heap[size] = record;
        heapifyUp(size);
        size++;
    }

    /**
     * Removes and returns the record with the highest priority.
     * The last element is moved to the root and then pushed downward.
     * Returns null if the queue is empty.
     * Time complexity: O(log n).
     */
    public PatientRecord dequeue() {
        if (size == 0) {
            return null;
        }
        PatientRecord top = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        if (size > 0) {
            heapifyDown(0);
        }
        return top;
    }

    /**
     * Returns the highest priority record without removing it.
     * Time complexity: O(1).
     */
    public PatientRecord peek() {
        if (size == 0) return null;
        return heap[0];
    }

    /**
     * Searches the heap array linearly for a record by ID.
     * The heap is not sorted by ID, so a full scan is needed.
     * Time complexity: O(n).
     */
    public PatientRecord searchRecord(int id) {
        for (int i = 0; i < size; i++) {
            if (heap[i].getId() == id) {
                return heap[i];
            }
        }
        return null;
    }

    /**
     * Removes a record by ID from an arbitrary position in the heap.
     * After removal, the heap property is restored by running both
     * heapifyUp and heapifyDown on the moved element.
     * Returns true if the record was found and removed, false otherwise.
     * Time complexity: O(n) to find + O(log n) to fix heap.
     */
    public boolean deleteRecord(int id) {
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (heap[i].getId() == id) {
                index = i;
                break;
            }
        }
        if (index == -1) return false;

        heap[index] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        if (index < size) {
            heapifyUp(index);
            heapifyDown(index);
        }
        return true;
    }

    /**
     * Prints every record currently stored in the heap array.
     * Note that the order on screen is heap-array order, not priority order.
     * To process records in true priority order use dequeue() repeatedly.
     * Time complexity: O(n).
     */
    public void traverseRecords() {
        if (size == 0) {
            System.out.println("  (queue is empty)");
            return;
        }
        System.out.println("  (heap-array order -- not sorted by priority)");
        for (int i = 0; i < size; i++) {
            System.out.println("  [" + i + "] Priority=" + heap[i].getPriority()
                               + " | " + heap[i]);
        }
    }

    /**
     * Processes the entire queue from highest to lowest priority
     * by dequeuing all records one at a time.  The queue is emptied
     * by this operation.
     */
    public void processAdmissionQueue() {
        System.out.println("  Processing admission queue (highest priority first)...");
        int position = 1;
        PatientRecord next = dequeue();
        while (next != null) {
            System.out.println("  Position " + position + " | Priority="
                               + next.getPriority() + " | " + next);
            position++;
            next = dequeue();
        }
    }

    public int getSize() { return size; }
    public boolean isEmpty() { return size == 0; }
}
