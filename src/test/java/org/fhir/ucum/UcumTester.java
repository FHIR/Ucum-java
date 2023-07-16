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

import javax.xml.parsers.ParserConfigurationException;

import org.fhir.ucum.utils.XmlUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;


public class UcumTester {

  @Test
  public void testSuite() throws IOException, UcumException, ParserConfigurationException, SAXException {
    UcumTester worker = new UcumTester();
    worker.setDefinitions("src/main/resources/ucum-essence.xml");
    worker.setTests("src/test/resources/UcumFunctionalTests.xml");
    System.out.println("Defitions: " + worker.getDefinitions() + "\n" + "Tests: " + worker.getTests());
    worker.execute();

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

  private void execute() throws IOException, UcumException, ParserConfigurationException, SAXException  {
    ucumSvc = new UcumEssenceService(definitions);
    errCount = 0;
    Document doc = XmlUtils.parseDOM(new FileInputStream(tests));
    Element element = doc.getDocumentElement();

    if (!element.getNodeName().equals("ucumTests"))
      throw new UcumException("Unable to process XML document: expected 'ucumTests' but found '"+element.getNodeName()+"'");

    Element focus = XmlUtils.getFirstChild(element);
    while (focus != null) {
      if (focus.getNodeName().equals("history"))
        ;
      else if (focus.getNodeName().equals("validation"))
        runValidationTests(focus);
      else if (focus.getNodeName().equals("displayNameGeneration"))
        runDisplayNameGeneration(focus);
      else if (focus.getNodeName().equals("conversion"))
        runConversion(focus);
      else if (focus.getNodeName().equals("multiplication"))
        runMultiplication(focus);
      else if (focus.getNodeName().equals("division"))
        runDivision(focus);
      else
        throw new UcumException("unknown element name "+focus.getNodeName());
      focus = XmlUtils.getNextSibling(focus);
    }
    if (errCount > 0)
      System.err.println(Integer.toString(errCount)+" errors");
  }

  private void runMultiplication(Element x) throws IOException, UcumException  {
    for (Element e : XmlUtils.getNamedChildren(x, "case")) {
      runMultiplicationCase(e);      
    }
  }

  private void runDivision(Element x) throws IOException, UcumException  {
    for (Element e : XmlUtils.getNamedChildren(x, "case")) {
      runDivisionCase(e);      
    }
  }

  private void runMultiplicationCase(Element x) throws IOException, UcumException  {

    String id = x.getAttribute("id");
    String v1 = x.getAttribute("v1");
    String u1 = x.getAttribute("u1");
    String v2 = x.getAttribute("v2");
    String u2 = x.getAttribute("u2");
    String vRes = x.getAttribute("vRes");
    String uRes = x.getAttribute("uRes");

    Pair o1 = new Pair(new Decimal(v1), u1);
    Pair o2 = new Pair(new Decimal(v2), u2);
    Pair o3 = ucumSvc.multiply(o1, o2);

    debug("Multiplication Test "+id+": the value '"+v1+" "+u1+"' * '"+v2+" "+u2+"' ==> "+o3.getValue().toString()+" "+o3.getCode());

    // if (!res.toPlainString().equals(outcome)) { - that assumes that we can get the precision right, which we can't
    if (o3.getValue().comparesTo(new Decimal(vRes)) != 0 || !o3.getCode().equals(uRes)) {
      errCount++;
      System.err.println("Test "+id+": The value '"+vRes+" "+uRes+"' was expected, but the result was "+o3.getValue().toString()+" "+o3.getCode());
    }

  }

  private void runDivisionCase(Element x) throws IOException, UcumException  {

    String id = x.getAttribute("id");
    String v1 = x.getAttribute("v1");
    String u1 = x.getAttribute("u1");
    String v2 = x.getAttribute("v2");
    String u2 = x.getAttribute("u2");
    String vRes = x.getAttribute("vRes");
    String uRes = x.getAttribute("uRes");

    Pair o1 = new Pair(new Decimal(v1), u1);
    Pair o2 = new Pair(new Decimal(v2), u2);
    Pair o3 = ucumSvc.divideBy(o1, o2);

    debug("Division Test "+id+": the value '"+v1+" "+u1+"' * '"+v2+" "+u2+"' ==> "+o3.getValue().toString()+" "+o3.getCode());

    // if (!res.toPlainString().equals(outcome)) { - that assumes that we can get the precision right, which we can't
    if (o3.getValue().comparesTo(new Decimal(vRes)) != 0 || !o3.getCode().equals(uRes)) {
      errCount++;
      System.err.println("Test "+id+": The value '"+vRes+" "+uRes+"' was expected, but the result was "+o3.getValue().toString()+" "+o3.getCode());
    }

  }


  private void runConversion(Element x) throws IOException, UcumException  {
    for (Element e : XmlUtils.getNamedChildren(x, "case")) {
      runConversionCase(e);      
    }
  }

  private void runConversionCase(Element x) throws IOException, UcumException  {

    String id = x.getAttribute("id");
    String value = x.getAttribute("value");
    String srcUnit = x.getAttribute("srcUnit");
    String dstUnit = x.getAttribute("dstUnit");
    String outcome = x.getAttribute("outcome");
    System.out.println("case "+id+": "+value+" "+srcUnit+" -> "+outcome+" "+dstUnit);
    Decimal res = ucumSvc.convert(new Decimal(value), srcUnit, dstUnit);
    debug("Convert Test "+id+": the value '"+value+" "+srcUnit+"' ==> "+res.toString()+" "+dstUnit);

    // if (!res.toPlainString().equals(outcome)) { - that assumes that we can get the precision right, which we can't
    if (res.comparesTo(new Decimal(outcome)) != 0) {
      errCount++;
      System.err.println("Test "+id+": The value '"+outcome+"' was expected the result was "+res.toString());
    }
  }


  private void debug(String string) {
    // System.out.println(string);

  }
  private void runDisplayNameGeneration(Element x) throws IOException, UcumException  {
    for (Element e : XmlUtils.getNamedChildren(x, "case")) {
      runDisplayNameGenerationCase(e);      
    }
  }

  private void runDisplayNameGenerationCase(Element x) throws IOException, UcumException  {
    String id = x.getAttribute("id");
    String unit = x.getAttribute("unit");
    String display = x.getAttribute("display");

    String res = ucumSvc.analyse(unit);
    debug("Analyse Test "+id+": the unit '"+unit+"' ==> "+res);

    if (!res.equals(display)) {
      errCount++;
      System.err.println("Test "+id+": The unit '"+unit+"' was expected to be displayed as '"+display+"', but was displayed as "+res);
    }
  }

  private void runValidationTests(Element x) throws IOException  {
    for (Element e : XmlUtils.getNamedChildren(x, "case")) {
      runValidationCase(e);      
    }
  }

  private void runValidationCase(Element x) throws IOException  {
    String id = x.getAttribute("id");
    String unit = x.getAttribute("unit");
    boolean valid = "true".equals(x.getAttribute("valid"));
    String reason = x.getAttribute("reason");

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
  }


}
