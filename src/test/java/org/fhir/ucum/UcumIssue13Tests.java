package org.fhir.ucum;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.Assert.assertThrows;



public class UcumIssue13Tests extends UcumServiceTest{

  @Test
  public void testGigaYear() throws IOException, UcumException {
    assertThrows(UcumException.class, () -> {
      ucumService.convert(new Decimal("0.1"), "Ga", "a");
    });
  }

}
