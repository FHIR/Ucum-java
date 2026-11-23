# Ucum-java
[![OpenSSF Scorecard](https://api.scorecard.dev/projects/github.com/FHIR/Ucum-java/badge)](https://scorecard.dev/viewer/?uri=github.com/FHIR/Ucum-java) [![CodeQL](https://github.com/FHIR/Ucum-java/actions/workflows/codeql.yml/badge.svg)](https://github.com/FHIR/Ucum-java/actions/workflows/codeql.yml) [![OWASP Security Scans](https://github.com/FHIR/Ucum-java/actions/workflows/owasp.yml/badge.svg)](https://github.com/FHIR/Ucum-java/actions/workflows/owasp.yml) [![Trivy Security Scans](https://github.com/FHIR/Ucum-java/actions/workflows/trivy.yml/badge.svg)](https://github.com/FHIR/Ucum-java/actions/workflows/trivy.yml)

|                           CI Status (master)                           |                                            Current Release |      Latest SNAPSHOT       |
|:----------------------------------------------------------------------:|-----------------------------------------------------------:|:--------------------------:|
| [![Build Status][Badge-AzureMasterPipeline]][Link-AzureMasterPipeline] | [![Badge-MavenCentralReleases]][Link-MavenCentralReleases] | ![Badge-SonatypeSnapshots] |


FHIR Java library providing UCUM Services

The library provides a set of services around UCUM:

- validate a UCUM unit (and also against a particular base unit)
- decide whether one unit can be converted/compared to another
- translate a quantity from one unit to another 
- prepare a human-readable display of a unit 
- multiply 2 quantities together

To use the library, download ucum-essence from http://unitsofmeasure.org, and then create a UCUMEssenceService:

```java
UcumService ucumSvc = new UcumEssenceService(definitionFile);
```

## Limits on input

The services are commonly used to process units and values supplied by external parties (e.g. FHIR `Quantity`), 
so the library limits the work that any single input can cause. The limits are well beyond anything 
used in practice.

Unit expressions (enforced by the parser, so units beyond these limits are reported as invalid by `validate()` and 
rejected by all the other services):

| Limit | Value | Constant |
|-------|-------|----------|
| Exponent of any single unit (e.g. `m3`, `10*-24`) | +/-30 | `ExpressionParser.MAX_EXPONENT` |
| Sum of the absolute values of all exponents | 60 | `ExpressionParser.MAX_TOTAL_EXPONENT` |
| Number of units and numbers in the expression | 20 | `ExpressionParser.MAX_COMPONENTS` |
| Depth of nested brackets | 10 | `ExpressionParser.MAX_NESTING` |
| Number of terms (including annotations) | 50 | `ExpressionParser.MAX_TERMS` |

Note that UCUM itself does not limit these, so a few units that are legal UCUM (e.g. `m31`) are rejected.

Numbers (`Decimal`):

* Scientific notation must have an exponent within +/-1000 (`Decimal.MAX_EXPONENT`)
* Multiplication rounds its operands and result to at most 40 significant digits, and division rounds its operands 
  to 40 significant digits and calculates at most 40 digits beyond them. Applications that need different limits 
  can change them (application wide) using `Decimal.setMultiplyPrecisionLimit()` and `Decimal.setDividePrecisionLimit()`

Other:

* `search(kind, text, true)` evaluates `text` as a regular expression, and throws `IllegalArgumentException` if 
  it is not valid, or if the search takes longer than 500ms (protection against regular expressions that 
  backtrack catastrophically)
* Error messages truncate the caller supplied values they include to 100 characters

[Link-AzureMasterPipeline]: https://dev.azure.com/fhir-pipelines/ucum-java/_build/latest?definitionId=51&branchName=master
[Badge-AzureMasterPipeline]: https://dev.azure.com/fhir-pipelines/ucum-java/_apis/build/status%2FMaster%20Branch%20Pipeline?branchName=master
[Link-MavenCentralReleases]: https://central.sonatype.com/artifact/org.fhir/ucum
[Badge-MavenCentralReleases]: https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Frepo1.maven.org%2Fmaven2%2Forg%2Ffhir%2Fucum%2Fmaven-metadata.xml "Maven Central Releases"
[Badge-SonatypeSnapshots]:https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Forg%2Ffhir%2Fucum%2Fmaven-metadata.xml "Sonatype Snapshots"
