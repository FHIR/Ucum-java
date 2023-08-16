package org.fhir.ucum;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UcumIssue23Tests extends UcumServiceTest{

  @Test
  public void testGetUnits() throws IOException, UcumException {

    String c = ucumService.getCanonicalUnits("dB");
    assertEquals("", c); // unity
  }

}
