import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DosageCalculatorTest {

    private static Stream<Arguments> provideRaccTestValues() {
        return Stream.of(
                // Row 5: a=T, b=F, c=T, d=T
                Arguments.of(5, 70, 40.0, false, true, 55.0),

                // Row 6: a=T, b=F, c=T, d=F
                Arguments.of(6, 70, 40.0, false, false, 32.0),

                // Row 8: a=T, b=F, c=F, d=F
                Arguments.of(8, 70, 60.0, false, false, 79.0),

                // Row 10: a=F, b=T, c=T, d=F
                Arguments.of(10, 60, 40.0, true, false, 32.0),

                // Row 14: a=F, b=F, c=T, d=F
                Arguments.of(14, 60, 40.0, false, false, 54.0)
        );
    }

    @ParameterizedTest(name = "Row {0}: age={1}, weight={2}, isHighRisk={3}, hasAllergy={4} -> expected={5}")
    @MethodSource("provideRaccTestValues")
    void testCalculateDosageRACC(int rowNum, int age, double weight, boolean isHighRisk, boolean hasAllergy, double expectedDosage) {
        double actualDosage = DosageCalculator.calculateDosage(age, weight, isHighRisk, hasAllergy);
        // Using a small delta for double comparison
        assertEquals(expectedDosage, actualDosage, 0.0001);
    }
}