package org.fhir.ucum.serializing;

import java.util.Iterator;
import java.util.ServiceLoader;

public class DefinitionParserFactory {
    static ServiceLoader<DefinitionParserProvider> loader = ServiceLoader
            .load(DefinitionParserProvider.class);

    public static Iterator<DefinitionParserProvider> providers(boolean refresh) {
        if (refresh) {
            loader.reload();
        }
        return loader.iterator();
    }

    public static DefinitionParser getParser() {
        if (providers(false).hasNext()) {
            return providers(false).next().create();
        }
        throw new RuntimeException("No XML Parsers found. Choose a dependency: org.fhir:ucum.xmlpull.mxp1 (Java 9+), org.fhir:ucum.xmlpull.android (Native Android), org.fhir:ucum.xmlpull.xpp3 (old Java)");
    }
}
