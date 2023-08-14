package org.fhir.ucum;



import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class UcumIssue6_7Tests extends UcumServiceTest{

  @Test
  public void testDecimalPrecision() throws IOException, UcumException {


      Decimal result = ucumService.convert(new Decimal("15"), "/min", "/h");
      assertEquals("900", result.asDecimal());
  }

  @Test
  public void testDecimalEquals() throws IOException, UcumException {

      Decimal dec1 = new Decimal(42);
      Decimal dec2 = new Decimal(42);
      assertEquals(dec1, dec2);
      

      dec1 = new Decimal("42.00");
      dec2 = new Decimal("42.00");
      assertEquals(dec1, dec2);
      
      

      dec1 = new Decimal("42.000");
      dec2 = new Decimal("42.00");
      assertNotEquals(dec1, dec2);
  }
}
