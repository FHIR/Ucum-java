package org.fhir.ucum.serializing;

import org.fhir.ucum.UcumException;
import org.fhir.ucum.UcumModel;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

public interface DefinitionParser {
    public UcumModel parse(String filename) throws DefinitionParserException, UcumException, IOException, ParseException;
    public UcumModel parse(InputStream stream) throws DefinitionParserException, UcumException, IOException, ParseException;
}
