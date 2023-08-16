package org.fhir.ucum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;


public class UcumIssue22Tests extends UcumServiceTest{

  @Test
  public void testDegreeConversion() throws IOException, UcumException {

      Assertions.assertThrows(UcumException.class, () -> {
      ucumService.convert(new Decimal("100", 15), "Cel", "K");
    });

      Assertions.assertThrows(UcumException.class, () -> {
      ucumService.convert(new Decimal("100", 15), "[degF]", "Cel");
    });
  }



}
