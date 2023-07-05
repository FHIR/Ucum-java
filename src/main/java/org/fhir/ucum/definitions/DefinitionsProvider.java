package org.fhir.ucum.definitions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;

import org.fhir.ucum.UcumException;
import org.fhir.ucum.UcumModel;
import org.xml.sax.SAXException;

public interface DefinitionsProvider {
  public UcumModel parse(String filename) throws UcumException;
  public UcumModel parse(InputStream stream) throws UcumException;
  
}
