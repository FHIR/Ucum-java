package org.fhir.ucum.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class XmlUtils {

  public static Document parseDOM(InputStream stream) throws ParserConfigurationException, SAXException, IOException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(false);
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(stream);
    return doc;
  }

  public static Element getNamedChild(Element e, String name) {
    Element c = getFirstChild(e);
    while (c != null && !name.equals(c.getLocalName()) && !name.equals(c.getNodeName()))
      c = getNextSibling(c);
    return c;
  }
  
  public static String getNamedChildText(Element element, String name) {
    Element e = getNamedChild(element, name);
    return e == null ? null : e.getTextContent();
  }
  
  public static Element getFirstChild(Element e) {
    if (e == null)
      return null;
    Node n = e.getFirstChild();
    while (n != null && n.getNodeType() != Node.ELEMENT_NODE)
      n = n.getNextSibling();
    return (Element) n;
  }


  public static Element getNextSibling(Element e) {
    Node n = e.getNextSibling();
    while (n != null && n.getNodeType() != Node.ELEMENT_NODE)
      n = n.getNextSibling();
    return (Element) n;
  }


  public static List<Element> getNamedChildren(Element e, String name) {
    List<Element> res = new ArrayList<Element>();
    Element c = getFirstChild(e);
    while (c != null) {
      if (name.equals(c.getLocalName()) || name.equals(c.getNodeName()) )
        res.add(c);
      c = getNextSibling(c);
    }
    return res;
  }
}
