package org.fhir.ucum;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;


public class UcumIssue23Tests extends UcumServiceTest{

  @Test
  public void testGetUnits() throws IOException, UcumException {

    String c = ucumService.getCanonicalUnits("dB");
    assertEquals("", c); // unity
  }

}
