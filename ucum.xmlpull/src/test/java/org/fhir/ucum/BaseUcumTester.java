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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class BaseUcumTester {

    private InputStream definitions;
    private InputStream tests;
    public InputStream getDefinitions() {
        return definitions;
    }
    public void setDefinitions(InputStream definitions) {
        this.definitions = definitions;
    }
    public InputStream getTests() {
        return tests;
    }
    public void setTests(InputStream tests) {
        this.tests = tests;
    }


    private UcumService ucumSvc;
    private int errCount;

    public void execute() throws XmlPullParserException, IOException, UcumException  {
        ucumSvc = new UcumEssenceService(definitions);
        errCount = 0;
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance(System.getProperty(XmlPullParserFactory.PROPERTY_NAME), null);
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        xpp.setInput(tests, null);

        int eventType = xpp.next();
        if (eventType != XmlPullParser.START_TAG)
            throw new XmlPullParserException("Unable to process XML document");
        if (!xpp.getName().equals("ucumTests"))
            throw new XmlPullParserException("Unable to process XML document: expected 'ucumTests' but found '"+xpp.getName()+"'");

        xpp.next();
        while (xpp.getEventType() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() == XmlPullParser.TEXT) {
                if (Utilities.isWhitespace(xpp.getText()))
                    xpp.next();
                else
                    throw new XmlPullParserException("Unexpected text "+xpp.getText());
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
            else if (xpp.getName().equals("division"))
              runDivision(xpp);
            else
                throw new XmlPullParserException("unknown element name "+xpp.getName());
        }
        xpp.next();
        if (errCount > 0)
            System.err.println(Integer.toString(errCount)+" errors");
    }

    private void runMultiplication(XmlPullParser xpp) throws XmlPullParserException, IOException, UcumException  {
        xpp.next();
        while (xpp.getEventType() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() == XmlPullParser.TEXT) {
                if (Utilities.isWhitespace(xpp.getText()))
                    xpp.next();
                else
                    throw new XmlPullParserException("Unexpected text "+xpp.getText());
            } else if (xpp.getName().equals("case"))
                runMultiplicationCase(xpp);
            else
                throw new XmlPullParserException("unknown element name "+xpp.getName());
        }
        xpp.next();
    }

    private void runDivision(XmlPullParser xpp) throws XmlPullParserException, IOException, UcumException  {
      xpp.next();
      while (xpp.getEventType() != XmlPullParser.END_TAG) {
          if (xpp.getEventType() == XmlPullParser.TEXT) {
              if (Utilities.isWhitespace(xpp.getText()))
                  xpp.next();
              else
                  throw new XmlPullParserException("Unexpected text "+xpp.getText());
          } else if (xpp.getName().equals("case"))
              runDivisionCase(xpp);
          else
              throw new XmlPullParserException("unknown element name "+xpp.getName());
      }
      xpp.next();
  }

    private void runMultiplicationCase(XmlPullParser xpp) throws XmlPullParserException, IOException, UcumException  {

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

        debug("Multiplication Test "+id+": the value '"+v1+" "+u1+"' * '"+v2+" "+u2+"' ==> "+o3.getValue().toString()+" "+o3.getCode());

        // if (!res.toPlainString().equals(outcome)) { - that assumes that we can get the precision right, which we can't
        if (o3.getValue().comparesTo(new Decimal(vRes)) != 0 || !o3.getCode().equals(uRes)) {
            errCount++;
            System.err.println("Test "+id+": The value '"+vRes+" "+uRes+"' was expected, but the result was "+o3.getValue().toString()+" "+o3.getCode());
        }
        while (xpp.getEventType() != XmlPullParser.END_TAG)
            xpp.next();
        xpp.next();

    }

    private void runDivisionCase(XmlPullParser xpp) throws XmlPullParserException, IOException, UcumException  {

      String id = xpp.getAttributeValue(null, "id");
      String v1 = xpp.getAttributeValue(null, "v1");
      String u1 = xpp.getAttributeValue(null, "u1");
      String v2 = xpp.getAttributeValue(null, "v2");
      String u2 = xpp.getAttributeValue(null, "u2");
      String vRes = xpp.getAttributeValue(null, "vRes");
      String uRes = xpp.getAttributeValue(null, "uRes");

      Pair o1 = new Pair(new Decimal(v1), u1);
      Pair o2 = new Pair(new Decimal(v2), u2);
      Pair o3 = ucumSvc.divideBy(o1, o2);

      debug("Division Test "+id+": the value '"+v1+" "+u1+"' * '"+v2+" "+u2+"' ==> "+o3.getValue().toString()+" "+o3.getCode());

      // if (!res.toPlainString().equals(outcome)) { - that assumes that we can get the precision right, which we can't
      if (o3.getValue().comparesTo(new Decimal(vRes)) != 0 || !o3.getCode().equals(uRes)) {
          errCount++;
          System.err.println("Test "+id+": The value '"+vRes+" "+uRes+"' was expected, but the result was "+o3.getValue().toString()+" "+o3.getCode());
      }
      while (xpp.getEventType() != XmlPullParser.END_TAG)
          xpp.next();
      xpp.next();

  }


    private void runConversion(XmlPullParser xpp) throws XmlPullParserException, IOException, UcumException  {
        xpp.next();
        while (xpp.getEventType() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() == XmlPullParser.TEXT) {
                if (Utilities.isWhitespace(xpp.getText()))
                    xpp.next();
                else
                    throw new XmlPullParserException("Unexpected text "+xpp.getText());
            } else if (xpp.getName().equals("case"))
                runConversionCase(xpp);
            else
                throw new XmlPullParserException("unknown element name "+xpp.getName());
        }
        xpp.next();
    }

    private void runConversionCase(XmlPullParser xpp) throws XmlPullParserException, IOException, UcumException  {

        String id = xpp.getAttributeValue(null, "id");
        String value = xpp.getAttributeValue(null, "value");
        String srcUnit = xpp.getAttributeValue(null, "srcUnit");
        String dstUnit = xpp.getAttributeValue(null, "dstUnit");
        String outcome = xpp.getAttributeValue(null, "outcome");
        System.out.println("case "+id+": "+value+" "+srcUnit+" -> "+outcome+" "+dstUnit);
        Decimal res = ucumSvc.convert(new Decimal(value), srcUnit, dstUnit);
        debug("Convert Test "+id+": the value '"+value+" "+srcUnit+"' ==> "+res.toString()+" "+dstUnit);

        // if (!res.toPlainString().equals(outcome)) { - that assumes that we can get the precision right, which we can't
        if (res.comparesTo(new Decimal(outcome)) != 0) {
            errCount++;
            System.err.println("Test "+id+": The value '"+outcome+"' was expected the result was "+res.toString());
        }
        while (xpp.getEventType() != XmlPullParser.END_TAG)
            xpp.next();
        xpp.next();
    }


    private void debug(String string) {
       // System.out.println(string);

    }
    private void runDisplayNameGeneration(XmlPullParser xpp) throws XmlPullParserException, IOException, UcumException  {
        xpp.next();
        while (xpp.getEventType() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() == XmlPullParser.TEXT) {
                if (Utilities.isWhitespace(xpp.getText()))
                    xpp.next();
                else
                    throw new XmlPullParserException("Unexpected text "+xpp.getText());
            } else if (xpp.getName().equals("case"))
                runDisplayNameGenerationCase(xpp);
            else
                throw new XmlPullParserException("unknown element name "+xpp.getName());
        }
        xpp.next();
    }

    private void runDisplayNameGenerationCase(XmlPullParser xpp) throws XmlPullParserException, IOException, UcumException  {
        String id = xpp.getAttributeValue(null, "id");
        String unit = xpp.getAttributeValue(null, "unit");
        String display = xpp.getAttributeValue(null, "display");

        String res = ucumSvc.analyse(unit);
        debug("Analyse Test "+id+": the unit '"+unit+"' ==> "+res);

        if (!res.equals(display)) {
            errCount++;
            System.err.println("Test "+id+": The unit '"+unit+"' was expected to be displayed as '"+display+"', but was displayed as "+res);
        }
        while (xpp.getEventType() != XmlPullParser.END_TAG)
            xpp.next();
        xpp.next();
    }

    private void runValidationTests(XmlPullParser xpp) throws XmlPullParserException, IOException  {
        xpp.next();
        while (xpp.getEventType() != XmlPullParser.END_TAG) {
            if (xpp.getEventType() == XmlPullParser.TEXT) {
                if (Utilities.isWhitespace(xpp.getText()))
                    xpp.next();
                else
                    throw new XmlPullParserException("Unexpected text "+xpp.getText());
            } else if (xpp.getName().equals("case"))
                runValidationCase(xpp);
            else
                throw new XmlPullParserException("unknown element name "+xpp.getName());
        }
        xpp.next();
    }

    private void runValidationCase(XmlPullParser xpp) throws XmlPullParserException, IOException  {
        String id = xpp.getAttributeValue(null, "id");
        String unit = xpp.getAttributeValue(null, "unit");
        boolean valid = "true".equals(xpp.getAttributeValue(null, "valid"));
        String reason = xpp.getAttributeValue(null, "reason");

        String res = ucumSvc.validate(unit);
        boolean result = res == null;
        if (result)
            debug("Validation Test "+id+": the unit '"+unit+"' is valid");
        else
            debug("Validation Test "+id+": the unit '"+unit+"' is not valid because "+res);

        if (valid != result) {
            errCount++;
            if (valid) {
              Assert.assertTrue("Unit "+unit+" was expected to be valid, but was invalid ("+id+")", false);
              System.err.println("Test "+id+": The unit '"+unit+"' was expected to be valid, but wasn't accepted");
            } else {
              Assert.assertTrue("Unit "+unit+" was expected to be invalid, but was valid ("+id+")", false);
              System.err.println("Test "+id+": The unit '"+unit+"' was expected to be invalid because '"+reason+"', but was accepted");
            }
        }
        while (xpp.getEventType() != XmlPullParser.END_TAG)
            xpp.next();
        xpp.next();
    }

    private void skipElement(XmlPullParser xpp) throws XmlPullParserException, IOException  {
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
