import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * DataLoader.java
 * Reads the healthcare CSV file and converts each row into a PatientRecord.
 * Returns an array of PatientRecord objects that other structures can consume.
 */
public class DataLoader {

    /**
     * Loads up to maxRecords rows from the given CSV file.
     * The first row is treated as a header and skipped.
     * IDs are auto-generated starting from 1 because the original dataset
     * does not include a dedicated numeric ID column.
     *
     * Expected CSV column order (0-based index):
     *   0  Name
     *   1  Age
     *   2  Gender
     *   3  Blood Type
     *   4  Medical Condition
     *   5  Date of Admission
     *   6  Doctor
     *   7  Hospital
     *   8  Insurance Provider
     *   9  Billing Amount
     *   10 Room Number
     *   11 Admission Type
     *   12 Discharge Date
     *   13 Medication
     *   14 Test Results
     *
     * Fields used in PatientRecord: Age, Gender, Medical Condition,
     *   Hospital, Admission Type, Billing Amount.
     *
     * @param filePath   path to the CSV file
     * @param maxRecords maximum number of records to load (0 means no limit)
     * @return array of loaded PatientRecord objects
     */
    public static PatientRecord[] load(String filePath, int maxRecords) {
        // First pass: count the lines so we know the array size.
        int lineCount = 0;
        try (BufferedReader counter = new BufferedReader(new FileReader(filePath))) {
            String line = counter.readLine(); // skip header
            while ((line = counter.readLine()) != null) {
                if (!line.trim().isEmpty()) lineCount++;
                if (maxRecords > 0 && lineCount >= maxRecords) break;
            }
        } catch (IOException e) {
            System.out.println("Error counting lines: " + e.getMessage());
            return new PatientRecord[0];
        }

        PatientRecord[] records = new PatientRecord[lineCount];
        int index = 0;
        int id    = 1;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // skip header row
            String line;
            while ((line = reader.readLine()) != null && index < lineCount) {
                if (line.trim().isEmpty()) continue;

                String[] parts = splitCSVLine(line);
                if (parts.length < 12) continue;  // skip malformed rows

                try {
                    int    age             = Integer.parseInt(parts[1].trim());
                    String gender          = parts[2].trim();
                    String medCondition    = parts[4].trim();
                    String hospital        = parts[7].trim();
                    double billing         = Double.parseDouble(parts[9].trim());
                    String admissionType   = parts[11].trim();

                    records[index] = new PatientRecord(
                        id, age, gender, medCondition, hospital, admissionType, billing
                    );
                    index++;
                    id++;
                } catch (NumberFormatException ignored) {
                    // Skip rows where numeric fields cannot be parsed.
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        // Return a trimmed array in case some rows were skipped.
        if (index < records.length) {
            PatientRecord[] trimmed = new PatientRecord[index];
            for (int i = 0; i < index; i++) trimmed[i] = records[i];
            return trimmed;
        }
        return records;
    }

    /**
     * Splits a single CSV line into fields.
     * Handles fields enclosed in double quotes (which may contain commas).
     */
    private static String[] splitCSVLine(String line) {
        // Simple approach: split on commas that are not inside quotes.
        String[] result = new String[16];
        int  fieldCount = 0;
        boolean inQuote = false;
        StringBuilder current = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuote = !inQuote;
            } else if (c == ',' && !inQuote) {
                if (fieldCount < result.length) {
                    result[fieldCount++] = current.toString();
                    current.setLength(0);
                }
            } else {
                current.append(c);
            }
        }
        if (fieldCount < result.length) {
            result[fieldCount++] = current.toString();
        }

        String[] trimmed = new String[fieldCount];
        for (int i = 0; i < fieldCount; i++) {
            trimmed[i] = result[i] == null ? "" : result[i];
        }
        return trimmed;
    }
}
