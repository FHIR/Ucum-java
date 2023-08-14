package org.fhir.ucum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UcumServiceTest {
    protected UcumService ucumService;

    @BeforeAll
    public void beforeAll() throws FileNotFoundException, UcumException {
        ucumService = getUcumEssenceService();
    }
    private UcumService getUcumEssenceService() throws FileNotFoundException, UcumException {
        ;
        String fileName = "ucum-essence.xml";
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        InputStream inputStream = new FileInputStream(file);
        UcumService ucumService = new UcumEssenceService(inputStream);
        return ucumService;
    }
}
