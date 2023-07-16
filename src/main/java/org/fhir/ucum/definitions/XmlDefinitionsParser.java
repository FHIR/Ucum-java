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

package org.fhir.ucum.definitions;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.fhir.ucum.BaseUnit;
import org.fhir.ucum.Decimal;
import org.fhir.ucum.DefinedUnit;
import org.fhir.ucum.Prefix;
import org.fhir.ucum.UcumException;
import org.fhir.ucum.UcumModel;
import org.fhir.ucum.Value;
import org.fhir.ucum.utils.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Parses the file ucum-essense.xml
 * 
 * @author Grahame Grieve
 *
 */

public class XmlDefinitionsParser implements DefinitionsProvider {

	public UcumModel parse(String filename) throws UcumException  {
	  try {
	    return parse(new FileInputStream(new File(filename)));
    } catch (Exception e) {
      throw new UcumException(e);
    } 
	}

	public UcumModel parse(InputStream stream) throws UcumException {
	  try {
	    Document doc = XmlUtils.parseDOM(stream);
	    Element element = doc.getDocumentElement();

	    if (!element.getNodeName().equals("root")) 
	      throw new UcumException("Unable to process XML document: expected 'root' but found '"+element.getNodeName()+"'");
	    DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss' 'Z");
	    Date date = fmt.parse(element.getAttribute("revision-date").substring(7, 32));        
	    UcumModel root = new UcumModel(element.getAttribute("version"), element.getAttribute("revision"), date);
	    Element focus = XmlUtils.getFirstChild(element);
	    while (focus != null) {
	      if (focus.getNodeName().equals("prefix")) 
	        root.getPrefixes().add(parsePrefix(focus));
	      else if (focus.getNodeName().equals("base-unit")) 
	        root.getBaseUnits().add(parseBaseUnit(focus));
	      else if (focus.getNodeName().equals("unit")) 
	        root.getDefinedUnits().add(parseUnit(focus));
	      else 
	        throw new UcumException("unknown element name "+focus.getNodeName());
	      focus = XmlUtils.getNextSibling(focus);
	    }
	    return root;
	  } catch (Exception e) {
	    throw new UcumException(e);
	  }
	}


	private DefinedUnit parseUnit(Element x) throws IOException, UcumException  {
		DefinedUnit unit = new DefinedUnit(x.getAttribute("Code"), x.getAttribute("CODE"));
		unit.setMetric("yes".equals(x.getAttribute("isMetric")));
		unit.setSpecial("yes".equals(x.getAttribute("isSpecial")));
		unit.setClass_(x.getAttribute("class"));
		for (Element e : XmlUtils.getNamedChildren(x, "name")) {
		  unit.getNames().add(e.getTextContent());
		}
    unit.setPrintSymbol(XmlUtils.getNamedChildText(x, "printSymbol"));
    unit.setProperty(XmlUtils.getNamedChildText(x, "property"));
		unit.setValue(parseValue(XmlUtils.getNamedChild(x, "value"), "unit "+unit.getCode()));
		return unit;
	}

	private Value parseValue(Element x, String context) throws UcumException, IOException  {
		Decimal val = null;
		if (x.getAttribute("value") != null) 
			try {
				if (x.getAttribute("value").contains("."))
					val = new Decimal(x.getAttribute("value"), 24); // unlimited precision for these
				else
					val = new Decimal(x.getAttribute("value"));
			} catch (NumberFormatException e) {
				throw new UcumException("Error reading "+context+": "+e.getMessage());
			}
		Value value = new Value(x.getAttribute("Unit"), x.getAttribute("UNIT"), val);
		value.setText(x.getTextContent());
		return value;
	}

	private BaseUnit parseBaseUnit(Element x) throws IOException {
		BaseUnit base = new BaseUnit(x.getAttribute("Code"), x.getAttribute("CODE"));
		base.setDim(x.getAttribute("dim").charAt(0));
    for (Element e : XmlUtils.getNamedChildren(x, "name")) {
      base.getNames().add(e.getTextContent());
    }
    base.setPrintSymbol(XmlUtils.getNamedChildText(x, "printSymbol"));
    base.setProperty(XmlUtils.getNamedChildText(x, "property"));    
		return base;
	}

	private Prefix parsePrefix(Element x) throws IOException, UcumException  {
		Prefix prefix = new Prefix(x.getAttribute("Code"), x.getAttribute("CODE"));
    for (Element e : XmlUtils.getNamedChildren(x, "name")) {
      prefix.getNames().add(e.getTextContent());
    }
    prefix.setPrintSymbol(XmlUtils.getNamedChildText(x, "printSymbol"));
    Element value = XmlUtils.getNamedChild(x, "value");
		prefix.setValue(new Decimal(value.getAttribute("value"), 24));
		return prefix;
	}

}
