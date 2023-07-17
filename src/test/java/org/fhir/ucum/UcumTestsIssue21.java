package org.fhir.ucum;

import java.io.IOException;

import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;

import org.junit.Assert;
import org.junit.Test;

public class UcumTestsIssue21 {

  @Test
  public void testMileConversion() throws IOException, UcumException {

    String fileName = "ucum-essence.xml";
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource(fileName).getFile());
    InputStream inputStream = new FileInputStream(file);
    UcumEssenceService ucumService = new UcumEssenceService(inputStream);
    
    Decimal m = ucumService.convert(new Decimal("1",15), "[mi_i]","m");
    Assert.assertEquals("1609", m.asDecimal()); // because UCUM value for cm is wrong precision
  }

}
