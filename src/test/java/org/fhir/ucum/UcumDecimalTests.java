package org.fhir.ucum;



import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UcumDecimalTests {

  @Test
  public void testAsInteger() throws UcumException {
      String message = "Failed to round trip the integer ";

      assertEquals(0, new Decimal(0).asInteger(), message + "0");
      assertEquals(1, new Decimal(1).asInteger(), message + "1");
      assertEquals(2, new Decimal(2).asInteger(), message + "2");
      assertEquals(64, new Decimal(64).asInteger(), message + "64");
      assertEquals(Integer.MAX_VALUE, new Decimal(Integer.MAX_VALUE).asInteger(), String.format("%s%d", message, Integer.MAX_VALUE));
      assertEquals(-1, new Decimal(-1).asInteger(), message + "-1");
      assertEquals(-2, new Decimal(-2).asInteger(), message + "-2");
      assertEquals(-64, new Decimal(-64).asInteger(), message + "-64");
      assertEquals(Integer.MIN_VALUE, new Decimal(Integer.MIN_VALUE).asInteger(), String.format("%s%d", message, Integer.MIN_VALUE));
  }

  @Test
  public void testStringSupport() throws UcumException {
      String message = "decimal: ";
      assertEquals("1", new Decimal("1").toString(), message);
      assertEquals("1", new Decimal("1e0").asDecimal(), message);
      assertEquals("0", new Decimal("0").toString(), message);
      assertEquals("0", new Decimal("-0").toString(), message);
      assertEquals("0", new Decimal("0e0").asDecimal(), message);
      assertEquals("10", new Decimal("10").toString(), message);
      assertEquals("10", new Decimal("1.0e1").asDecimal(), message);
      assertEquals("99", new Decimal("99").toString(), message);
      assertEquals("99", new Decimal("9.9e1").asDecimal(), message);
      assertEquals("-1", new Decimal("-1").toString(), message);
      assertEquals("-1", new Decimal("-1e0").asDecimal(), message);
      assertEquals("-10", new Decimal("-10").toString(), message);
      assertEquals("-10", new Decimal("-1.0e1").asDecimal(), message);
      assertEquals("-99", new Decimal("-99").toString(), message);
      assertEquals("-99", new Decimal("-9.9e1").asDecimal(), message);
      assertEquals("1.1", new Decimal("1.1").toString(), message);
      assertEquals("1.1", new Decimal("1.1e0").asDecimal(), message);
      assertEquals("-1.1", new Decimal("-1.1").toString(), message);
      assertEquals("-1.1", new Decimal("-1.1e0").asDecimal(), message);
      assertEquals("11.1", new Decimal("11.1").toString(), message);
      assertEquals("11.1", new Decimal("1.11e1").asDecimal(), message);
      assertEquals("1.11", new Decimal("1.11").toString(), message);
      assertEquals("1.11", new Decimal("1.11e0").asDecimal(), message);
      assertEquals("1.111", new Decimal("1.111").toString(), message);
      assertEquals("1.111", new Decimal("1.111e0").asDecimal(), message);
      assertEquals("0.1", new Decimal("0.1").toString(), message);
      assertEquals("0.1", new Decimal("1e-1").asDecimal(), message);
      assertEquals("0.1", new Decimal("00.1").toString(), message);
      assertEquals("0.1", new Decimal(".1").toString(), message);
      assertEquals("1.0", new Decimal("1.0").toString(), message);
      assertEquals("1.0", new Decimal("1.0e0").asDecimal(), message);
      assertEquals("1.00", new Decimal("1.00").toString(), message);
      assertEquals("1.00", new Decimal("1.00e0").asDecimal(), message);
      assertEquals("1.000000000000000000000000000000000000000", new Decimal("1.000000000000000000000000000000000000000").toString(), message);
      assertEquals("1.000000000000000000000000000000000000000", new Decimal("1.000000000000000000000000000000000000000e0").asDecimal(), message);
      assertEquals("-11.1", new Decimal("-11.1").toString(), message);
      assertEquals("-11.1", new Decimal("-1.11e1").asDecimal(), message);
      assertEquals("-1.11", new Decimal("-1.11").toString(), message);
      assertEquals("-1.11", new Decimal("-1.11e0").asDecimal(), message);
      assertEquals("-1.111", new Decimal("-1.111").toString(), message);
      assertEquals("-1.111", new Decimal("-1.111e0").asDecimal(), message);
      assertEquals("-0.1", new Decimal("-0.1").toString(), message);
      assertEquals("-0.1", new Decimal("-1e-1").asDecimal(), message);
      assertEquals("-0.1", new Decimal("-00.1").toString(), message);
      assertEquals("-0.1", new Decimal("-.1").toString(), message);
      assertEquals("-1.0", new Decimal("-1.0").toString(), message);
      assertEquals("-1.0", new Decimal("-1.0e0").asDecimal(), message);
      assertEquals("-1.00", new Decimal("-1.00").toString(), message);
      assertEquals("-1.00", new Decimal("-1.00e0").asDecimal(), message);
      assertEquals("-1.000000000000000000000000000000000000000", new Decimal("-1.000000000000000000000000000000000000000").toString(), message);
      assertEquals("-1.000000000000000000000000000000000000000", new Decimal("-1.000000000000000000000000000000000000000e0").asDecimal(), message);
      assertEquals("0.0", new Decimal("0.0").toString(), message);
      assertEquals("0.0", new Decimal("0.0e0").asDecimal(), message);
      assertEquals("0.0000", new Decimal("0.0000").toString(), message);
      assertEquals("0.0000", new Decimal("0.0000e0").asDecimal(), message);
      assertEquals("0.100", new Decimal("0.100").toString(), message);
      assertEquals("0.100", new Decimal("1.00e-1").asDecimal(), message);
      assertEquals("100", new Decimal("100").toString(), message);
      assertEquals("100", new Decimal("1.00e2").asDecimal(), message);
      assertEquals("0.01", new Decimal("0.01").toString(), message);
      assertEquals("0.01", new Decimal("1e-2").asDecimal(), message);
      assertEquals("0.001", new Decimal("0.001").toString(), message);
      assertEquals("0.001", new Decimal("1e-3").asDecimal(), message);
      assertEquals("0.0001", new Decimal("0.0001").toString(), message);
      assertEquals("0.0001", new Decimal("1e-4").asDecimal(), message);
      assertEquals("0.0001", new Decimal("00.0001").toString(), message);
      assertEquals("0.0001", new Decimal("000.0001").toString(), message);
      assertEquals("-0.01", new Decimal("-0.01").toString(), message);
      assertEquals("-0.01", new Decimal("-1e-2").asDecimal(), message);
      assertEquals("10.01", new Decimal("10.01").toString(), message);
      assertEquals("10.01", new Decimal("1.001e1").asDecimal(), message);
      assertEquals("0.00001", new Decimal("0.00001").toString(), message);
      assertEquals("0.00001", new Decimal("1e-5").asDecimal(), message);
      assertEquals("0.000001", new Decimal("0.000001").toString(), message);
      assertEquals("0.000001", new Decimal("1e-6").asDecimal(), message);
      assertEquals("0.0000001", new Decimal("0.0000001").toString(), message);
      assertEquals("0.0000001", new Decimal("1e-7").asDecimal(), message);
      assertEquals("0.000000001", new Decimal("0.000000001").toString(), message);
      assertEquals("0.000000001", new Decimal("1e-9").asDecimal(), message);
      assertEquals("0.00000000001", new Decimal("0.00000000001").toString(), message);
      assertEquals("0.00000000001", new Decimal("1e-11").asDecimal(), message);
      assertEquals("0.0000000000001", new Decimal("0.0000000000001").toString(), message);
      assertEquals("0.0000000000001", new Decimal("1e-13").asDecimal(), message);
      assertEquals("0.000000000000001", new Decimal("0.000000000000001").toString(), message);
      assertEquals("0.000000000000001", new Decimal("1e-15").asDecimal(), message);
      assertEquals("0.00000000000000001", new Decimal("0.00000000000000001").toString(), message);
      assertEquals("0.00000000000000001", new Decimal("1e-17").asDecimal(), message);
      assertEquals("10.1", new Decimal("10.1").toString(), message);
      assertEquals("10.1", new Decimal("1.01e1").asDecimal(), message);
      assertEquals("100.1", new Decimal("100.1").toString(), message);
      assertEquals("100.1", new Decimal("1.001e2").asDecimal(), message);
      assertEquals("1000.1", new Decimal("1000.1").toString(), message);
      assertEquals("1000.1", new Decimal("1.0001e3").asDecimal(), message);
      assertEquals("10000.1", new Decimal("10000.1").toString(), message);
      assertEquals("10000.1", new Decimal("1.00001e4").asDecimal(), message);
      assertEquals("100000.1", new Decimal("100000.1").toString(), message);
      assertEquals("100000.1", new Decimal("1.000001e5").asDecimal(), message);
      assertEquals("1000000.1", new Decimal("1000000.1").toString(), message);
      assertEquals("1000000.1", new Decimal("1.0000001e6").asDecimal(), message);
      assertEquals("10000000.1", new Decimal("10000000.1").toString(), message);
      assertEquals("10000000.1", new Decimal("1.00000001e7").asDecimal(), message);
      assertEquals("100000000.1", new Decimal("100000000.1").toString(), message);
      assertEquals("100000000.1", new Decimal("1.000000001e8").asDecimal(), message);
      assertEquals("1000000000.1", new Decimal("1000000000.1").toString(), message);
      assertEquals("1000000000.1", new Decimal("1.0000000001e9").asDecimal(), message);
      assertEquals("10000000000.1", new Decimal("10000000000.1").toString(), message);
      assertEquals("10000000000.1", new Decimal("1.00000000001e10").asDecimal(), message);
      assertEquals("100000000000.1", new Decimal("100000000000.1").toString(), message);
      assertEquals("100000000000.1", new Decimal("1.000000000001e11").asDecimal(), message);
      assertEquals("1000000000000.1", new Decimal("1000000000000.1").toString(), message);
      assertEquals("1000000000000.1", new Decimal("1.0000000000001e12").asDecimal(), message);
      assertEquals("10000000000000.1", new Decimal("10000000000000.1").toString(), message);
      assertEquals("10000000000000.1", new Decimal("1.00000000000001e13").asDecimal(), message);
      assertEquals("100000000000000.1", new Decimal("100000000000000.1").toString(), message);
      assertEquals("100000000000000.1", new Decimal("1.000000000000001e14").asDecimal(), message);

      message = "scientific: ";
      assertEquals("1e0", new Decimal("1").asScientific(), message);
      assertEquals("0e0", new Decimal("0").asScientific(), message);
      assertEquals("0e0", new Decimal("-0").asScientific(), message);
      assertEquals("1.0e1", new Decimal("10").asScientific(), message);
      assertEquals("9.9e1", new Decimal("99").asScientific(), message);
      assertEquals("-1e0", new Decimal("-1").asScientific(), message);
      assertEquals("-1.0e1", new Decimal("-10").asScientific(), message);
      assertEquals("-9.9e1", new Decimal("-99").asScientific(), message);
      assertEquals("1.1e0", new Decimal("1.1").asScientific(), message);
      assertEquals("-1.1e0", new Decimal("-1.1").asScientific(), message);
      assertEquals("1.11e1", new Decimal("11.1").asScientific(), message);
      assertEquals("1.11e0", new Decimal("1.11").asScientific(), message);
      assertEquals("1.111e0", new Decimal("1.111").asScientific(), message);
      assertEquals("1e-1", new Decimal("0.1").asScientific(), message);
      assertEquals("1e-1", new Decimal("00.1").asScientific(), message);
      assertEquals("1e-1", new Decimal(".1").asScientific(), message);
      assertEquals("1.0e0", new Decimal("1.0").asScientific(), message);
      assertEquals("1.00e0", new Decimal("1.00").asScientific(), message);
      assertEquals("1.000000000000000000000000000000000000000e0", new Decimal("1.000000000000000000000000000000000000000").asScientific(), message);
      assertEquals("-1.11e1", new Decimal("-11.1").asScientific(), message);
      assertEquals("-1.11e0", new Decimal("-1.11").asScientific(), message);
      assertEquals("-1.111e0", new Decimal("-1.111").asScientific(), message);
      assertEquals("-1e-1", new Decimal("-0.1").asScientific(), message);
      assertEquals("-1e-1", new Decimal("-00.1").asScientific(), message);
      assertEquals("-1e-1", new Decimal("-.1").asScientific(), message);
      assertEquals("-1.0e0", new Decimal("-1.0").asScientific(), message);
      assertEquals("-1.00e0", new Decimal("-1.00").asScientific(), message);
      assertEquals("-1.000000000000000000000000000000000000000e0", new Decimal("-1.000000000000000000000000000000000000000").asScientific(), message);
      assertEquals("0.0e0", new Decimal("0.0").asScientific(), message);
      assertEquals("0.0000e0", new Decimal("0.0000").asScientific(), message);
      assertEquals("1.00e-1", new Decimal("0.100").asScientific(), message);
      assertEquals("1.00e2", new Decimal("100").asScientific(), message);
      assertEquals("1e-2", new Decimal("0.01").asScientific(), message);
      assertEquals("1e-3", new Decimal("0.001").asScientific(), message);
      assertEquals("1e-4", new Decimal("0.0001").asScientific(), message);
      assertEquals("1e-4", new Decimal("00.0001").asScientific(), message);
      assertEquals("1e-4", new Decimal("000.0001").asScientific(), message);
      assertEquals("-1e-2", new Decimal("-0.01").asScientific(), message);
      assertEquals("1.001e1", new Decimal("10.01").asScientific(), message);
      assertEquals("1e-5", new Decimal("0.00001").asScientific(), message);
      assertEquals("1e-6", new Decimal("0.000001").asScientific(), message);
      assertEquals("1e-7", new Decimal("0.0000001").asScientific(), message);
      assertEquals("1e-9", new Decimal("0.000000001").asScientific(), message);
      assertEquals("1e-11", new Decimal("0.00000000001").asScientific(), message);
      assertEquals("1e-13", new Decimal("0.0000000000001").asScientific(), message);
      assertEquals("1e-15", new Decimal("0.000000000000001").asScientific(), message);
      assertEquals("1e-17", new Decimal("0.00000000000000001").asScientific(), message);
      assertEquals("1.01e1", new Decimal("10.1").asScientific(), message);
      assertEquals("1.001e2", new Decimal("100.1").asScientific(), message);
      assertEquals("1.0001e3", new Decimal("1000.1").asScientific(), message);
      assertEquals("1.00001e4", new Decimal("10000.1").asScientific(), message);
      assertEquals("1.000001e5", new Decimal("100000.1").asScientific(), message);
      assertEquals("1.0000001e6", new Decimal("1000000.1").asScientific(), message);
      assertEquals("1.00000001e7", new Decimal("10000000.1").asScientific(), message);
      assertEquals("1.000000001e8", new Decimal("100000000.1").asScientific(), message);
      assertEquals("1.0000000001e9", new Decimal("1000000000.1").asScientific(), message);
      assertEquals("1.00000000001e10", new Decimal("10000000000.1").asScientific(), message);
      assertEquals("1.000000000001e11", new Decimal("100000000000.1").asScientific(), message);
      assertEquals("1.0000000000001e12", new Decimal("1000000000000.1").asScientific(), message);
      assertEquals("1.00000000000001e13", new Decimal("10000000000000.1").asScientific(), message);
      assertEquals("1.000000000000001e14", new Decimal("100000000000000.1").asScientific(), message);

      //        testString("1e-3", "1e-3");   , "1e-3");  e0  }
  }

  @Test
  public void testTruncate() throws UcumException {
      String message = "wrong trunc - ";
      assertEquals("1", new Decimal("1").trunc().asDecimal(), message);
      assertEquals("1", new Decimal("1.01").trunc().asDecimal(), message);
      assertEquals("-1", new Decimal("-1.01").trunc().asDecimal(), message);
      assertEquals("0", new Decimal("0.01").trunc().asDecimal(), message);
      assertEquals("0", new Decimal("-0.01").trunc().asDecimal(), message);
      assertEquals("0", new Decimal("0.1").trunc().asDecimal(), message);
      assertEquals("0", new Decimal("0.0001").trunc().asDecimal(), message);
      assertEquals("100", new Decimal("100.000000000000000000000000000000000000000001").trunc().asDecimal(), message);
  }

  @Test
  public void testCompares() throws UcumException {
      int result =  new Decimal("1").comparesTo(new Decimal("1"));
      String message = getCompareMessage("1", "1", 0, result);
      assertEquals(0, result, message);
      result =  new Decimal("0").comparesTo(new Decimal("0"));
      message = getCompareMessage("0", "0", 0, result);
      assertEquals(0, result, message);
      result =  new Decimal("0").comparesTo(new Decimal("1"));
      message = getCompareMessage("0", "1", -1, result);
      assertEquals(-1, result, message);
      result =  new Decimal("1").comparesTo(new Decimal("0"));
      message = getCompareMessage("1", "0", 1, result);
      assertEquals(1, result, message);
      result =  new Decimal("10").comparesTo(new Decimal("10"));
      message = getCompareMessage("10", "10", 0, result);
      assertEquals(0, result, message);
      result =  new Decimal("100").comparesTo(new Decimal("100"));
      message = getCompareMessage("100", "100", 0, result);
      assertEquals(0, result, message);
      result =  new Decimal("0.1").comparesTo(new Decimal("0.1"));
      message = getCompareMessage("0.1", "0.1", 0, result);
      assertEquals(0, result, message);
      result =  new Decimal("0.01").comparesTo(new Decimal("0.01"));
      message = getCompareMessage("0.01", "0.01", 0, result);
      assertEquals(0, result, message);
      result =  new Decimal("0.01").comparesTo(new Decimal("0.0100"));
      message = getCompareMessage("0.01", "0.0100", 0, result);
      assertEquals(0, result, message);
      result =  new Decimal("1").comparesTo(new Decimal("1.00000000"));
      message = getCompareMessage("1", "1.00000000", 0, result);
      assertEquals(0, result, message);
      result =  new Decimal("1.111111").comparesTo(new Decimal("1.111111"));
      message = getCompareMessage("1.111111", "1.111111", 0, result);
      assertEquals(0, result, message);
  }

  @Test
  public void testAddition() throws UcumException {
      Decimal res = new Decimal("1").add(new Decimal("1"));
      String message = getAddSubMessage("1", "1", "2", "+", res);
      assertEquals("2", res.asDecimal(), message);
      res = new Decimal("0").add(new Decimal("1"));
      message = getAddSubMessage("0", "1", "1", "+", res);
      assertEquals("1", res.asDecimal(), message);
      res = new Decimal("0").add(new Decimal("0"));
      message = getAddSubMessage("0", "0", "0", "+", res);
      assertEquals("0", res.asDecimal(), message);
      res = new Decimal("5").add(new Decimal("5"));
      message = getAddSubMessage("5", "5", "10", "+", res);
      assertEquals("10", res.asDecimal(), message);
      res = new Decimal("10").add(new Decimal("1"));
      message = getAddSubMessage("10", "1", "11", "+", res);
      assertEquals("11", res.asDecimal(), message);
      res = new Decimal("11").add(new Decimal("12"));
      message = getAddSubMessage("11", "12", "23", "+", res);
      assertEquals("23", res.asDecimal(), message);
      res = new Decimal("15").add(new Decimal("16"));
      message = getAddSubMessage("15", "16", "31", "+", res);
      assertEquals("31", res.asDecimal(), message);
      res = new Decimal("150").add(new Decimal("160"));
      message = getAddSubMessage("150", "160", "310", "+", res);
      assertEquals("310", res.asDecimal(), message);
      res = new Decimal("153").add(new Decimal("168"));
      message = getAddSubMessage("153", "168", "321", "+", res);
      assertEquals("321", res.asDecimal(), message);
      res = new Decimal("15300000000000000000000000000000000001").add(new Decimal("1680"));
      message = getAddSubMessage("\"15300000000000000000000000000000000001", "1680", "15300000000000000000000000000000001681", "+", res);
      assertEquals("15300000000000000000000000000000001681", res.asDecimal(), message);
      res = new Decimal("1").add(new Decimal(".1"));
      message = getAddSubMessage("1", ".1", "1.1", "+", res);
      assertEquals("1.1", res.asDecimal(), message);
      res = new Decimal("1").add(new Decimal(".001"));
      message = getAddSubMessage("1", ".001", "1.001", "+", res);
      assertEquals("1.001", res.asDecimal(), message);
      res = new Decimal(".1").add(new Decimal(".1"));
      message = getAddSubMessage(".1", ".1", "0.2", "+", res);
      assertEquals("0.2", res.asDecimal(), message);
      res = new Decimal(".1").add(new Decimal(".01"));
      message = getAddSubMessage(".1", ".01", "0.11", "+", res);
      assertEquals("0.11", res.asDecimal(), message);
      res = new Decimal("5").add(new Decimal("6"));
      message = getAddSubMessage("5", "6", "11", "+", res);
      assertEquals("11", res.asDecimal(), message);
      res = new Decimal("5").add(new Decimal("-6"));
      message = getAddSubMessage("5", "-6", "-1", "+", res);
      assertEquals("-1", res.asDecimal(), message);
      res = new Decimal("-5").add(new Decimal("6"));
      message = getAddSubMessage("-5", "6", "1", "+", res);
      assertEquals("1", res.asDecimal(), message);
      res = new Decimal("-5").add(new Decimal("-6"));
      message = getAddSubMessage("-5", "-6", "-11", "+", res);
      assertEquals("-11", res.asDecimal(), message);
      res = new Decimal("2").add(new Decimal("0.001"));
      message = getAddSubMessage("2", "0.001", "2.001", "+", res);
      assertEquals("2.001", res.asDecimal(), message);
      res = new Decimal("2.0").add(new Decimal("0.001"));
      message = getAddSubMessage("2.0", "0.001", "2.001", "+", res);
      assertEquals("2.001", res.asDecimal(), message);
  }

  @Test
  public void testSubtract() throws UcumException {
      Decimal res = new Decimal("2").subtract(new Decimal("1"));
      String message = getAddSubMessage("2", "1", "1", "-", res);
      assertEquals("1", res.asDecimal(), message);
      res = new Decimal("2").subtract(new Decimal("0"));
      message = getAddSubMessage("2", "0", "2", "-", res);
      assertEquals("2", res.asDecimal(), message);
      res = new Decimal("0").subtract(new Decimal("0"));
      message = getAddSubMessage("0", "0", "0", "-", res);
      assertEquals("0", res.asDecimal(), message);
      res = new Decimal("0").subtract(new Decimal("2"));
      message = getAddSubMessage("0", "2", "-2", "-", res);
      assertEquals("-2", res.asDecimal(), message);
      res = new Decimal("2").subtract(new Decimal("2"));
      message = getAddSubMessage("2", "2", "0", "-", res);
      assertEquals("0", res.asDecimal(), message);
      res = new Decimal("1").subtract(new Decimal("2"));
      message = getAddSubMessage("1", "2", "-1", "-", res);
      assertEquals("-1", res.asDecimal(), message);
      res = new Decimal("20").subtract(new Decimal("1"));
      message = getAddSubMessage("20", "1", "19", "-", res);
      assertEquals("19", res.asDecimal(), message);
      res = new Decimal("2").subtract(new Decimal(".1"));
      message = getAddSubMessage("2", ".1", "1.9", "-", res);
      assertEquals("1.9", res.asDecimal(), message);
      res = new Decimal("2").subtract(new Decimal(".000001"));
      message = getAddSubMessage("2", ".000001", "1.999999", "-", res);
      assertEquals("1.999999", res.asDecimal(), message);
      res = new Decimal("2").subtract(new Decimal("2.000001"));
      message = getAddSubMessage("2", "2.000001", "-0.000001", "-", res);
      assertEquals("-0.000001", res.asDecimal(), message);
      res = new Decimal("3.5").subtract(new Decimal("35.5"));
      message = getAddSubMessage("3.5", "35.5", "-32.0", "-", res);
      assertEquals("-32.0", res.asDecimal(), message);
      res = new Decimal("5").subtract(new Decimal("6"));
      message = getAddSubMessage("5", "6", "-1", "-", res);
      assertEquals("-1", res.asDecimal(), message);
      res = new Decimal("6").subtract(new Decimal("5"));
      message = getAddSubMessage("6", "5", "1", "-", res);
      assertEquals("1", res.asDecimal(), message);
      res = new Decimal("5").subtract(new Decimal("-6"));
      message = getAddSubMessage("5", "-6", "11", "-", res);
      assertEquals("11", res.asDecimal(), message);
      res = new Decimal("6").subtract(new Decimal("-5"));
      message = getAddSubMessage("6", "-5", "11", "-", res);
      assertEquals("11", res.asDecimal(), message);
      res = new Decimal("-5").subtract(new Decimal("6"));
      message = getAddSubMessage("-5", "6", "-11", "-", res);
      assertEquals("-11", res.asDecimal(), message);
      res = new Decimal("-6").subtract(new Decimal("5"));
      message = getAddSubMessage("-6", "5", "-11", "-", res);
      assertEquals("-11", res.asDecimal(), message);
      res = new Decimal("-5").subtract(new Decimal("-6"));
      message = getAddSubMessage("-5", "-6", "1", "-", res);
      assertEquals("1", res.asDecimal(), message);
      res = new Decimal("-6").subtract(new Decimal("-5"));
      message = getAddSubMessage("-6", "-5", "-1", "-", res);
      assertEquals("-1", res.asDecimal(), message);
  }

  @Test
  public void testMultiply() throws UcumException {
      Decimal res = new Decimal("2").multiply(new Decimal("2"));
      String message = getAddSubMessage("2", "2", "4", "*", res);
      assertEquals("4", res.asDecimal(), message);
      res = new Decimal("2").multiply(new Decimal("0.5"));
      message = getAddSubMessage("2", "0.5", "1", "*", res);
      assertEquals("1", res.asDecimal(), message);
      res = new Decimal("0").multiply(new Decimal("0"));
      message = getAddSubMessage("0", "0", "0", "*", res);
      assertEquals("0", res.asDecimal(), message);
      res = new Decimal("0").multiply(new Decimal("1"));
      message = getAddSubMessage("0", "1", "0", "*", res);
      assertEquals("0", res.asDecimal(), message);
      res = new Decimal("4").multiply(new Decimal("4"));
      message = getAddSubMessage("4", "4", "16", "*", res);
      assertEquals("16", res.asDecimal(), message);
      res = new Decimal("20").multiply(new Decimal("20"));
      message = getAddSubMessage("20", "20", "400", "*", res);
      assertEquals("400", res.asDecimal(), message);
      res = new Decimal("200").multiply(new Decimal("20"));
      message = getAddSubMessage("200", "20", "4000", "*", res);
      assertEquals("4000", res.asDecimal(), message);
      res = new Decimal("400").multiply(new Decimal("400"));
      message = getAddSubMessage("400", "400", "160000", "*", res);
      assertEquals("160000", res.asDecimal(), message);
      res = new Decimal("2.0").multiply(new Decimal("2.0"));
      message = getAddSubMessage("2.0", "2.0", "4.0", "*", res);
      assertEquals("4.0", res.asDecimal(), message);
      res = new Decimal("2.00").multiply(new Decimal("2.0"));
      message = getAddSubMessage("2.00", "2.0", "4.0", "*", res);
      assertEquals("4.0", res.asDecimal(), message);
      res = new Decimal("2.0").multiply(new Decimal("0.2"));
      message = getAddSubMessage("2.0", "0.2", "0.4", "*", res);
      assertEquals("0.4", res.asDecimal(), message);
      res = new Decimal("2.0").multiply(new Decimal("0.20"));
      message = getAddSubMessage("2.0", "0.20", "0.40", "*", res);
      assertEquals("0.40", res.asDecimal(), message);
      res = new Decimal("13").multiply(new Decimal("13"));
      message = getAddSubMessage("13", "13", "169", "*", res);
      assertEquals("169", res.asDecimal(), message);
      res = new Decimal("12").multiply(new Decimal("89"));
      message = getAddSubMessage("12", "89", "1068", "*", res);
      assertEquals("1068", res.asDecimal(), message);
      res = new Decimal("1234").multiply(new Decimal("6789"));
      message = getAddSubMessage("1234", "6789", "8377626", "*", res);
      assertEquals("8377626", res.asDecimal(), message);
      res = new Decimal("10000").multiply(new Decimal("0.0001"));
      message = getAddSubMessage("10000", "0.0001", "1", "*", res);
      assertEquals("1", res.asDecimal(), message);
      res = new Decimal("10000").multiply(new Decimal("0.00010"));
      message = getAddSubMessage("10000", "0.00010", "1.0", "*", res);
      assertEquals("1.0", res.asDecimal(), message);
      res = new Decimal("10000").multiply(new Decimal("0.000100"));
      message = getAddSubMessage("10000", "0.000100", "1.00", "*", res);
      assertEquals("1.00", res.asDecimal(), message);
      res = new Decimal("10000").multiply(new Decimal("0.0001000"));
      message = getAddSubMessage("10000", "0.0001000", "1.000", "*", res);
      assertEquals("1.000", res.asDecimal(), message);
      res = new Decimal("10000").multiply(new Decimal("0.00010000"));
      message = getAddSubMessage("10000", "0.00010000", "1.0000", "*", res);
      assertEquals("1.0000", res.asDecimal(), message);
      res = new Decimal("10000").multiply(new Decimal("0.000100000"));
      message = getAddSubMessage("10000", "0.000100000", "1.00000", "*", res);
      assertEquals("1.00000", res.asDecimal(), message);
      res = new Decimal("10000.0").multiply(new Decimal("0.000100000"));
      message = getAddSubMessage("10000.0", "0.000100000", "1.00000", "*", res);
      assertEquals("1.00000", res.asDecimal(), message);
      res = new Decimal("10000.0").multiply(new Decimal("0.0001000000"));
      message = getAddSubMessage("10000.0", "0.0001000000", "1.00000", "*", res);
      assertEquals("1.00000", res.asDecimal(), message);
      res = new Decimal("10000.0").multiply(new Decimal("0.00010000000"));
      message = getAddSubMessage("10000.0", "0.00010000000", "1.00000", "*", res);
      assertEquals("1.00000", res.asDecimal(), message);
      res = new Decimal("2").multiply(new Decimal("-2"));
      message = getAddSubMessage("2", "-2", "-4", "*", res);
      assertEquals("-4", res.asDecimal(), message);
      res = new Decimal("-2").multiply(new Decimal("2"));
      message = getAddSubMessage("-2", "2", "-4", "*", res);
      assertEquals("-4", res.asDecimal(), message);
      res = new Decimal("-2").multiply(new Decimal("-2"));
      message = getAddSubMessage("-2", "-2", "4", "*", res);
      assertEquals("4", res.asDecimal(), message);
      res = new Decimal("35328734682734").multiply(new Decimal("2349834295876423"));
      message = getAddSubMessage("35328734682734", "2349834295876423", "83016672387407213199375780482", "*", res);
      assertEquals("83016672387407213199375780482", res.asDecimal(), message);
      res = new Decimal("35328734682734000000000").multiply(new Decimal("2349834295876423000000000"));
      message = getAddSubMessage("35328734682734000000000", "2349834295876423000000000", "83016672387407213199375780482000000000000000000", "*", res);
      assertEquals("83016672387407213199375780482000000000000000000", res.asDecimal(), message);
      res = new Decimal("3532873468.2734").multiply(new Decimal("23498342958.76423"));
      message = getAddSubMessage("3532873468.2734", "23498342958.76423", "83016672387407213199.375780482", "*", res);
      assertEquals("83016672387407213199.375780482", res.asDecimal(), message);
  }

  @Test
  public void testDivide() throws UcumException {
      Decimal res = new Decimal("500").divide(new Decimal("4"));
      String message = getAddSubMessage("500", "4", "125", "/", res);
      assertEquals("125", res.asDecimal(), message);
      res = new Decimal("1260257").divide(new Decimal("37"));
      message = getAddSubMessage("1260257", "37", "34061", "/", res);
      assertEquals("34061", res.asDecimal(), message);
      res = new Decimal("127").divide(new Decimal("4"));
      message = getAddSubMessage("127", "4", "31.75", "/", res);
      assertEquals("31.75", res.asDecimal(), message);
      res = new Decimal("10").divide(new Decimal("10"));
      message = getAddSubMessage("10", "10", "1", "/", res);
      assertEquals("1", res.asDecimal(), message);
      res = new Decimal("1").divide(new Decimal("1"));
      message = getAddSubMessage("1", "1", "1", "/", res);
      assertEquals("1", res.asDecimal(), message);
      res = new Decimal("10").divide(new Decimal("3"));
      message = getAddSubMessage("10", "3", "3.3", "/", res);
      assertEquals("3.3", res.asDecimal(), message);
      res = new Decimal("10.0").divide(new Decimal("3"));
      message = getAddSubMessage("10.0", "3", "3.33", "/", res);
      assertEquals("3.33", res.asDecimal(), message);
      res = new Decimal("10.00").divide(new Decimal("3"));
      message = getAddSubMessage("10.00", "3", "3.333", "/", res);
      assertEquals("3.333", res.asDecimal(), message);
      res = new Decimal("10.00").divide(new Decimal("3.0"));
      message = getAddSubMessage("10.00", "3.0", "3.3", "/", res);
      assertEquals("3.3", res.asDecimal(), message);
      res = new Decimal("100").divide(new Decimal("1"));
      message = getAddSubMessage("100", "1", "100", "/", res);
      assertEquals("100", res.asDecimal(), message);
      res = new Decimal("1000").divide(new Decimal("10"));
      message = getAddSubMessage("1000", "10", "100", "/", res);
      assertEquals("100", res.asDecimal(), message);
      res = new Decimal("100001").divide(new Decimal("10"));
      message = getAddSubMessage("100001", "10", "10000.1", "/", res);
      assertEquals("10000.1", res.asDecimal(), message);
      res = new Decimal("100").divide(new Decimal("10"));
      message = getAddSubMessage("100", "10", "10", "/", res);
      assertEquals("10", res.asDecimal(), message);
      res = new Decimal("1").divide(new Decimal("10"));
      message = getAddSubMessage("1", "10", "0.1", "/", res);
      assertEquals("0.1", res.asDecimal(), message);
      res = new Decimal("1").divide(new Decimal("15"));
      message = getAddSubMessage("1", "15", "0.067", "/", res);
      assertEquals("0.067", res.asDecimal(), message);
      res = new Decimal("1.0").divide(new Decimal("15"));
      message = getAddSubMessage("1.0", "15", "0.067", "/", res);
      assertEquals("0.067", res.asDecimal(), message);
      res = new Decimal("1.00").divide(new Decimal("15.0"));
      message = getAddSubMessage("1.00", "15.0", "0.0667", "/", res);
      assertEquals("0.0667", res.asDecimal(), message);
      res = new Decimal("1").divide(new Decimal("0.1"));
      message = getAddSubMessage("1", "0.1", "10", "/", res);
      assertEquals("10", res.asDecimal(), message);
      res = new Decimal("1").divide(new Decimal("0.10"));
      message = getAddSubMessage("1", "0.10", "10", "/", res);
      assertEquals("10", res.asDecimal(), message);
      res = new Decimal("1").divide(new Decimal("0.010"));
      message = getAddSubMessage("1", "0.010", "100", "/", res);
      assertEquals("100", res.asDecimal(), message);
      res = new Decimal("1").divide(new Decimal("1.5"));
      message = getAddSubMessage("1", "1.5", "0.67", "/", res);
      assertEquals("0.67", res.asDecimal(), message);
      res = new Decimal("1.0").divide(new Decimal("1.5"));
      message = getAddSubMessage("1.0", "1.5", "0.67", "/", res);
      assertEquals("0.67", res.asDecimal(), message);
      res = new Decimal("10").divide(new Decimal("1.5"));
      message = getAddSubMessage("10", "1.5", "6.7", "/", res);
      assertEquals("6.7", res.asDecimal(), message);
      res = new Decimal("-1").divide(new Decimal("1"));
      message = getAddSubMessage("-1", "1", "-1", "/", res);
      assertEquals("-1", res.asDecimal(), message);
      res = new Decimal("1").divide(new Decimal("-1"));
      message = getAddSubMessage("1", "-1", "-1", "/", res);
      assertEquals("-1", res.asDecimal(), message);
      res = new Decimal("-1").divide(new Decimal("-1"));
      message = getAddSubMessage("-1", "-1", "1", "/", res);
      assertEquals("1", res.asDecimal(), message);
      res = new Decimal("2").divide(new Decimal("2"));
      message = getAddSubMessage("2", "2", "1", "/", res);
      assertEquals("1", res.asDecimal(), message);
      res = new Decimal("20").divide(new Decimal("2"));
      message = getAddSubMessage("20", "2", "10", "/", res);
      assertEquals("10", res.asDecimal(), message);
      res = new Decimal("22").divide(new Decimal("2"));
      message = getAddSubMessage("22", "2", "11", "/", res);
      assertEquals("11", res.asDecimal(), message);
      res = new Decimal("83016672387407213199375780482").divide(new Decimal("2349834295876423"));
      message = getAddSubMessage("83016672387407213199375780482", "2349834295876423", "35328734682734", "/", res);
      assertEquals("35328734682734", res.asDecimal(), message);
      res = new Decimal("83016672387407213199375780482000000000000000000").divide(new Decimal("2349834295876423000000000"));
      message = getAddSubMessage("83016672387407213199375780482000000000000000000", "2349834295876423000000000", "35328734682734000000000", "/", res);
      assertEquals("35328734682734000000000", res.asDecimal(), message);
      res = new Decimal("83016672387407213199.375780482").divide(new Decimal("23498342958.76423"));
      message = getAddSubMessage("83016672387407213199.375780482", "23498342958.76423", "3532873468.2734", "/", res);
      assertEquals("3532873468.2734", res.asDecimal(), message);
  }

  @Test
  public void testIntegerDivide() throws UcumException {
      Decimal res = new Decimal("500").divInt(new Decimal("4"));
      String message = getAddSubMessage("500", "4", "125", "/(int)", res);
      assertEquals("125", res.asDecimal(), message);
      res = new Decimal("1260257").divInt(new Decimal("37"));
      message = getAddSubMessage("1260257", "37", "34061", "/(int)", res);
      assertEquals("34061", res.asDecimal(), message);
      res = new Decimal("127").divInt(new Decimal("4"));
      message = getAddSubMessage("127", "4", "31", "/(int)", res);
      assertEquals("31", res.asDecimal(), message);
      res = new Decimal("10").divInt(new Decimal("10"));
      message = getAddSubMessage("10", "10", "1", "/(int)", res);
      assertEquals("1", res.asDecimal(), message);
      res = new Decimal("1").divInt(new Decimal("1"));
      message = getAddSubMessage("1", "1", "1", "/(int)", res);
      assertEquals("1", res.asDecimal(), message);
      res = new Decimal("100").divInt(new Decimal("1"));
      message = getAddSubMessage("100", "1", "100", "/(int)", res);
      assertEquals("100", res.asDecimal(), message);
      res = new Decimal("1000").divInt(new Decimal("10"));
      message = getAddSubMessage("1000", "10", "100", "/(int)", res);
      assertEquals("100", res.asDecimal(), message);
      res = new Decimal("100001").divInt(new Decimal("10"));
      message = getAddSubMessage("100001", "10", "10000", "/(int)", res);
      assertEquals("10000", res.asDecimal(), message);
      res = new Decimal("1").divInt(new Decimal("1.5"));
      message = getAddSubMessage("1", "1.5", "0", "/(int)", res);
      assertEquals("0", res.asDecimal(), message);
      res = new Decimal("10").divInt(new Decimal("1.5"));
      message = getAddSubMessage("10", "1.5", "6", "/(int)", res);
      assertEquals("6", res.asDecimal(), message);
  }


  @Test
  public void testWholeNumberRounding() throws UcumException {
    testRounding("1", "1");  
    testRounding("0", "0");  
    testRounding("9", "9");  
    
    testRounding("100", "100");  
    testRounding("100.1", "100.1");  
    testRounding("100.0000000000000000001", "100");  
    testRounding("100.0000000000000000000", "100");  
    testRounding("100.0000010000000000000", "100.000001");  
    testRounding("100.0000000000000000010", "100.0000000000000000010");  
    testRounding("100.0000010000000000100", "100.0000010000000000100");  
    testRounding("100.0000010000000001000", "100.0000010000000001000");  
    testRounding("100.0000010000000010000", "100.000001000000001");  
    

    testRounding("99", "99");  
    testRounding("99.9", "99.9");  
    testRounding("199.9999999999999999999", "200");  
    testRounding("199.9999999999999999991", "200");  
    testRounding("167.9999999999999999991", "168");  
    testRounding("166.9999919999999999999", "166.999992");  

  }
  
  private void testRounding(String value, String expected) throws UcumException {
    Decimal dec = new Decimal(value);
    dec.checkForCouldBeWholeNumber();
    assertEquals(expected, dec.asDecimal());
  }

  @Test
  public void testModulo() throws UcumException {
      Decimal res = new Decimal("10").modulo(new Decimal("1"));
      String message = getAddSubMessage("10", "1", "0", "%", res);
      assertEquals("0", res.asDecimal(), message);
      res = new Decimal("7").modulo(new Decimal("4"));
      message = getAddSubMessage("7", "4", "3", "%%", res);
      assertEquals("3", res.asDecimal(), message);
  }

  private String getCompareMessage(String v1, String v2, int outcome, int result) {
    return String.format("Compare fail: %s.compares(%s) should be %d but was %d", v1, v2, outcome, result);
}

private String getAddSubMessage(String s1, String s2, String s3, String op, Decimal result) {
    return String.format("%s %s %s = %s, but the library returned %s", s1, op, s2, s3, result.asDecimal());
}

}
