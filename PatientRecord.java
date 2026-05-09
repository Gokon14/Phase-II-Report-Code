/**
 * PatientRecord.java
 * Represents a single patient record loaded from the healthcare dataset.
 * Each row in the CSV file is converted into one PatientRecord object.
 */
public class PatientRecord {

    private int    id;
    private int    age;
    private String gender;
    private String medicalCondition;
    private String hospital;
    private String admissionType;   // "Emergency", "Urgent", or "Elective"
    private double billingAmount;

    // ----- constructor -----

    public PatientRecord(int id, int age, String gender,
                         String medicalCondition, String hospital,
                         String admissionType, double billingAmount) {
        this.id               = id;
        this.age              = age;
        this.gender           = gender;
        this.medicalCondition = medicalCondition;
        this.hospital         = hospital;
        this.admissionType    = admissionType;
        this.billingAmount    = billingAmount;
    }

    // ----- getters -----

    public int    getId()               { return id; }
    public int    getAge()              { return age; }
    public String getGender()           { return gender; }
    public String getMedicalCondition() { return medicalCondition; }
    public String getHospital()         { return hospital; }
    public String getAdmissionType()    { return admissionType; }
    public double getBillingAmount()    { return billingAmount; }

    /**
     * Returns a numeric priority for this record based on admission type.
     * Higher number means higher priority in the queue.
     *   Emergency -> 3
     *   Urgent    -> 2
     *   Elective  -> 1
     *   (anything else) -> 0
     */
    public int getPriority() {
        switch (admissionType.trim().toLowerCase()) {
            case "emergency": return 3;
            case "urgent":    return 2;
            case "elective":  return 1;
            default:          return 0;
        }
    }

    @Override
    public String toString() {
        return String.format(
            "ID=%-6d | Age=%-3d | Gender=%-7s | Condition=%-20s | Hospital=%-30s | Type=%-10s | Bill=$%.2f",
            id, age, gender, medicalCondition, hospital, admissionType, billingAmount
        );
    }
}
