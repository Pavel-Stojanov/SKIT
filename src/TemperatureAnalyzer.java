public class TemperatureAnalyzer {
    /**
     * Analyzes an array of daily temperatures.
     * Returns a summary message based on:
     * - Number of warm days (>= 20°C)
     * - If any temperature is invalid (< -50 or > 60)
     * - If all days are warm
     *
     * @param temps an array of integers representing daily temperatures
     * @return summary message
     */
    public static String analyzeTemperatures(int[] temps) {
        boolean hasInvalid = false;
        int warmDays = 0;

        for (int i = 0; i < temps.length; i++) {
            int temp = temps[i];

            if (temp < -50 || temp > 60) {
                hasInvalid = true;
            } else if (temp >= 20) {
                warmDays++;
            }
        }

        if (hasInvalid) {
            return "Invalid temperatures detected.";
        } else if (warmDays == temps.length && temps.length > 0) {
            return "All days were warm.";
        } else if (warmDays == 0) {
            return "No warm days.";
        } else {
            return "Some days were warm.";
        }
    }
}
