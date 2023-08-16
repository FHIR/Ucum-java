package org.fhir.ucum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;





public class UcumIssue13Tests extends UcumServiceTest{

  @Test
  public void testGigaYear() throws IOException, UcumException {
      Assertions.assertThrows(UcumException.class, () -> {
      ucumService.convert(new Decimal("0.1"), "Ga", "a");
    });
  }

}
