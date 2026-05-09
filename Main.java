import java.util.Scanner;

/**
 * Main.java
 * Entry point for the COSC 214 healthcare data structures project.
 *
 * The program loads the CSV dataset, populates all four data structures,
 * then presents an interactive menu where the user can perform insert,
 * search, delete, and traverse operations on any structure.
 *
 * A benchmark section at startup measures the time taken by each
 * structure to load all records, helping compare their performance.
 *
 * Usage:
 *   javac *.java
 *   java Main healthcare_dataset.csv
 *
 * If no filename argument is supplied the program looks for
 * "healthcare_dataset.csv" in the current directory.
 */
public class Main {

    // ----- data structures -----
    private static DynamicArray        dynamicArray;
    private static LinkedList          linkedList;
    private static PatientHashMap      hashMap;
    private static PatientPriorityQueue priorityQueue;

    private static Scanner scanner = new Scanner(System.in);

    // ─────────────────────────────────────────────────────────────────────────
    public static void main(String[] args) {

        String csvFile = (args.length > 0) ? args[0] : "healthcare_dataset.csv";
        System.out.println("=================================================================");
        System.out.println("  COSC 214 -- Evaluating Data Structures with Healthcare Data   ");
        System.out.println("=================================================================");

        // ── Load dataset ──────────────────────────────────────────────────
        System.out.println("\nLoading dataset from: " + csvFile);
        PatientRecord[] allRecords = DataLoader.load(csvFile, 0);
        System.out.println("Records loaded: " + allRecords.length);

        if (allRecords.length == 0) {
            System.out.println("No records found. Please check the CSV file path.");
            return;
        }

        // ── Populate structures and benchmark ─────────────────────────────
        System.out.println("\n--- Benchmark: Inserting all records into each structure ---");
        benchmarkAndLoad(allRecords);

        // ── Interactive menu ──────────────────────────────────────────────
        mainMenu();

        System.out.println("\nThank you for using the Patient Record System. Goodbye.");
        scanner.close();
    }

    // ─── benchmark load ───────────────────────────────────────────────────
    private static void benchmarkAndLoad(PatientRecord[] records) {
        long start, end;

        // Dynamic Array
        dynamicArray = new DynamicArray();
        start = System.nanoTime();
        for (PatientRecord r : records) dynamicArray.insertRecord(r);
        end = System.nanoTime();
        System.out.printf("  DynamicArray      : %,d records in %6.2f ms%n",
                          dynamicArray.getSize(), (end - start) / 1_000_000.0);

        // Linked List
        linkedList = new LinkedList();
        start = System.nanoTime();
        for (PatientRecord r : records) linkedList.insertRecord(r);
        end = System.nanoTime();
        System.out.printf("  LinkedList        : %,d records in %6.2f ms%n",
                          linkedList.getSize(), (end - start) / 1_000_000.0);

        // HashMap
        hashMap = new PatientHashMap();
        start = System.nanoTime();
        for (PatientRecord r : records) hashMap.insertRecord(r);
        end = System.nanoTime();
        System.out.printf("  PatientHashMap    : %,d records in %6.2f ms%n",
                          hashMap.getSize(), (end - start) / 1_000_000.0);

        // Priority Queue
        priorityQueue = new PatientPriorityQueue();
        start = System.nanoTime();
        for (PatientRecord r : records) priorityQueue.insertRecord(r);
        end = System.nanoTime();
        System.out.printf("  PriorityQueue     : %,d records in %6.2f ms%n",
                          priorityQueue.getSize(), (end - start) / 1_000_000.0);

        System.out.println();

        // Run search benchmark for the first valid ID in each structure.
        if (records.length > 0) {
            int sampleId = records[records.length / 2].getId();
            System.out.println("--- Benchmark: Search for ID " + sampleId + " in each structure ---");

            start = System.nanoTime();
            PatientRecord r1 = dynamicArray.searchRecord(sampleId);
            end   = System.nanoTime();
            System.out.printf("  DynamicArray search   : %s in %.0f ns%n",
                              r1 != null ? "found" : "not found", (double)(end - start));

            start = System.nanoTime();
            PatientRecord r2 = linkedList.searchRecord(sampleId);
            end   = System.nanoTime();
            System.out.printf("  LinkedList search     : %s in %.0f ns%n",
                              r2 != null ? "found" : "not found", (double)(end - start));

            start = System.nanoTime();
            PatientRecord r3 = hashMap.searchRecord(sampleId);
            end   = System.nanoTime();
            System.out.printf("  PatientHashMap search : %s in %.0f ns%n",
                              r3 != null ? "found" : "not found", (double)(end - start));

            start = System.nanoTime();
            PatientRecord r4 = priorityQueue.searchRecord(sampleId);
            end   = System.nanoTime();
            System.out.printf("  PriorityQueue search  : %s in %.0f ns%n",
                              r4 != null ? "found" : "not found", (double)(end - start));
        }
    }

    // ─── main menu ────────────────────────────────────────────────────────
    private static void mainMenu() {
        while (true) {
            System.out.println("\n=================================================================");
            System.out.println("  MAIN MENU -- Select a Data Structure");
            System.out.println("=================================================================");
            System.out.println("  1. Dynamic Array");
            System.out.println("  2. Linked List");
            System.out.println("  3. Hash Map");
            System.out.println("  4. Priority Queue");
            System.out.println("  5. Run Full Benchmark Report");
            System.out.println("  0. Exit");
            System.out.print("  Your choice: ");

            int choice = readInt();
            switch (choice) {
                case 1: operationsMenu("Dynamic Array",   1); break;
                case 2: operationsMenu("Linked List",     2); break;
                case 3: operationsMenu("Hash Map",        3); break;
                case 4: operationsMenu("Priority Queue",  4); break;
                case 5: fullBenchmarkReport();                break;
                case 0: return;
                default: System.out.println("  Invalid option. Please try again.");
            }
        }
    }

    // ─── operations sub-menu ──────────────────────────────────────────────
    private static void operationsMenu(String structureName, int structureCode) {
        while (true) {
            System.out.println("\n--- " + structureName + " ---");
            System.out.println("  1. Insert a new record");
            System.out.println("  2. Search by ID");
            System.out.println("  3. Delete by ID");
            System.out.println("  4. Traverse all records");
            if (structureCode == 4) {
                System.out.println("  5. Process admission queue (dequeue all)");
            }
            System.out.println("  0. Back to main menu");
            System.out.print("  Your choice: ");

            int choice = readInt();
            if (choice == 0) return;

            switch (choice) {
                case 1: doInsert(structureCode);   break;
                case 2: doSearch(structureCode);   break;
                case 3: doDelete(structureCode);   break;
                case 4: doTraverse(structureCode); break;
                case 5:
                    if (structureCode == 4) doProcessQueue();
                    else System.out.println("  Invalid option.");
                    break;
                default: System.out.println("  Invalid option.");
            }
        }
    }

    // ─── operation handlers ───────────────────────────────────────────────
    private static void doInsert(int code) {
        System.out.println("\n  Enter record details:");
        System.out.print("  Age              : "); int    age       = readInt();
        System.out.print("  Gender           : "); String gender    = scanner.nextLine().trim();
        System.out.print("  Medical Condition: "); String condition = scanner.nextLine().trim();
        System.out.print("  Hospital         : "); String hospital  = scanner.nextLine().trim();
        System.out.print("  Admission Type   : "); String admType   = scanner.nextLine().trim();
        System.out.print("  Billing Amount   : "); double billing   = readDouble();

        int id = generateNextId(code);
        PatientRecord record = new PatientRecord(id, age, gender, condition, hospital, admType, billing);

        switch (code) {
            case 1: dynamicArray.insertRecord(record);  break;
            case 2: linkedList.insertRecord(record);    break;
            case 3: hashMap.insertRecord(record);       break;
            case 4: priorityQueue.insertRecord(record); break;
        }
        System.out.println("  Record inserted with ID " + id + ".");
    }

    private static void doSearch(int code) {
        System.out.print("\n  Enter patient ID to search: ");
        int id = readInt();
        PatientRecord result = null;
        long start = System.nanoTime();
        switch (code) {
            case 1: result = dynamicArray.searchRecord(id);  break;
            case 2: result = linkedList.searchRecord(id);    break;
            case 3: result = hashMap.searchRecord(id);       break;
            case 4: result = priorityQueue.searchRecord(id); break;
        }
        long elapsed = System.nanoTime() - start;
        if (result != null) {
            System.out.println("  Found in " + elapsed + " ns:");
            System.out.println("  " + result);
        } else {
            System.out.println("  No record found for ID " + id + ".");
        }
    }

    private static void doDelete(int code) {
        System.out.print("\n  Enter patient ID to delete: ");
        int id = readInt();
        boolean removed = false;
        switch (code) {
            case 1: removed = dynamicArray.deleteRecord(id);  break;
            case 2: removed = linkedList.deleteRecord(id);    break;
            case 3: removed = hashMap.deleteRecord(id);       break;
            case 4: removed = priorityQueue.deleteRecord(id); break;
        }
        System.out.println(removed
            ? "  Record " + id + " deleted successfully."
            : "  No record found for ID " + id + ".");
    }

    private static void doTraverse(int code) {
        System.out.println("\n  All records in the structure:");
        switch (code) {
            case 1: dynamicArray.traverseRecords();  break;
            case 2: linkedList.traverseRecords();    break;
            case 3: hashMap.traverseRecords();       break;
            case 4: priorityQueue.traverseRecords(); break;
        }
    }

    private static void doProcessQueue() {
        System.out.println();
        priorityQueue.processAdmissionQueue();
    }

    // ─── full benchmark report ────────────────────────────────────────────
    private static void fullBenchmarkReport() {
        System.out.println("\n=================================================================");
        System.out.println("  FULL BENCHMARK REPORT");
        System.out.println("=================================================================");

        int[] testIds = { 1, 500, 2000, 5000, 9000 };
        System.out.printf("  %-22s %-12s %-12s %-12s %-12s%n",
                          "Search ID", "DynArray(ns)", "LinkedList(ns)", "HashMap(ns)", "PQueue(ns)");
        System.out.println("  " + "-".repeat(70));
        for (int id : testIds) {
            long t1, t2, t3, t4, s;

            s = System.nanoTime(); dynamicArray.searchRecord(id);   t1 = System.nanoTime() - s;
            s = System.nanoTime(); linkedList.searchRecord(id);     t2 = System.nanoTime() - s;
            s = System.nanoTime(); hashMap.searchRecord(id);        t3 = System.nanoTime() - s;
            s = System.nanoTime(); priorityQueue.searchRecord(id);  t4 = System.nanoTime() - s;

            System.out.printf("  %-22d %-12d %-12d %-12d %-12d%n", id, t1, t2, t3, t4);
        }
        System.out.println("\n  Note: Nanosecond measurements may vary between runs.");
        System.out.println("  The HashMap is expected to be fastest for search operations.");
        System.out.println("  The DynamicArray and LinkedList use linear scan (slower for large data).");
    }

    // ─── utility helpers ──────────────────────────────────────────────────
    private static int generateNextId(int code) {
        switch (code) {
            case 1: return dynamicArray.getSize() + 1;
            case 2: return linkedList.getSize()   + 1;
            case 3: return hashMap.getSize()      + 1;
            case 4: return priorityQueue.getSize()+ 1;
            default: return 1;
        }
    }

    private static int readInt() {
        try {
            String line = scanner.nextLine().trim();
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static double readDouble() {
        try {
            String line = scanner.nextLine().trim();
            return Double.parseDouble(line);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
