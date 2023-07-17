package org.fhir.ucum;

import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;

import org.junit.Assert;
import org.junit.Test;

public class UcumTestsIssue6_7 {

  @Test
  public void testDecimalPrecision() throws IOException, UcumException {

      String fileName = "ucum-essence.xml";
      ClassLoader classLoader = getClass().getClassLoader();
      File file = new File(classLoader.getResource(fileName).getFile());

     InputStream inputStream = new FileInputStream(file);

      UcumEssenceService ucumService = new UcumEssenceService(inputStream);
      Decimal result = ucumService.convert(new Decimal("15"), "/min", "/h");
      Assert.assertEquals("900", result.asDecimal());
  }

  @Test
  public void testDecimalEquals() throws IOException, UcumException {

      Decimal dec1 = new Decimal(42);
      Decimal dec2 = new Decimal(42);
      Assert.assertEquals(dec1, dec2);
      

      dec1 = new Decimal("42.00");
      dec2 = new Decimal("42.00");
      Assert.assertEquals(dec1, dec2);
      
      

      dec1 = new Decimal("42.000");
      dec2 = new Decimal("42.00");
      Assert.assertNotEquals(dec1, dec2);
  }
}
