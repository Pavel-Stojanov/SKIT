public class DosageCalculator {
    /**
     * Calculates a personalized dosage based on age, weight, risk level, and allergy status.
     *
     * @param age        Patient's age in years
     * @param weight     Patient's weight in kilograms
     * @param isHighRisk Whether the patient is in a high-risk group
     * @param hasAllergy Whether the patient has a mild allergy to the medication
     * @return Calculated dosage in milligrams
     */

    public static double calculateDosage(int age, double weight, boolean isHighRisk, boolean hasAllergy) {

        if ((age > 65 || isHighRisk) && weight < 50 && !hasAllergy) {

            return weight * 0.8;

        } else {

            return weight * 1.2 + age * 0.1;

        }

    }
}