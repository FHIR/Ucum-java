package org.fhir.ucum;

import java.io.FileInputStream;

import org.junit.Before;
import org.junit.Test;

public class UcumTestsIsssue10 {

  private UcumEssenceService ucumSvc;

  @Before
  public void setup() throws Exception {
    ucumSvc = new UcumEssenceService("src/main/resources/ucum-essence.xml");
    //ucumSvc = new UcumEssenceService(new FileInputStream("C:\\work\\org.hl7.fhir\\build\\tests\\ucum-essence.xml"));
  }

  @Test
  public void KgToPoundTest() throws UcumException {
    long start = System.currentTimeMillis();
    for (float i = 90.5f; i < 100f; i += 0.0001f) {
      //    float i = 90.7183f;
      Decimal decimal = new Decimal(Float.toString(i));
      float expectPound = i * 2.2046226218487758072297380134503f;
      Decimal actual = ucumSvc.convert(decimal, "kg", "[lb_av]");
      if (Math.abs(Float.parseFloat(actual.asDecimal()) - expectPound) > 0.001) {
        System.out.println(i + " actual:" + actual + " expected:" + expectPound);
      }
    }
    System.out.println("elapsed = "+describeDuration(System.currentTimeMillis() - start));
  }
  

  public static String describeDuration(long ms) {
    long days = ms / (1000 * 60 * 60 * 24);
    long hours = ms / (1000 * 60 * 60) % 24;
    long mins = ms / (1000 * 60) % 60;
    long secs = ms / (1000) % 60;
    ms = ms % 1000;
    if (days > 0) {
      return ""+days+"d "+hours+":"+mins+":"+secs+"."+ms;      
    } else {
      return ""+hours+":"+mins+":"+secs+"."+ms;
    }
    

  }
  
}
