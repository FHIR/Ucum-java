package org.fhir.ucum.definitions;

import java.io.InputStream;

import org.fhir.ucum.UcumException;
import org.fhir.ucum.UcumModel;

public interface DefinitionsProvider {
  public UcumModel parse(String filename) throws UcumException;
  public UcumModel parse(InputStream stream) throws UcumException;
  
}
