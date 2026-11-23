package org.fhir.ucum;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Hostile unit strings must be rejected quickly rather than consuming unbounded CPU
 * in canonicalisation (GHSA-3mxc-hm56-xrc9)
 */
public class UcumComplexityLimitTests extends UcumServiceTest {

  private static String repeat(String unit, int count, String sep) {
    StringBuilder b = new StringBuilder(unit);
    for (int i = 1; i < count; i++)
      b.append(sep).append(unit);
    return b.toString();
  }

  private void assertRejected(String unit) {
    Assertions.assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
      assertThrows(UcumException.class, () -> ucumService.getCanonicalUnits(unit));
      assertNotNull(ucumService.validate(unit));
    });
  }

  @Test
  public void testLargeExponents() {
    assertRejected("km2000000000");
    assertRejected("km100000");
    assertRejected("km400");
    assertRejected("km31");
    assertRejected("km-31");
    assertRejected("km99999999999"); // doesn't fit in an int
  }

  @Test
  public void testTooManyComponents() {
    assertRejected(repeat("Ym", 21, "."));
    assertRejected(repeat("2", 21, "."));
  }

  @Test
  public void testTotalExponentTooLarge() {
    assertRejected(repeat("Ym24", 10, "."));
    assertRejected("Ym30.Ym30.Ym");
    assertRejected(repeat("Ym3", 21, "/"));
  }

  @Test
  public void testNestingTooDeep() {
    assertRejected("(((((((((((m)))))))))))");
  }

  @Test
  public void testRealisticUnitsStillWork() throws UcumException {
    Assertions.assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
      assertNull(ucumService.validate("10*23"));
      assertNull(ucumService.validate("10*-24"));
      assertNull(ucumService.validate("10*12/L"));
      assertEquals("m30", ucumService.getCanonicalUnits("km30"));
      assertEquals("m-30", ucumService.getCanonicalUnits("km-30"));
      assertEquals("m20", ucumService.getCanonicalUnits(repeat("Ym", 20, ".")));
      assertEquals("m60", ucumService.getCanonicalUnits("Ym30.Ym30"));
      assertEquals("m", ucumService.getCanonicalUnits("((((((((((m))))))))))"));
    });
  }

  @Test
  public void testTooManyTerms() {
    // each annotation or stray '/' is a level of recursion in the parser: these used to overflow the stack
    assertRejected(repeat("{a}", 100000, ""));
    assertRejected("m"+repeat("{a}", 100000, ""));
    assertRejected("m"+repeat("/", 100000, "")+"m");
    assertRejected(repeat("{a}", 51, ""));
  }

  @Test
  public void testLongTokensAreCheap() {
    // the lexer used to build tokens one character at a time (quadratic)
    Assertions.assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
      for (int i = 0; i < 10; i++) {
        assertNotNull(ucumService.validate(repeat("a", 200000, "")));
        assertNotNull(ucumService.validate("["+repeat("a", 200000, "")+"]"));
        assertNotNull(ucumService.validate(repeat("1", 200000, "")));
        assertNotNull(ucumService.validate("+"+repeat("1", 200000, "")));
      }
      assertNull(ucumService.validate("{"+repeat("a", 1000000, "")+"}"));
    });
  }

  @Test
  public void testValidateEdgeCases() {
    // used to report null (= valid), because the NullPointerException had no message 
    assertNotNull(ucumService.validate(null));
    assertEquals(true, ucumService.validate("{abc").contains("unterminated annotation"));
    assertEquals(true, ucumService.validate("{a\u00e9}").contains("non-ascii"));
    assertNull(ucumService.validate("{a}"));
    assertNull(ucumService.validate("m{a}"));
  }

  @Test
  public void testHugeValues() throws UcumException {
    Assertions.assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
      assertThrows(UcumException.class, () -> ucumService.convert(new Decimal("1e300000000"), "m", "km"));
      assertEquals(0, ucumService.convert(new Decimal("1e1000"), "m", "km").comparesTo(new Decimal("1e997")));
      Pair p = ucumService.divideBy(new Pair(new Decimal("1."+repeat("1", 200000, "")), "m"), new Pair(new Decimal("3"), "s"));
      assertEquals("3.703703703703703703703703703703703703703e-1", p.getValue().asScientific()); // dividend rounded to 40 1s first
      assertEquals("m.s-1", p.getCode());
    });
  }

  @Test
  public void testRegexSearch() {
    assertEquals(1, ucumService.search(null, "meter", true).size());
    assertEquals(5, ucumService.search(null, ".*meter.*", true).size());
    IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> ucumService.search(null, "[", true));
    assertEquals(true, e.getMessage().contains("is not valid"));
  }

  @Test
  public void testRegexSearchTimesOut() {
    // catastrophic backtracking against ordinary unit names: this used to run for (at least) many seconds
    Assertions.assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
      IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> ucumService.search(null, "((.|.)*)*!", true));
      assertEquals(true, e.getMessage().contains("took too long"));
    });
  }

  @Test
  public void testErrorMessagesAreTruncated() {
    String big = repeat("a", 200000, "");
    assertShort(ucumService.validate(big));
    assertShort(ucumService.validate("m."+big));
    assertShort(ucumService.validate("{"+big));
    assertShort(ucumService.validate("m/"+repeat("{a}", 100000, "")));
    assertShort(ucumService.validateInProperty("m", big));
    assertShort(ucumService.validateCanonicalUnits("m", big));
    assertShort(assertThrows(UcumException.class, () -> ucumService.getCanonicalUnits(big)).getMessage());
    assertShort(assertThrows(UcumException.class, () -> ucumService.convert(new Decimal(1), "m", "s."+repeat("s", 200000, ""))).getMessage());
    assertShort(assertThrows(UcumException.class, () -> new Decimal(repeat("1", 200000, "")+"x")).getMessage());
    assertShort(assertThrows(IllegalArgumentException.class, () -> ucumService.search(null, "["+big, true)).getMessage());
  }

  private void assertShort(String msg) {
    assertNotNull(msg);
    assertEquals(true, msg.length() < 1000, "message is "+msg.length()+" chars long");
  }
}
