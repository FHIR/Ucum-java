package org.fhir.ucum;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.Assert.assertThrows;



public class UcumIssue22Tests extends UcumServiceTest{

  @Test
  public void testDegreeConversion() throws IOException, UcumException {

      assertThrows(UcumException.class, () -> {
      ucumService.convert(new Decimal("100", 15), "Cel", "K");
    });
    
    assertThrows(UcumException.class, () -> {
      ucumService.convert(new Decimal("100", 15), "[degF]", "Cel");
    });
  }



}
