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
        throw new RuntimeException("No ModelInfoReaderProviders found");
    }
}
