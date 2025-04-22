package org.fhir.ucum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JavaTestIssue50 extends UcumServiceTest {

  @Test
  void test() throws UcumException {
    Assertions.assertTrue(ucumService.isComparable("[iU]", "[iU]"));
  }

}
