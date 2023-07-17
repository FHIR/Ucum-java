package org.fhir.ucum;

import java.io.IOException;

import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;

import org.junit.Assert;
import org.junit.Test;

public class UcumTestsIssue23 {

  @Test
  public void testGetUnits() throws IOException, UcumException {

    String fileName = "ucum-essence.xml";
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource(fileName).getFile());
    InputStream inputStream = new FileInputStream(file);
    UcumEssenceService ucumService = new UcumEssenceService(inputStream);

    String c = ucumService.getCanonicalUnits("dB");
    Assert.assertEquals("", c); // unity 
  }

}
