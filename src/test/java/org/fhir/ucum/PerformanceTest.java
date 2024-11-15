package org.fhir.ucum;

import static java.util.stream.IntStream.range;

import org.junit.jupiter.api.Test;

public class PerformanceTest extends UcumServiceTest {

    final static String SOURCE_UNIT = "mol.m-3";
    final String TARGET_UNIT = "mmol/L";
    final static int PRECISION = 64;

    @Test
    void testPerformance() throws UcumException {

        final Decimal value = new Decimal("3.1415926535", PRECISION);

        range(0, 4000).forEach(i -> {
            try {
                ucumService.convert(value, SOURCE_UNIT, TARGET_UNIT);
            } catch (UcumException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
