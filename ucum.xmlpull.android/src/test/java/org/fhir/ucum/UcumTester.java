package org.fhir.ucum;

/*******************************************************************************
 * Crown Copyright (c) 2006 - 2014, Copyright (c) 2006 - 2014 Kestral Computing & Health Intersections P/L.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kestral Computing P/L - initial implementation
 *    Health Intersections P/L - port to java, ongoing maintenance
 *
 ******************************************************************************/

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class UcumTester extends BaseUcumTester {

    @Test
    public void testSuite() throws XmlPullParserException, IOException, UcumException {
        UcumTester worker = new UcumTester();
        worker.setDefinitions(UcumEssenceService.class.getResourceAsStream("/ucum-essence.xml"));
        worker.setTests(UcumTester.class.getResourceAsStream("/UcumFunctionalTests.xml"));
        System.out.println("Defitions: " + worker.getDefinitions() + "\n" + "Tests: " + worker.getTests());
        worker.execute();
    }
}
