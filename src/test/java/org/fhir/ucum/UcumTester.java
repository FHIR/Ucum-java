package org.fhir.ucum;

/*******************************************************************************
 * Crown Copyright (c) 2006 - 2014, Copyright (c) 2006 - 2014 Kestral Computing & Health Intersections P/L.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kestral Computing P/L - initial implementation
 *    Health Intersections P/L - port to java, ongoing maintenance
 *
 ******************************************************************************/

import org.junit.Assert;
import org.junit.Test;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class UcumTester {

    @Test
    public void testAsInteger() throws UcumException {
        String message = "Failed to round trip the integer ";

        Assert.assertEquals(message + "0", 0, new Decimal(0).asInteger());
        Assert.assertEquals(message + "1", 1, new Decimal(1).asInteger());
        Assert.assertEquals(message + "2", 2, new Decimal(2).asInteger());
        Assert.assertEquals(message + "64", 64, new Decimal(64).asInteger());
        Assert.assertEquals(String.format("%s%d", message, Integer.MAX_VALUE), Integer.MAX_VALUE, new Decimal(Integer.MAX_VALUE).asInteger());
        Assert.assertEquals(message + "-1", -1, new Decimal(-1).asInteger());
        Assert.assertEquals(message + "-2", -2, new Decimal(-2).asInteger());
        Assert.assertEquals(message + "-64", -64, new Decimal(-64).asInteger());
        Assert.assertEquals(String.format("%s%d", message, Integer.MIN_VALUE), Integer.MIN_VALUE, new Decimal(Integer.MIN_VALUE).asInteger());
    }

    @Test
    public void testStringSupport() throws UcumException {
        String message = "decimal: ";
        Assert.assertEquals(message, "1", new Decimal("1").toString());
        Assert.assertEquals(message, "1", new Decimal("1e0").asDecimal());
        Assert.assertEquals(message, "0", new Decimal("0").toString());
        Assert.assertEquals(message, "0", new Decimal("-0").toString());
        Assert.assertEquals(message, "0", new Decimal("0e0").asDecimal());
        Assert.assertEquals(message, "10", new Decimal("10").toString());
        Assert.assertEquals(message, "10", new Decimal("1.0e1").asDecimal());
        Assert.assertEquals(message, "99", new Decimal("99").toString());
        Assert.assertEquals(message, "99", new Decimal("9.9e1").asDecimal());
        Assert.assertEquals(message, "-1", new Decimal("-1").toString());
        Assert.assertEquals(message, "-1", new Decimal("-1e0").asDecimal());
        Assert.assertEquals(message, "-10", new Decimal("-10").toString());
        Assert.assertEquals(message, "-10", new Decimal("-1.0e1").asDecimal());
        Assert.assertEquals(message, "-99", new Decimal("-99").toString());
        Assert.assertEquals(message, "-99", new Decimal("-9.9e1").asDecimal());
        Assert.assertEquals(message, "1.1", new Decimal("1.1").toString());
        Assert.assertEquals(message, "1.1", new Decimal("1.1e0").asDecimal());
        Assert.assertEquals(message, "-1.1", new Decimal("-1.1").toString());
        Assert.assertEquals(message, "-1.1", new Decimal("-1.1e0").asDecimal());
        Assert.assertEquals(message, "11.1", new Decimal("11.1").toString());
        Assert.assertEquals(message, "11.1", new Decimal("1.11e1").asDecimal());
        Assert.assertEquals(message, "1.11", new Decimal("1.11").toString());
        Assert.assertEquals(message, "1.11", new Decimal("1.11e0").asDecimal());
        Assert.assertEquals(message, "1.111", new Decimal("1.111").toString());
        Assert.assertEquals(message, "1.111", new Decimal("1.111e0").asDecimal());
        Assert.assertEquals(message, "0.1", new Decimal("0.1").toString());
        Assert.assertEquals(message, "0.1", new Decimal("1e-1").asDecimal());
        Assert.assertEquals(message, "0.1", new Decimal("00.1").toString());
        Assert.assertEquals(message, "0.1", new Decimal(".1").toString());
        Assert.assertEquals(message, "1.0", new Decimal("1.0").toString());
        Assert.assertEquals(message, "1.0", new Decimal("1.0e0").asDecimal());
        Assert.assertEquals(message, "1.00", new Decimal("1.00").toString());
        Assert.assertEquals(message, "1.00", new Decimal("1.00e0").asDecimal());
        Assert.assertEquals(message, "1.000000000000000000000000000000000000000", new Decimal("1.000000000000000000000000000000000000000").toString());
        Assert.assertEquals(message, "1.000000000000000000000000000000000000000", new Decimal("1.000000000000000000000000000000000000000e0").asDecimal());
        Assert.assertEquals(message, "-11.1", new Decimal("-11.1").toString());
        Assert.assertEquals(message, "-11.1", new Decimal("-1.11e1").asDecimal());
        Assert.assertEquals(message, "-1.11", new Decimal("-1.11").toString());
        Assert.assertEquals(message, "-1.11", new Decimal("-1.11e0").asDecimal());
        Assert.assertEquals(message, "-1.111", new Decimal("-1.111").toString());
        Assert.assertEquals(message, "-1.111", new Decimal("-1.111e0").asDecimal());
        Assert.assertEquals(message, "-0.1", new Decimal("-0.1").toString());
        Assert.assertEquals(message, "-0.1", new Decimal("-1e-1").asDecimal());
        Assert.assertEquals(message, "-0.1", new Decimal("-00.1").toString());
        Assert.assertEquals(message, "-0.1", new Decimal("-.1").toString());
        Assert.assertEquals(message, "-1.0", new Decimal("-1.0").toString());
        Assert.assertEquals(message, "-1.0", new Decimal("-1.0e0").asDecimal());
        Assert.assertEquals(message, "-1.00", new Decimal("-1.00").toString());
        Assert.assertEquals(message, "-1.00", new Decimal("-1.00e0").asDecimal());
        Assert.assertEquals(message, "-1.000000000000000000000000000000000000000", new Decimal("-1.000000000000000000000000000000000000000").toString());
        Assert.assertEquals(message, "-1.000000000000000000000000000000000000000", new Decimal("-1.000000000000000000000000000000000000000e0").asDecimal());
        Assert.assertEquals(message, "0.0", new Decimal("0.0").toString());
        Assert.assertEquals(message, "0.0", new Decimal("0.0e0").asDecimal());
        Assert.assertEquals(message, "0.0000", new Decimal("0.0000").toString());
        Assert.assertEquals(message, "0.0000", new Decimal("0.0000e0").asDecimal());
        Assert.assertEquals(message, "0.100", new Decimal("0.100").toString());
        Assert.assertEquals(message, "0.100", new Decimal("1.00e-1").asDecimal());
        Assert.assertEquals(message, "100", new Decimal("100").toString());
        Assert.assertEquals(message, "100", new Decimal("1.00e2").asDecimal());
        Assert.assertEquals(message, "0.01", new Decimal("0.01").toString());
        Assert.assertEquals(message, "0.01", new Decimal("1e-2").asDecimal());
        Assert.assertEquals(message, "0.001", new Decimal("0.001").toString());
        Assert.assertEquals(message, "0.001", new Decimal("1e-3").asDecimal());
        Assert.assertEquals(message, "0.0001", new Decimal("0.0001").toString());
        Assert.assertEquals(message, "0.0001", new Decimal("1e-4").asDecimal());
        Assert.assertEquals(message, "0.0001", new Decimal("00.0001").toString());
        Assert.assertEquals(message, "0.0001", new Decimal("000.0001").toString());
        Assert.assertEquals(message, "-0.01", new Decimal("-0.01").toString());
        Assert.assertEquals(message, "-0.01", new Decimal("-1e-2").asDecimal());
        Assert.assertEquals(message, "10.01", new Decimal("10.01").toString());
        Assert.assertEquals(message, "10.01", new Decimal("1.001e1").asDecimal());
        Assert.assertEquals(message, "0.00001", new Decimal("0.00001").toString());
        Assert.assertEquals(message, "0.00001", new Decimal("1e-5").asDecimal());
        Assert.assertEquals(message, "0.000001", new Decimal("0.000001").toString());
        Assert.assertEquals(message, "0.000001", new Decimal("1e-6").asDecimal());
        Assert.assertEquals(message, "0.0000001", new Decimal("0.0000001").toString());
        Assert.assertEquals(message, "0.0000001", new Decimal("1e-7").asDecimal());
        Assert.assertEquals(message, "0.000000001", new Decimal("0.000000001").toString());
        Assert.assertEquals(message, "0.000000001", new Decimal("1e-9").asDecimal());
        Assert.assertEquals(message, "0.00000000001", new Decimal("0.00000000001").toString());
        Assert.assertEquals(message, "0.00000000001", new Decimal("1e-11").asDecimal());
        Assert.assertEquals(message, "0.0000000000001", new Decimal("0.0000000000001").toString());
        Assert.assertEquals(message, "0.0000000000001", new Decimal("1e-13").asDecimal());
        Assert.assertEquals(message, "0.000000000000001", new Decimal("0.000000000000001").toString());
        Assert.assertEquals(message, "0.000000000000001", new Decimal("1e-15").asDecimal());
        Assert.assertEquals(message, "0.00000000000000001", new Decimal("0.00000000000000001").toString());
        Assert.assertEquals(message, "0.00000000000000001", new Decimal("1e-17").asDecimal());
        Assert.assertEquals(message, "10.1", new Decimal("10.1").toString());
        Assert.assertEquals(message, "10.1", new Decimal("1.01e1").asDecimal());
        Assert.assertEquals(message, "100.1", new Decimal("100.1").toString());
        Assert.assertEquals(message, "100.1", new Decimal("1.001e2").asDecimal());
        Assert.assertEquals(message, "1000.1", new Decimal("1000.1").toString());
        Assert.assertEquals(message, "1000.1", new Decimal("1.0001e3").asDecimal());
        Assert.assertEquals(message, "10000.1", new Decimal("10000.1").toString());
        Assert.assertEquals(message, "10000.1", new Decimal("1.00001e4").asDecimal());
        Assert.assertEquals(message, "100000.1", new Decimal("100000.1").toString());
        Assert.assertEquals(message, "100000.1", new Decimal("1.000001e5").asDecimal());
        Assert.assertEquals(message, "1000000.1", new Decimal("1000000.1").toString());
        Assert.assertEquals(message, "1000000.1", new Decimal("1.0000001e6").asDecimal());
        Assert.assertEquals(message, "10000000.1", new Decimal("10000000.1").toString());
        Assert.assertEquals(message, "10000000.1", new Decimal("1.00000001e7").asDecimal());
        Assert.assertEquals(message, "100000000.1", new Decimal("100000000.1").toString());
        Assert.assertEquals(message, "100000000.1", new Decimal("1.000000001e8").asDecimal());
        Assert.assertEquals(message, "1000000000.1", new Decimal("1000000000.1").toString());
        Assert.assertEquals(message, "1000000000.1", new Decimal("1.0000000001e9").asDecimal());
        Assert.assertEquals(message, "10000000000.1", new Decimal("10000000000.1").toString());
        Assert.assertEquals(message, "10000000000.1", new Decimal("1.00000000001e10").asDecimal());
        Assert.assertEquals(message, "100000000000.1", new Decimal("100000000000.1").toString());
        Assert.assertEquals(message, "100000000000.1", new Decimal("1.000000000001e11").asDecimal());
        Assert.assertEquals(message, "1000000000000.1", new Decimal("1000000000000.1").toString());
        Assert.assertEquals(message, "1000000000000.1", new Decimal("1.0000000000001e12").asDecimal());
        Assert.assertEquals(message, "10000000000000.1", new Decimal("10000000000000.1").toString());
        Assert.assertEquals(message, "10000000000000.1", new Decimal("1.00000000000001e13").asDecimal());
        Assert.assertEquals(message, "100000000000000.1", new Decimal("100000000000000.1").toString());
        Assert.assertEquals(message, "100000000000000.1", new Decimal("1.000000000000001e14").asDecimal());

        message = "scientific: ";
        Assert.assertEquals(message, "1e0", new Decimal("1").asScientific());
        Assert.assertEquals(message, "0e0", new Decimal("0").asScientific());
        Assert.assertEquals(message, "0e0", new Decimal("-0").asScientific());
        Assert.assertEquals(message, "1.0e1", new Decimal("10").asScientific());
        Assert.assertEquals(message, "9.9e1", new Decimal("99").asScientific());
        Assert.assertEquals(message, "-1e0", new Decimal("-1").asScientific());
        Assert.assertEquals(message, "-1.0e1", new Decimal("-10").asScientific());
        Assert.assertEquals(message, "-9.9e1", new Decimal("-99").asScientific());
        Assert.assertEquals(message, "1.1e0", new Decimal("1.1").asScientific());
        Assert.assertEquals(message, "-1.1e0", new Decimal("-1.1").asScientific());
        Assert.assertEquals(message, "1.11e1", new Decimal("11.1").asScientific());
        Assert.assertEquals(message, "1.11e0", new Decimal("1.11").asScientific());
        Assert.assertEquals(message, "1.111e0", new Decimal("1.111").asScientific());
        Assert.assertEquals(message, "1e-1", new Decimal("0.1").asScientific());
        Assert.assertEquals(message, "1e-1", new Decimal("00.1").asScientific());
        Assert.assertEquals(message, "1e-1", new Decimal(".1").asScientific());
        Assert.assertEquals(message, "1.0e0", new Decimal("1.0").asScientific());
        Assert.assertEquals(message, "1.00e0", new Decimal("1.00").asScientific());
        Assert.assertEquals(message, "1.000000000000000000000000000000000000000e0", new Decimal("1.000000000000000000000000000000000000000").asScientific());
        Assert.assertEquals(message, "-1.11e1", new Decimal("-11.1").asScientific());
        Assert.assertEquals(message, "-1.11e0", new Decimal("-1.11").asScientific());
        Assert.assertEquals(message, "-1.111e0", new Decimal("-1.111").asScientific());
        Assert.assertEquals(message, "-1e-1", new Decimal("-0.1").asScientific());
        Assert.assertEquals(message, "-1e-1", new Decimal("-00.1").asScientific());
        Assert.assertEquals(message, "-1e-1", new Decimal("-.1").asScientific());
        Assert.assertEquals(message, "-1.0e0", new Decimal("-1.0").asScientific());
        Assert.assertEquals(message, "-1.00e0", new Decimal("-1.00").asScientific());
        Assert.assertEquals(message, "-1.000000000000000000000000000000000000000e0", new Decimal("-1.000000000000000000000000000000000000000").asScientific());
        Assert.assertEquals(message, "0.0e0", new Decimal("0.0").asScientific());
        Assert.assertEquals(message, "0.0000e0", new Decimal("0.0000").asScientific());
        Assert.assertEquals(message, "1.00e-1", new Decimal("0.100").asScientific());
        Assert.assertEquals(message, "1.00e2", new Decimal("100").asScientific());
        Assert.assertEquals(message, "1e-2", new Decimal("0.01").asScientific());
        Assert.assertEquals(message, "1e-3", new Decimal("0.001").asScientific());
        Assert.assertEquals(message, "1e-4", new Decimal("0.0001").asScientific());
        Assert.assertEquals(message, "1e-4", new Decimal("00.0001").asScientific());
        Assert.assertEquals(message, "1e-4", new Decimal("000.0001").asScientific());
        Assert.assertEquals(message, "-1e-2", new Decimal("-0.01").asScientific());
        Assert.assertEquals(message, "1.001e1", new Decimal("10.01").asScientific());
        Assert.assertEquals(message, "1e-5", new Decimal("0.00001").asScientific());
        Assert.assertEquals(message, "1e-6", new Decimal("0.000001").asScientific());
        Assert.assertEquals(message, "1e-7", new Decimal("0.0000001").asScientific());
        Assert.assertEquals(message, "1e-9", new Decimal("0.000000001").asScientific());
        Assert.assertEquals(message, "1e-11", new Decimal("0.00000000001").asScientific());
        Assert.assertEquals(message, "1e-13", new Decimal("0.0000000000001").asScientific());
        Assert.assertEquals(message, "1e-15", new Decimal("0.000000000000001").asScientific());
        Assert.assertEquals(message, "1e-17", new Decimal("0.00000000000000001").asScientific());
        Assert.assertEquals(message, "1.01e1", new Decimal("10.1").asScientific());
        Assert.assertEquals(message, "1.001e2", new Decimal("100.1").asScientific());
        Assert.assertEquals(message, "1.0001e3", new Decimal("1000.1").asScientific());
        Assert.assertEquals(message, "1.00001e4", new Decimal("10000.1").asScientific());
        Assert.assertEquals(message, "1.000001e5", new Decimal("100000.1").asScientific());
        Assert.assertEquals(message, "1.0000001e6", new Decimal("1000000.1").asScientific());
        Assert.assertEquals(message, "1.00000001e7", new Decimal("10000000.1").asScientific());
        Assert.assertEquals(message, "1.000000001e8", new Decimal("100000000.1").asScientific());
        Assert.assertEquals(message, "1.0000000001e9", new Decimal("1000000000.1").asScientific());
        Assert.assertEquals(message, "1.00000000001e10", new Decimal("10000000000.1").asScientific());
        Assert.assertEquals(message, "1.000000000001e11", new Decimal("100000000000.1").asScientific());
        Assert.assertEquals(message, "1.0000000000001e12", new Decimal("1000000000000.1").asScientific());
        Assert.assertEquals(message, "1.00000000000001e13", new Decimal("10000000000000.1").asScientific());
        Assert.assertEquals(message, "1.000000000000001e14", new Decimal("100000000000000.1").asScientific());

        //        testString("1e-3", "1e-3");   , "1e-3");  e0  }
    }

    @Test
    public void testTruncate() throws UcumException {
        String message = "wrong trunc - ";
        Assert.assertEquals(message, "1", new Decimal("1").trunc().asDecimal());
        Assert.assertEquals(message, "1", new Decimal("1.01").trunc().asDecimal());
        Assert.assertEquals(message, "-1", new Decimal("-1.01").trunc().asDecimal());
        Assert.assertEquals(message, "0", new Decimal("0.01").trunc().asDecimal());
        Assert.assertEquals(message, "0", new Decimal("-0.01").trunc().asDecimal());
        Assert.assertEquals(message, "0", new Decimal("0.1").trunc().asDecimal());
        Assert.assertEquals(message, "0", new Decimal("0.0001").trunc().asDecimal());
        Assert.assertEquals(message, "100", new Decimal("100.000000000000000000000000000000000000000001").trunc().asDecimal());
    }

    @Test
    public void testCompares() throws UcumException {
        int result = new Decimal("1").comparesTo(new Decimal("1"));
        String message = getCompareMessage("1", "1", 0, result);
        Assert.assertEquals(message, 0, result);
        result = new Decimal("0").comparesTo(new Decimal("0"));
        message = getCompareMessage("0", "0", 0, result);
        Assert.assertEquals(message, 0, result);
        result = new Decimal("0").comparesTo(new Decimal("1"));
        message = getCompareMessage("0", "1", -1, result);
        Assert.assertEquals(message, -1, result);
        result = new Decimal("1").comparesTo(new Decimal("0"));
        message = getCompareMessage("1", "0", 1, result);
        Assert.assertEquals(message, 1, result);
        result = new Decimal("10").comparesTo(new Decimal("10"));
        message = getCompareMessage("10", "10", 0, result);
        Assert.assertEquals(message, 0, result);
        result = new Decimal("100").comparesTo(new Decimal("100"));
        message = getCompareMessage("100", "100", 0, result);
        Assert.assertEquals(message, 0, result);
        result = new Decimal("0.1").comparesTo(new Decimal("0.1"));
        message = getCompareMessage("0.1", "0.1", 0, result);
        Assert.assertEquals(message, 0, result);
        result = new Decimal("0.01").comparesTo(new Decimal("0.01"));
        message = getCompareMessage("0.01", "0.01", 0, result);
        Assert.assertEquals(message, 0, result);
        result = new Decimal("0.01").comparesTo(new Decimal("0.0100"));
        message = getCompareMessage("0.01", "0.0100", 0, result);
        Assert.assertEquals(message, 0, result);
        result = new Decimal("1").comparesTo(new Decimal("1.00000000"));
        message = getCompareMessage("1", "1.00000000", 0, result);
        Assert.assertEquals(message, 0, result);
        result = new Decimal("1.111111").comparesTo(new Decimal("1.111111"));
        message = getCompareMessage("1.111111", "1.111111", 0, result);
        Assert.assertEquals(message, 0, result);
    }

    @Test
    public void testAddition() throws UcumException {
        Decimal res = new Decimal("1").add(new Decimal("1"));
        String message = getAddSubMessage("1", "1", "2", "+", res);
        Assert.assertEquals(message, "2", res.asDecimal());
        res = new Decimal("0").add(new Decimal("1"));
        message = getAddSubMessage("0", "1", "1", "+", res);
        Assert.assertEquals(message, "1", res.asDecimal());
        res = new Decimal("0").add(new Decimal("0"));
        message = getAddSubMessage("0", "0", "0", "+", res);
        Assert.assertEquals(message, "0", res.asDecimal());
        res = new Decimal("5").add(new Decimal("5"));
        message = getAddSubMessage("5", "5", "10", "+", res);
        Assert.assertEquals(message, "10", res.asDecimal());
        res = new Decimal("10").add(new Decimal("1"));
        message = getAddSubMessage("10", "1", "11", "+", res);
        Assert.assertEquals(message, "11", res.asDecimal());
        res = new Decimal("11").add(new Decimal("12"));
        message = getAddSubMessage("11", "12", "23", "+", res);
        Assert.assertEquals(message, "23", res.asDecimal());
        res = new Decimal("15").add(new Decimal("16"));
        message = getAddSubMessage("15", "16", "31", "+", res);
        Assert.assertEquals(message, "31", res.asDecimal());
        res = new Decimal("150").add(new Decimal("160"));
        message = getAddSubMessage("150", "160", "310", "+", res);
        Assert.assertEquals(message, "310", res.asDecimal());
        res = new Decimal("153").add(new Decimal("168"));
        message = getAddSubMessage("153", "168", "321", "+", res);
        Assert.assertEquals(message, "321", res.asDecimal());
        res = new Decimal("15300000000000000000000000000000000001").add(new Decimal("1680"));
        message = getAddSubMessage("\"15300000000000000000000000000000000001", "1680", "15300000000000000000000000000000001681", "+", res);
        Assert.assertEquals(message, "15300000000000000000000000000000001681", res.asDecimal());
        res = new Decimal("1").add(new Decimal(".1"));
        message = getAddSubMessage("1", ".1", "1.1", "+", res);
        Assert.assertEquals(message, "1.1", res.asDecimal());
        res = new Decimal("1").add(new Decimal(".001"));
        message = getAddSubMessage("1", ".001", "1.001", "+", res);
        Assert.assertEquals(message, "1.001", res.asDecimal());
        res = new Decimal(".1").add(new Decimal(".1"));
        message = getAddSubMessage(".1", ".1", "0.2", "+", res);
        Assert.assertEquals(message, "0.2", res.asDecimal());
        res = new Decimal(".1").add(new Decimal(".01"));
        message = getAddSubMessage(".1", ".01", "0.11", "+", res);
        Assert.assertEquals(message, "0.11", res.asDecimal());
        res = new Decimal("5").add(new Decimal("6"));
        message = getAddSubMessage("5", "6", "11", "+", res);
        Assert.assertEquals(message, "11", res.asDecimal());
        res = new Decimal("5").add(new Decimal("-6"));
        message = getAddSubMessage("5", "-6", "-1", "+", res);
        Assert.assertEquals(message, "-1", res.asDecimal());
        res = new Decimal("-5").add(new Decimal("6"));
        message = getAddSubMessage("-5", "6", "1", "+", res);
        Assert.assertEquals(message, "1", res.asDecimal());
        res = new Decimal("-5").add(new Decimal("-6"));
        message = getAddSubMessage("-5", "-6", "-11", "+", res);
        Assert.assertEquals(message, "-11", res.asDecimal());
        res = new Decimal("2").add(new Decimal("0.001"));
        message = getAddSubMessage("2", "0.001", "2.001", "+", res);
        Assert.assertEquals(message, "2.001", res.asDecimal());
        res = new Decimal("2.0").add(new Decimal("0.001"));
        message = getAddSubMessage("2.0", "0.001", "2.001", "+", res);
        Assert.assertEquals(message, "2.001", res.asDecimal());
    }

    @Test
    public void testSubtract() throws UcumException {
        Decimal res = new Decimal("2").subtract(new Decimal("1"));
        String message = getAddSubMessage("2", "1", "1", "-", res);
        Assert.assertEquals(message, "1", res.asDecimal());
        res = new Decimal("2").subtract(new Decimal("0"));
        message = getAddSubMessage("2", "0", "2", "-", res);
        Assert.assertEquals(message, "2", res.asDecimal());
        res = new Decimal("0").subtract(new Decimal("0"));
        message = getAddSubMessage("0", "0", "0", "-", res);
        Assert.assertEquals(message, "0", res.asDecimal());
        res = new Decimal("0").subtract(new Decimal("2"));
        message = getAddSubMessage("0", "2", "-2", "-", res);
        Assert.assertEquals(message, "-2", res.asDecimal());
        res = new Decimal("2").subtract(new Decimal("2"));
        message = getAddSubMessage("2", "2", "0", "-", res);
        Assert.assertEquals(message, "0", res.asDecimal());
        res = new Decimal("1").subtract(new Decimal("2"));
        message = getAddSubMessage("1", "2", "-1", "-", res);
        Assert.assertEquals(message, "-1", res.asDecimal());
        res = new Decimal("20").subtract(new Decimal("1"));
        message = getAddSubMessage("20", "1", "19", "-", res);
        Assert.assertEquals(message, "19", res.asDecimal());
        res = new Decimal("2").subtract(new Decimal(".1"));
        message = getAddSubMessage("2", ".1", "1.9", "-", res);
        Assert.assertEquals(message, "1.9", res.asDecimal());
        res = new Decimal("2").subtract(new Decimal(".000001"));
        message = getAddSubMessage("2", ".000001", "1.999999", "-", res);
        Assert.assertEquals(message, "1.999999", res.asDecimal());
        res = new Decimal("2").subtract(new Decimal("2.000001"));
        message = getAddSubMessage("2", "2.000001", "-0.000001", "-", res);
        Assert.assertEquals(message, "-0.000001", res.asDecimal());
        res = new Decimal("3.5").subtract(new Decimal("35.5"));
        message = getAddSubMessage("3.5", "35.5", "-32.0", "-", res);
        Assert.assertEquals(message, "-32.0", res.asDecimal());
        res = new Decimal("5").subtract(new Decimal("6"));
        message = getAddSubMessage("5", "6", "-1", "-", res);
        Assert.assertEquals(message, "-1", res.asDecimal());
        res = new Decimal("6").subtract(new Decimal("5"));
        message = getAddSubMessage("6", "5", "1", "-", res);
        Assert.assertEquals(message, "1", res.asDecimal());
        res = new Decimal("5").subtract(new Decimal("-6"));
        message = getAddSubMessage("5", "-6", "11", "-", res);
        Assert.assertEquals(message, "11", res.asDecimal());
        res = new Decimal("6").subtract(new Decimal("-5"));
        message = getAddSubMessage("6", "-5", "11", "-", res);
        Assert.assertEquals(message, "11", res.asDecimal());
        res = new Decimal("-5").subtract(new Decimal("6"));
        message = getAddSubMessage("-5", "6", "-11", "-", res);
        Assert.assertEquals(message, "-11", res.asDecimal());
        res = new Decimal("-6").subtract(new Decimal("5"));
        message = getAddSubMessage("-6", "5", "-11", "-", res);
        Assert.assertEquals(message, "-11", res.asDecimal());
        res = new Decimal("-5").subtract(new Decimal("-6"));
        message = getAddSubMessage("-5", "-6", "1", "-", res);
        Assert.assertEquals(message, "1", res.asDecimal());
        res = new Decimal("-6").subtract(new Decimal("-5"));
        message = getAddSubMessage("-6", "-5", "-1", "-", res);
        Assert.assertEquals(message, "-1", res.asDecimal());
    }

    @Test
    public void testMultiply() throws UcumException {
        Decimal res = new Decimal("2").multiply(new Decimal("2"));
        String message = getAddSubMessage("2", "2", "4", "*", res);
        Assert.assertEquals(message, "4", res.asDecimal());
        res = new Decimal("2").multiply(new Decimal("0.5"));
        message = getAddSubMessage("2", "0.5", "1", "*", res);
        Assert.assertEquals(message, "1", res.asDecimal());
        res = new Decimal("0").multiply(new Decimal("0"));
        message = getAddSubMessage("0", "0", "0", "*", res);
        Assert.assertEquals(message, "0", res.asDecimal());
        res = new Decimal("0").multiply(new Decimal("1"));
        message = getAddSubMessage("0", "1", "0", "*", res);
        Assert.assertEquals(message, "0", res.asDecimal());
        res = new Decimal("4").multiply(new Decimal("4"));
        message = getAddSubMessage("4", "4", "16", "*", res);
        Assert.assertEquals(message, "16", res.asDecimal());
        res = new Decimal("20").multiply(new Decimal("20"));
        message = getAddSubMessage("20", "20", "400", "*", res);
        Assert.assertEquals(message, "400", res.asDecimal());
        res = new Decimal("200").multiply(new Decimal("20"));
        message = getAddSubMessage("200", "20", "4000", "*", res);
        Assert.assertEquals(message, "4000", res.asDecimal());
        res = new Decimal("400").multiply(new Decimal("400"));
        message = getAddSubMessage("400", "400", "160000", "*", res);
        Assert.assertEquals(message, "160000", res.asDecimal());
        res = new Decimal("2.0").multiply(new Decimal("2.0"));
        message = getAddSubMessage("2.0", "2.0", "4.0", "*", res);
        Assert.assertEquals(message, "4.0", res.asDecimal());
        res = new Decimal("2.00").multiply(new Decimal("2.0"));
        message = getAddSubMessage("2.00", "2.0", "4.0", "*", res);
        Assert.assertEquals(message, "4.0", res.asDecimal());
        res = new Decimal("2.0").multiply(new Decimal("0.2"));
        message = getAddSubMessage("2.0", "0.2", "0.4", "*", res);
        Assert.assertEquals(message, "0.4", res.asDecimal());
        res = new Decimal("2.0").multiply(new Decimal("0.20"));
        message = getAddSubMessage("2.0", "0.20", "0.40", "*", res);
        Assert.assertEquals(message, "0.40", res.asDecimal());
        res = new Decimal("13").multiply(new Decimal("13"));
        message = getAddSubMessage("13", "13", "169", "*", res);
        Assert.assertEquals(message, "169", res.asDecimal());
        res = new Decimal("12").multiply(new Decimal("89"));
        message = getAddSubMessage("12", "89", "1068", "*", res);
        Assert.assertEquals(message, "1068", res.asDecimal());
        res = new Decimal("1234").multiply(new Decimal("6789"));
        message = getAddSubMessage("1234", "6789", "8377626", "*", res);
        Assert.assertEquals(message, "8377626", res.asDecimal());
        res = new Decimal("10000").multiply(new Decimal("0.0001"));
        message = getAddSubMessage("10000", "0.0001", "1", "*", res);
        Assert.assertEquals(message, "1", res.asDecimal());
        res = new Decimal("10000").multiply(new Decimal("0.00010"));
        message = getAddSubMessage("10000", "0.00010", "1.0", "*", res);
        Assert.assertEquals(message, "1.0", res.asDecimal());
        res = new Decimal("10000").multiply(new Decimal("0.000100"));
        message = getAddSubMessage("10000", "0.000100", "1.00", "*", res);
        Assert.assertEquals(message, "1.00", res.asDecimal());
        res = new Decimal("10000").multiply(new Decimal("0.0001000"));
        message = getAddSubMessage("10000", "0.0001000", "1.000", "*", res);
        Assert.assertEquals(message, "1.000", res.asDecimal());
        res = new Decimal("10000").multiply(new Decimal("0.00010000"));
        message = getAddSubMessage("10000", "0.00010000", "1.0000", "*", res);
        Assert.assertEquals(message, "1.0000", res.asDecimal());
        res = new Decimal("10000").multiply(new Decimal("0.000100000"));
        message = getAddSubMessage("10000", "0.000100000", "1.00000", "*", res);
        Assert.assertEquals(message, "1.00000", res.asDecimal());
        res = new Decimal("10000.0").multiply(new Decimal("0.000100000"));
        message = getAddSubMessage("10000.0", "0.000100000", "1.00000", "*", res);
        Assert.assertEquals(message, "1.00000", res.asDecimal());
        res = new Decimal("10000.0").multiply(new Decimal("0.0001000000"));
        message = getAddSubMessage("10000.0", "0.0001000000", "1.00000", "*", res);
        Assert.assertEquals(message, "1.00000", res.asDecimal());
        res = new Decimal("10000.0").multiply(new Decimal("0.00010000000"));
        message = getAddSubMessage("10000.0", "0.00010000000", "1.00000", "*", res);
        Assert.assertEquals(message, "1.00000", res.asDecimal());
        res = new Decimal("2").multiply(new Decimal("-2"));
        message = getAddSubMessage("2", "-2", "-4", "*", res);
        Assert.assertEquals(message, "-4", res.asDecimal());
        res = new Decimal("-2").multiply(new Decimal("2"));
        message = getAddSubMessage("-2", "2", "-4", "*", res);
        Assert.assertEquals(message, "-4", res.asDecimal());
        res = new Decimal("-2").multiply(new Decimal("-2"));
        message = getAddSubMessage("-2", "-2", "4", "*", res);
        Assert.assertEquals(message, "4", res.asDecimal());
        res = new Decimal("35328734682734").multiply(new Decimal("2349834295876423"));
        message = getAddSubMessage("35328734682734", "2349834295876423", "83016672387407213199375780482", "*", res);
        Assert.assertEquals(message, "83016672387407213199375780482", res.asDecimal());
        res = new Decimal("35328734682734000000000").multiply(new Decimal("2349834295876423000000000"));
        message = getAddSubMessage("35328734682734000000000", "2349834295876423000000000", "83016672387407213199375780482000000000000000000", "*", res);
        Assert.assertEquals(message, "83016672387407213199375780482000000000000000000", res.asDecimal());
        res = new Decimal("3532873468.2734").multiply(new Decimal("23498342958.76423"));
        message = getAddSubMessage("3532873468.2734", "23498342958.76423", "83016672387407213199.375780482", "*", res);
        Assert.assertEquals(message, "83016672387407213199.375780482", res.asDecimal());
    }

    @Test
    public void testDivide() throws UcumException {
        Decimal res = new Decimal("500").divide(new Decimal("4"));
        String message = getAddSubMessage("500", "4", "125", "/", res);
        Assert.assertEquals(message, "125", res.asDecimal());
        res = new Decimal("1260257").divide(new Decimal("37"));
        message = getAddSubMessage("1260257", "37", "34061", "/", res);
        Assert.assertEquals(message, "34061", res.asDecimal());
        res = new Decimal("127").divide(new Decimal("4"));
        message = getAddSubMessage("127", "4", "31.75", "/", res);
        Assert.assertEquals(message, "31.75", res.asDecimal());
        res = new Decimal("10").divide(new Decimal("10"));
        message = getAddSubMessage("10", "10", "1", "/", res);
        Assert.assertEquals(message, "1", res.asDecimal());
        res = new Decimal("1").divide(new Decimal("1"));
        message = getAddSubMessage("1", "1", "1", "/", res);
        Assert.assertEquals(message, "1", res.asDecimal());
        res = new Decimal("10").divide(new Decimal("3"));
        message = getAddSubMessage("10", "3", "3.3", "/", res);
        Assert.assertEquals(message, "3.3", res.asDecimal());
        res = new Decimal("10.0").divide(new Decimal("3"));
        message = getAddSubMessage("10.0", "3", "3.33", "/", res);
        Assert.assertEquals(message, "3.33", res.asDecimal());
        res = new Decimal("10.00").divide(new Decimal("3"));
        message = getAddSubMessage("10.00", "3", "3.333", "/", res);
        Assert.assertEquals(message, "3.333", res.asDecimal());
        res = new Decimal("10.00").divide(new Decimal("3.0"));
        message = getAddSubMessage("10.00", "3.0", "3.3", "/", res);
        Assert.assertEquals(message, "3.3", res.asDecimal());
        res = new Decimal("100").divide(new Decimal("1"));
        message = getAddSubMessage("100", "1", "100", "/", res);
        Assert.assertEquals(message, "100", res.asDecimal());
        res = new Decimal("1000").divide(new Decimal("10"));
        message = getAddSubMessage("1000", "10", "100", "/", res);
        Assert.assertEquals(message, "100", res.asDecimal());
        res = new Decimal("100001").divide(new Decimal("10"));
        message = getAddSubMessage("100001", "10", "10000.1", "/", res);
        Assert.assertEquals(message, "10000.1", res.asDecimal());
        res = new Decimal("100").divide(new Decimal("10"));
        message = getAddSubMessage("100", "10", "10", "/", res);
        Assert.assertEquals(message, "10", res.asDecimal());
        res = new Decimal("1").divide(new Decimal("10"));
        message = getAddSubMessage("1", "10", "0.1", "/", res);
        Assert.assertEquals(message, "0.1", res.asDecimal());
        res = new Decimal("1").divide(new Decimal("15"));
        message = getAddSubMessage("1", "15", "0.067", "/", res);
        Assert.assertEquals(message, "0.067", res.asDecimal());
        res = new Decimal("1.0").divide(new Decimal("15"));
        message = getAddSubMessage("1.0", "15", "0.067", "/", res);
        Assert.assertEquals(message, "0.067", res.asDecimal());
        res = new Decimal("1.00").divide(new Decimal("15.0"));
        message = getAddSubMessage("1.00", "15.0", "0.0667", "/", res);
        Assert.assertEquals(message, "0.0667", res.asDecimal());
        res = new Decimal("1").divide(new Decimal("0.1"));
        message = getAddSubMessage("1", "0.1", "10", "/", res);
        Assert.assertEquals(message, "10", res.asDecimal());
        res = new Decimal("1").divide(new Decimal("0.10"));
        message = getAddSubMessage("1", "0.10", "10", "/", res);
        Assert.assertEquals(message, "10", res.asDecimal());
        res = new Decimal("1").divide(new Decimal("0.010"));
        message = getAddSubMessage("1", "0.010", "100", "/", res);
        Assert.assertEquals(message, "100", res.asDecimal());
        res = new Decimal("1").divide(new Decimal("1.5"));
        message = getAddSubMessage("1", "1.5", "0.67", "/", res);
        Assert.assertEquals(message, "0.67", res.asDecimal());
        res = new Decimal("1.0").divide(new Decimal("1.5"));
        message = getAddSubMessage("1.0", "1.5", "0.67", "/", res);
        Assert.assertEquals(message, "0.67", res.asDecimal());
        res = new Decimal("10").divide(new Decimal("1.5"));
        message = getAddSubMessage("10", "1.5", "6.7", "/", res);
        Assert.assertEquals(message, "6.7", res.asDecimal());
        res = new Decimal("-1").divide(new Decimal("1"));
        message = getAddSubMessage("-1", "1", "-1", "/", res);
        Assert.assertEquals(message, "-1", res.asDecimal());
        res = new Decimal("1").divide(new Decimal("-1"));
        message = getAddSubMessage("1", "-1", "-1", "/", res);
        Assert.assertEquals(message, "-1", res.asDecimal());
        res = new Decimal("-1").divide(new Decimal("-1"));
        message = getAddSubMessage("-1", "-1", "1", "/", res);
        Assert.assertEquals(message, "1", res.asDecimal());
        res = new Decimal("2").divide(new Decimal("2"));
        message = getAddSubMessage("2", "2", "1", "/", res);
        Assert.assertEquals(message, "1", res.asDecimal());
        res = new Decimal("20").divide(new Decimal("2"));
        message = getAddSubMessage("20", "2", "10", "/", res);
        Assert.assertEquals(message, "10", res.asDecimal());
        res = new Decimal("22").divide(new Decimal("2"));
        message = getAddSubMessage("22", "2", "11", "/", res);
        Assert.assertEquals(message, "11", res.asDecimal());
        res = new Decimal("83016672387407213199375780482").divide(new Decimal("2349834295876423"));
        message = getAddSubMessage("83016672387407213199375780482", "2349834295876423", "35328734682734", "/", res);
        Assert.assertEquals(message, "35328734682734", res.asDecimal());
        res = new Decimal("83016672387407213199375780482000000000000000000").divide(new Decimal("2349834295876423000000000"));
        message = getAddSubMessage("83016672387407213199375780482000000000000000000", "2349834295876423000000000", "35328734682734000000000", "/", res);
        Assert.assertEquals(message, "35328734682734000000000", res.asDecimal());
        res = new Decimal("83016672387407213199.375780482").divide(new Decimal("23498342958.76423"));
        message = getAddSubMessage("83016672387407213199.375780482", "23498342958.76423", "3532873468.2734", "/", res);
        Assert.assertEquals(message, "3532873468.2734", res.asDecimal());
    }

    @Test
    public void testIntegerDivide() throws UcumException {
        Decimal res = new Decimal("500").divInt(new Decimal("4"));
        String message = getAddSubMessage("500", "4", "125", "/(int)", res);
        Assert.assertEquals(message, "125", res.asDecimal());
        res = new Decimal("1260257").divInt(new Decimal("37"));
        message = getAddSubMessage("1260257", "37", "34061", "/(int)", res);
        Assert.assertEquals(message, "34061", res.asDecimal());
        res = new Decimal("127").divInt(new Decimal("4"));
        message = getAddSubMessage("127", "4", "31", "/(int)", res);
        Assert.assertEquals(message, "31", res.asDecimal());
        res = new Decimal("10").divInt(new Decimal("10"));
        message = getAddSubMessage("10", "10", "1", "/(int)", res);
        Assert.assertEquals(message, "1", res.asDecimal());
        res = new Decimal("1").divInt(new Decimal("1"));
        message = getAddSubMessage("1", "1", "1", "/(int)", res);
        Assert.assertEquals(message, "1", res.asDecimal());
        res = new Decimal("100").divInt(new Decimal("1"));
        message = getAddSubMessage("100", "1", "100", "/(int)", res);
        Assert.assertEquals(message, "100", res.asDecimal());
        res = new Decimal("1000").divInt(new Decimal("10"));
        message = getAddSubMessage("1000", "10", "100", "/(int)", res);
        Assert.assertEquals(message, "100", res.asDecimal());
        res = new Decimal("100001").divInt(new Decimal("10"));
        message = getAddSubMessage("100001", "10", "10000", "/(int)", res);
        Assert.assertEquals(message, "10000", res.asDecimal());
        res = new Decimal("1").divInt(new Decimal("1.5"));
        message = getAddSubMessage("1", "1.5", "0", "/(int)", res);
        Assert.assertEquals(message, "0", res.asDecimal());
        res = new Decimal("10").divInt(new Decimal("1.5"));
        message = getAddSubMessage("10", "1.5", "6", "/(int)", res);
        Assert.assertEquals(message, "6", res.asDecimal());
    }

    @Test
    public void testModulo() throws UcumException {
        Decimal res = new Decimal("10").modulo(new Decimal("1"));
        String message = getAddSubMessage("10", "1", "0", "%", res);
        Assert.assertEquals(message, "0", res.asDecimal());
        res = new Decimal("7").modulo(new Decimal("4"));
        message = getAddSubMessage("7", "4", "3", "%%", res);
        Assert.assertEquals(message, "3", res.asDecimal());
    }

    @Test
    public void testSuite() throws XmlPullParserException, IOException, UcumException {
        UcumTester worker = new UcumTester();
        worker.setDefinitions("src/main/resources/ucum-essence.xml");
        worker.setTests("src/test/resources/UcumFunctionalTests.xml");
        System.out.println("Defitions: " + worker.getDefinitions() + "\n" + "Tests: " + worker.getTests());
        worker.execute();
    }

    @Test
    public void testDecimalPrecission() throws IOException, UcumException {

        String fileName = "ucum-essence.xml";
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

       InputStream inputStream = new FileInputStream(file);

        UcumEssenceService ucumService = new UcumEssenceService(inputStream);
        Decimal result = ucumService.convert(new Decimal("15"), "/min", "/h");
        Assert.assertEquals(result.asDecimal(), 900);
    }

    @Test
    public void testDecimalEquals() throws IOException, UcumException {

        Decimal dec1 = new Decimal(42);
        Decimal dec2 = new Decimal(42);
        Assert.assertEquals(dec1, dec2);
    }

    private String definitions;
    private String tests;

    public String getDefinitions() {
        return definitions;
    }

    public void setDefinitions(String definitions) {
        this.definitions = definitions;
    }

    public String getTests() {
        return tests;
    }

    public void setTests(String tests) {
        this.tests = tests;
    }


    private UcumService ucumSvc;
    private int errCount;

    private void testDecimal() throws UcumException {
        testAsInteger();
        testStringSupport();
        testTruncate();
        testCompares();
        testAddition();
        testSubtract();
        testMultiply();
        testDivide();
        testIntegerDivide();
        testModulo();
    }

    private void execute() throws XmlPullParserException, IOException, UcumException {

        testDecimal();

        ucumSvc = new UcumEssenceService(definitions);
        errCount = 0;
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance(System.getProperty(XmlPullParserFactory.PROPERTY_NAME), null);
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

//        InputStream is = this.getClass().getResourceAsStream(tests);
//        xpp.setInput(is, null);
        xpp.setInput(new FileInputStream(tests), null);

        int eventType = xpp.next();
        if (eventType != XmlPullParser.START_TAG)
            throw new XmlPullParserException("Unable to process XML document");
        if (!xpp.getName().equals("ucumTests"))
            throw new XmlPullParserException("Unable to process XML document: expected 'ucumTests' but found '" + xpp.getName() + "'");

        xpp.next();
        while (xpp.getEventType() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() == XmlPullParser.TEXT) {
                if (Utilities.isWhitespace(xpp.getText()))
                    xpp.next();
                else
                    throw new XmlPullParserException("Unexpected text " + xpp.getText());
            } else if (xpp.getName().equals("history"))
                skipElement(xpp);
            else if (xpp.getName().equals("validation"))
                runValidationTests(xpp);
            else if (xpp.getName().equals("displayNameGeneration"))
                runDisplayNameGeneration(xpp);
            else if (xpp.getName().equals("conversion"))
                runConversion(xpp);
            else if (xpp.getName().equals("multiplication"))
                runMultiplication(xpp);
            else
                throw new XmlPullParserException("unknown element name " + xpp.getName());
        }
        xpp.next();
        if (errCount > 0)
            System.err.println(Integer.toString(errCount) + " errors");
    }

    private void runMultiplication(XmlPullParser xpp) throws XmlPullParserException, IOException, UcumException {
        xpp.next();
        while (xpp.getEventType() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() == XmlPullParser.TEXT) {
                if (Utilities.isWhitespace(xpp.getText()))
                    xpp.next();
                else
                    throw new XmlPullParserException("Unexpected text " + xpp.getText());
            } else if (xpp.getName().equals("case"))
                runMultiplicationCase(xpp);
            else
                throw new XmlPullParserException("unknown element name " + xpp.getName());
        }
        xpp.next();
    }

    private void runMultiplicationCase(XmlPullParser xpp) throws XmlPullParserException, IOException, UcumException {

        String id = xpp.getAttributeValue(null, "id");
        String v1 = xpp.getAttributeValue(null, "v1");
        String u1 = xpp.getAttributeValue(null, "u1");
        String v2 = xpp.getAttributeValue(null, "v2");
        String u2 = xpp.getAttributeValue(null, "u2");
        String vRes = xpp.getAttributeValue(null, "vRes");
        String uRes = xpp.getAttributeValue(null, "uRes");

        Pair o1 = new Pair(new Decimal(v1), u1);
        Pair o2 = new Pair(new Decimal(v2), u2);
        Pair o3 = ucumSvc.multiply(o1, o2);

        debug("Multiplication Test " + id + ": the value '" + v1 + " " + u1 + "' * '" + v2 + " " + u2 + "' ==> " + o3.getValue().toString() + " " + o3.getCode());

        // if (!res.toPlainString().equals(outcome)) { - that assumes that we can get the precision right, which we can't
        if (o3.getValue().comparesTo(new Decimal(vRes)) != 0 || !o3.getCode().equals(uRes)) {
            errCount++;
            System.err.println("Test " + id + ": The value '" + vRes + " " + uRes + "' was expected, but the result was " + o3.getValue().toString() + " " + o3.getCode());
        }
        while (xpp.getEventType() != XmlPullParser.END_TAG)
            xpp.next();
        xpp.next();

    }

    private String getCompareMessage(String v1, String v2, int outcome, int result) {
        return String.format("Compare fail: %s.compares(%s) should be %d but was %d", v1, v2, outcome, result);
    }

    private String getAddSubMessage(String s1, String s2, String s3, String op, Decimal result) {
        return String.format("%s %s %s = %s, but the library returned %s", s1, op, s2, s3, result.asDecimal());
    }

    private void runConversion(XmlPullParser xpp) throws XmlPullParserException, IOException, UcumException {
        xpp.next();
        while (xpp.getEventType() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() == XmlPullParser.TEXT) {
                if (Utilities.isWhitespace(xpp.getText()))
                    xpp.next();
                else
                    throw new XmlPullParserException("Unexpected text " + xpp.getText());
            } else if (xpp.getName().equals("case"))
                runConversionCase(xpp);
            else
                throw new XmlPullParserException("unknown element name " + xpp.getName());
        }
        xpp.next();
    }

    private void runConversionCase(XmlPullParser xpp) throws XmlPullParserException, IOException, UcumException {

        String id = xpp.getAttributeValue(null, "id");
        String value = xpp.getAttributeValue(null, "value");
        String srcUnit = xpp.getAttributeValue(null, "srcUnit");
        String dstUnit = xpp.getAttributeValue(null, "dstUnit");
        String outcome = xpp.getAttributeValue(null, "outcome");
        System.out.println("case " + id + ": " + value + " " + srcUnit + " -> " + outcome + " " + dstUnit);
        Decimal res = ucumSvc.convert(new Decimal(value), srcUnit, dstUnit);
        debug("Convert Test " + id + ": the value '" + value + " " + srcUnit + "' ==> " + res.toString() + " " + dstUnit);

        // if (!res.toPlainString().equals(outcome)) { - that assumes that we can get the precision right, which we can't
        if (res.comparesTo(new Decimal(outcome)) != 0) {
            errCount++;
            System.err.println("Test " + id + ": The value '" + outcome + "' was expected the result was " + res.toString());
        }
        while (xpp.getEventType() != XmlPullParser.END_TAG)
            xpp.next();
        xpp.next();
    }


    private void debug(String string) {
        System.out.println(string);

    }

    private void runDisplayNameGeneration(XmlPullParser xpp) throws XmlPullParserException, IOException, UcumException {
        xpp.next();
        while (xpp.getEventType() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() == XmlPullParser.TEXT) {
                if (Utilities.isWhitespace(xpp.getText()))
                    xpp.next();
                else
                    throw new XmlPullParserException("Unexpected text " + xpp.getText());
            } else if (xpp.getName().equals("case"))
                runDisplayNameGenerationCase(xpp);
            else
                throw new XmlPullParserException("unknown element name " + xpp.getName());
        }
        xpp.next();
    }

    private void runDisplayNameGenerationCase(XmlPullParser xpp) throws XmlPullParserException, IOException, UcumException {
        String id = xpp.getAttributeValue(null, "id");
        String unit = xpp.getAttributeValue(null, "unit");
        String display = xpp.getAttributeValue(null, "display");

        String res = ucumSvc.analyse(unit);
        debug("Analyse Test " + id + ": the unit '" + unit + "' ==> " + res);

        if (!res.equals(display)) {
            errCount++;
            System.err.println("Test " + id + ": The unit '" + unit + "' was expected to be displayed as '" + display + "', but was displayed as " + res);
        }
        while (xpp.getEventType() != XmlPullParser.END_TAG)
            xpp.next();
        xpp.next();
    }

    private void runValidationTests(XmlPullParser xpp) throws XmlPullParserException, IOException {
        xpp.next();
        while (xpp.getEventType() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() == XmlPullParser.TEXT) {
                if (Utilities.isWhitespace(xpp.getText()))
                    xpp.next();
                else
                    throw new XmlPullParserException("Unexpected text " + xpp.getText());
            } else if (xpp.getName().equals("case"))
                runValidationCase(xpp);
            else
                throw new XmlPullParserException("unknown element name " + xpp.getName());
        }
        xpp.next();
    }

    private void runValidationCase(XmlPullParser xpp) throws XmlPullParserException, IOException {
        String id = xpp.getAttributeValue(null, "id");
        String unit = xpp.getAttributeValue(null, "unit");
        boolean valid = "true".equals(xpp.getAttributeValue(null, "valid"));
        String reason = xpp.getAttributeValue(null, "reason");

        String res = ucumSvc.validate(unit);
        boolean result = res == null;
        if (result)
            debug("Validation Test " + id + ": the unit '" + unit + "' is valid");
        else
            debug("Validation Test " + id + ": the unit '" + unit + "' is not valid because " + res);

        if (valid != result) {
            errCount++;
            if (valid)
                System.err.println("Test " + id + ": The unit '" + unit + "' was expected to be valid, but wasn't accepted");
            else
                System.err.println("Test " + id + ": The unit '" + unit + "' was expected to be invalid because '" + reason + "', but was accepted");
        }
        while (xpp.getEventType() != XmlPullParser.END_TAG)
            xpp.next();
        xpp.next();
    }

    private void skipElement(XmlPullParser xpp) throws XmlPullParserException, IOException {
        xpp.next();
        while (xpp.getEventType() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() == XmlPullParser.START_TAG)
                skipElement(xpp);
            else
                xpp.next();
        }
        xpp.next();
    }


}
