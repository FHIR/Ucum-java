package org.fhir.ucum;





import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UcumCanonicalTest extends UcumServiceTest {


    @Test
    public void testGCanonical() throws FileNotFoundException, UcumException {
        Pair actual = ucumService.getCanonicalForm(new Pair(new Decimal(1), "g"));
        assertEquals("1 g", actual.toString());
    }
}
