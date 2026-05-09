/**
 * LinkedList.java
 * A singly linked list where each node holds one PatientRecord
 * and a reference to the next node.
 * Built entirely from scratch without using any Java collection classes.
 */
public class LinkedList {

    /**
     * Inner class representing a single node in the list.
     */
    private static class Node {
        PatientRecord record;
        Node          next;

        Node(PatientRecord record) {
            this.record = record;
            this.next   = null;
        }
    }

    private Node head;
    private int  size;

    // ----- constructor -----

    public LinkedList() {
        head = null;
        size = 0;
    }

    // ----- public operations -----

    /**
     * Appends a new record at the tail of the list.
     * Time complexity: O(n) to reach the tail, but O(1) if a tail pointer is added.
     * For simplicity we traverse to the end each time.
     */
    public void insertRecord(PatientRecord record) {
        Node newNode = new Node(record);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    /**
     * Searches for a record by ID by scanning from the head.
     * Returns the matching record, or null if not found.
     * Time complexity: O(n).
     */
    public PatientRecord searchRecord(int id) {
        Node current = head;
        while (current != null) {
            if (current.record.getId() == id) {
                return current.record;
            }
            current = current.next;
        }
        return null;
    }

    /**
     * Removes the node whose record matches the given ID.
     * Handles removal of the head node as a special case.
     * Returns true on success, false if the ID is not found.
     * Time complexity: O(n).
     */
    public boolean deleteRecord(int id) {
        if (head == null) {
            return false;
        }
        // Special case: the head node is the target.
        if (head.record.getId() == id) {
            head = head.next;
            size--;
            return true;
        }
        // General case: scan until we find the node just before the target.
        Node previous = head;
        Node current  = head.next;
        while (current != null) {
            if (current.record.getId() == id) {
                previous.next = current.next;
                size--;
                return true;
            }
            previous = current;
            current  = current.next;
        }
        return false;
    }

    /**
     * Visits every node from head to tail and prints each record.
     * Time complexity: O(n).
     */
    public void traverseRecords() {
        if (head == null) {
            System.out.println("  (list is empty)");
            return;
        }
        Node current = head;
        int  index   = 0;
        while (current != null) {
            System.out.println("  [" + index + "] " + current.record);
            current = current.next;
            index++;
        }
    }

    public int getSize() { return size; }
}
