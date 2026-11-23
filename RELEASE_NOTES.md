* Security: fix denial of service in unit canonicalisation and conversion (GHSA-3mxc-hm56-xrc9). A short unit string such as `km2000000000` could consume unbounded CPU. Unit expressions are now limited in complexity (see "Limits on input" in the README); units beyond the limits are reported as invalid
* Security: fix other ways that caller supplied input could consume unbounded CPU or memory, or crash the stack:
  * deeply nested or very long unit expressions (e.g. thousands of annotations or `/`s) no longer overflow the stack
  * very long unit strings are lexed in linear time
  * `Decimal` rejects scientific notation with an exponent beyond +/-1000 (e.g. `1e300000000` used to exhaust memory when converted to a string, and `1.5e2147483647` overflowed to `0.15`)
  * multiplication and division of very long numbers is bounded (see below)
  * `search(..., isRegex = true)` stops evaluating a regular expression after 500ms, and throws `IllegalArgumentException` (also for an invalid expression)
  * error messages truncate the caller supplied values they contain to 100 characters
* `Decimal`: multiplication is now independent of the magnitude of the numbers (it was very slow for large or small numbers)
* `Decimal`: new application wide precision limits, `setMultiplyPrecisionLimit()` and `setDividePrecisionLimit()` (both default to 40). Multiplication rounds its operands and result to this many significant digits; division rounds its operands, and calculates this many digits beyond them. Multiplication results are unchanged unless they have more than 40 significant digits, and division results are unchanged unless an operand has more than 40
* `Decimal`: reject `""`, `"-"` and non-ASCII digits, which used to be accepted
* `validate()`, `validateInProperty()` and `validateCanonicalUnits()` no longer report an invalid unit as valid when the underlying error has no message; `validate(null)` returns an error
* Special units now have a null value in the model (it used to be an empty `Decimal`)
* Fix documentation of `Decimal` precision behaviour (division results depend on significant figures)
