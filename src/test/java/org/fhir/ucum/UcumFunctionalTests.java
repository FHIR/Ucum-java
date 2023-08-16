/*******************************************************************************
BSD 3-Clause License

Copyright (c) 2006+, Health Intersections Pty Ltd
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

* Neither the name of the copyright holder nor the names of its
  contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 *******************************************************************************/

package org.fhir.ucum;

import org.fhir.ucum.utils.XmlUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UcumFunctionalTests extends UcumServiceTest {

    private static class UcumFunctionalTestException extends Exception {
        public UcumFunctionalTestException(String string) {
            super(string);
        }
    }
    private enum FunctionalTestType {
        HISTORY,
        VALIDATION,
        DISPLAY_NAME_GENERATION,
        CONVERSION,
        DIVISION,
        MULTIPLICATION
    }

    private static FunctionalTestType getFunctionalTestType(String typeString) throws UcumFunctionalTestException {
        if (typeString.equals("history"))
            return FunctionalTestType.HISTORY;
        else if (typeString.equals("validation"))
            return FunctionalTestType.VALIDATION;
        else if (typeString.equals("displayNameGeneration"))
            return FunctionalTestType.DISPLAY_NAME_GENERATION;
        else if (typeString.equals("conversion"))
            return  FunctionalTestType.CONVERSION;
        else if (typeString.equals("multiplication"))
            return FunctionalTestType.MULTIPLICATION;
        else if (typeString.equals("division"))
            return FunctionalTestType.DIVISION;
        else
            throw new UcumFunctionalTestException("unknown element name "+ typeString);
    }
  public static Stream<Arguments> getTestXMLElements() throws UcumFunctionalTestException, IOException, ParserConfigurationException, SAXException {
      List<Arguments> elements = new ArrayList<>();
      String tests = "src/test/resources/UcumFunctionalTests.xml";
      Document doc = XmlUtils.parseDOM(new FileInputStream(tests));
      Element element = doc.getDocumentElement();

      if (!element.getNodeName().equals("ucumTests"))
          throw new UcumFunctionalTestException("Unable to process XML document: expected 'ucumTests' but found '"+element.getNodeName()+"'");

      Element focus = XmlUtils.getFirstChild(element);
      while (focus != null) {

          String testTypeString = focus.getNodeName();
          FunctionalTestType testType = getFunctionalTestType(testTypeString);
          for (Element testCase : XmlUtils.getNamedChildren(focus, "case"))
          {
              String testId = testCase.getAttribute("id");
            elements.add(Arguments.of(testTypeString + ": " + testId, testType, testCase));
          }
          focus = XmlUtils.getNextSibling(focus);

      }
      return elements.stream();
  }
    @DisplayName("UCUM Functional Tests loaded from UcumFunctionalTests.xml")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("getTestXMLElements")
    public void testXmlElement(String testName, FunctionalTestType testType, Element testCase) throws IOException, UcumException, UcumFunctionalTestException {
        if (testType.equals(FunctionalTestType.HISTORY))
            // Test history element. Not a test. Do nothing.
            ;
        else if (testType.equals(FunctionalTestType.VALIDATION))
          runValidationCase(testCase);
        else if (testType.equals(FunctionalTestType.DISPLAY_NAME_GENERATION))
          runDisplayNameGenerationCase(testCase);
        else if (testType.equals(FunctionalTestType.CONVERSION))
          runConversionCase(testCase);
        else if (testType.equals(FunctionalTestType.MULTIPLICATION))
          runMultiplicationCase(testCase);
        else if (testType.equals(FunctionalTestType.DIVISION))
          runDivisionCase(testCase);
        else
          throw new UcumFunctionalTestException("unknown test type "+ testCase.getNodeName());
    }

  private void runMultiplicationCase(Element x) throws UcumException  {

    String id = x.getAttribute("id");
    String v1 = x.getAttribute("v1");
    String u1 = x.getAttribute("u1");
    String v2 = x.getAttribute("v2");
    String u2 = x.getAttribute("u2");
    String vRes = x.getAttribute("vRes");
    String uRes = x.getAttribute("uRes");

    Pair o1 = new Pair(new Decimal(v1), u1);
    Pair o2 = new Pair(new Decimal(v2), u2);
    Pair o3 = ucumService.multiply(o1, o2);

    debug("Multiplication Test "+id+": the value '"+v1+" "+u1+"' * '"+v2+" "+u2+"' ==> "+o3.getValue().toString()+" "+o3.getCode());

    // if (!res.toPlainString().equals(outcome)) { - that assumes that we can get the precision right, which we can't
    if (o3.getValue().comparesTo(new Decimal(vRes)) != 0 || !o3.getCode().equals(uRes)) {
      fail("Test "+id+": The value '"+vRes+" "+uRes+"' was expected, but the result was "+o3.getValue().toString()+" "+o3.getCode());
    }

  }

  private void runDivisionCase(Element x) throws UcumException  {

    String id = x.getAttribute("id");
    String v1 = x.getAttribute("v1");
    String u1 = x.getAttribute("u1");
    String v2 = x.getAttribute("v2");
    String u2 = x.getAttribute("u2");
    String vRes = x.getAttribute("vRes");
    String uRes = x.getAttribute("uRes");

    Pair o1 = new Pair(new Decimal(v1), u1);
    Pair o2 = new Pair(new Decimal(v2), u2);
    Pair o3 = ucumService.divideBy(o1, o2);

    debug("Division Test "+id+": the value '"+v1+" "+u1+"' * '"+v2+" "+u2+"' ==> "+o3.getValue().toString()+" "+o3.getCode());

    // if (!res.toPlainString().equals(outcome)) { - that assumes that we can get the precision right, which we can't
    if (o3.getValue().comparesTo(new Decimal(vRes)) != 0 || !o3.getCode().equals(uRes)) {
      fail("Test "+id+": The value '"+vRes+" "+uRes+"' was expected, but the result was "+o3.getValue().toString()+" "+o3.getCode());
    }

  }


  private void runConversionCase(Element x) throws UcumException  {

    String id = x.getAttribute("id");
    String value = x.getAttribute("value");
    String srcUnit = x.getAttribute("srcUnit");
    String dstUnit = x.getAttribute("dstUnit");
    String outcome = x.getAttribute("outcome");
    System.out.println("case "+id+": "+value+" "+srcUnit+" -> "+outcome+" "+dstUnit);
    Decimal res = ucumService.convert(new Decimal(value), srcUnit, dstUnit);
    debug("Convert Test "+id+": the value '"+value+" "+srcUnit+"' ==> "+res.toString()+" "+dstUnit);

    // if (!res.toPlainString().equals(outcome)) { - that assumes that we can get the precision right, which we can't
    if (res.comparesTo(new Decimal(outcome)) != 0) {
      fail("Test "+id+": The value '"+outcome+"' was expected the result was "+res);
    }
  }


  private void debug(String string) {
    // System.out.println(string);

  }

  private void runDisplayNameGenerationCase(Element x) throws UcumException  {
    String id = x.getAttribute("id");
    String unit = x.getAttribute("unit");
    String display = x.getAttribute("display");

    String res = ucumService.analyse(unit);
    debug("Analyse Test "+id+": the unit '"+unit+"' ==> "+res);

    if (!res.equals(display)) {
      fail("Test "+id+": The unit '"+unit+"' was expected to be displayed as '"+display+"', but was displayed as "+res);
    }
  }

  private void runValidationCase(Element x) {
    String id = x.getAttribute("id");
    String unit = x.getAttribute("unit");
    boolean valid = "true".equals(x.getAttribute("valid"));
    String reason = x.getAttribute("reason");

    String res = ucumService.validate(unit);
    boolean result = res == null;
    if (result)
      debug("Validation Test "+id+": the unit '"+unit+"' is valid");
    else
      debug("Validation Test "+id+": the unit '"+unit+"' is not valid because "+res);

    if (valid != result) {
      if (valid) {
       fail("Unit "+unit+" was expected to be valid, but was invalid ("+id+")");

      } else {
        fail("Unit "+unit+" was expected to be invalid, but was valid ("+id+")");
      }
    }
  }

}
