package org.fhir.ucum.serializing.xpp3;

import org.fhir.ucum.serializing.DefinitionParser;

public class DefinitionParserProvider implements org.fhir.ucum.serializing.DefinitionParserProvider {

    @Override
    public DefinitionParser create() {
        return new org.fhir.ucum.serializing.xpp3.DefinitionParser();
    }
}
