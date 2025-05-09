package org.fhir.ucum;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;


public class UcumIssue21Tests extends UcumServiceTest{

  @Test
  public void testMileConversion() throws IOException, UcumException {



    
    Decimal m = ucumService.convert(new Decimal("1",15), "[mi_i]","m");
    assertEquals("1609", m.asDecimal()); // because UCUM value for cm is wrong precision
  }

}
