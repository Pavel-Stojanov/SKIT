import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TemperatureAnalyzerTest {

    @Test
    public void testEmptyArray() {
        int[] temps = new int[]{};
        String result = TemperatureAnalyzer.analyzeTemperatures(temps);
        assertEquals("No warm days.", result);
    }

    @Test
    public void testInvalidTemperature() {
        int[] temps = new int[]{70};
        String result = TemperatureAnalyzer.analyzeTemperatures(temps);
        assertEquals("Invalid temperatures detected.", result);
    }

    @Test
    public void testAllWarmDays() {
        int[] temps = new int[]{25};
        String result = TemperatureAnalyzer.analyzeTemperatures(temps);
        assertEquals("All days were warm.", result);
    }

    @Test
    public void testSomeWarmDays() {
        int[] temps = new int[]{25, 15};
        String result = TemperatureAnalyzer.analyzeTemperatures(temps);
        assertEquals("Some days were warm.", result);
    }
}