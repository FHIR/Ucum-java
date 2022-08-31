# Ucum-java
FHIR Java library providing UCUM Services

The library provides a set of services around UCUM:

- validate a UCUM unit (and also against a particular base unit)
- decide whether one unit can be converted/compared to another
- translate a quantity from one unit to another 
- prepare a human-readable display of a unit 
- multiply 2 quantities together

To use the library, download ucum-essence from [http://unitsofmeasure.org](http://unitsofmeasure.org), and then create a UCUMEssenceService:

UcumService ucumSvc = new UcumEssenceService(definitionFile);

# Usage

Declare dependencies on: 
1. the base module `org.fhir:ucum`;
2. the ucum-essence [pull-parsing](http://www.xmlpull.org/) interface module `org.fhir:ucum.xmlpull`; 
3. and one of the available implementations: `org.fhir:ucum.xmlpull.codelibs-xpp3`, `org.fhir:ucum.xmlpull.android`, `org.fhir:ucum.xmlpull.xpp3`. 

For Java 9+, we recommend using [Codelib's XPP3](https://github.com/codelibs/xpp3) fork:
```xml
<dependency>
    <groupId>org.fhir</groupId>
    <artifactId>ucum.xmlpull.codelibs-xpp3</artifactId>
    <version>1.0.3</version>
</dependency>
```

For older versions of Java, we recommend using the original [XPP3](http://www.extreme.indiana.edu/xgws/xsoap/xpp/mxp1/): 
```xml
<dependency>
    <groupId>org.fhir</groupId>
    <artifactId>ucum.xmlpull.xpp3</artifactId>
    <version>1.0.3</version>
</dependency>
```

Android provides its own XML Pull Parser implementation, use the `ucum.xmlpull.android` to avoid loading additional XML tools: 
```xml
<dependency>
    <groupId>org.fhir</groupId>
    <artifactId>ucum.xmlpull.android</artifactId>
    <version>1.0.3</version>
</dependency>
```

# Repository Layout

This repository contains the following modules:

| Module Name                | Description                                                     |
|----------------------------|-----------------------------------------------------------------|
| ucum                       | Base implementation of Ucum                                     | 
| ucum.xmlpull               | Ucum-essence Parser using XMLPull (not provided)                |
| ucum.xmlpull.xpp3          | Ucum-essence Parser using XMLPull from XPP3 (Old Java)          |
| ucum.xmlpull.codelibs-xpp3 | Ucum-essence Parser using XMLPull from CodeLib's XPP3 (Java 9+) |
| ucum.xmlpull.android       | Ucum-essence Parser using XMLPull from the Android SDK          |
| ucum.test-resources        | Shared Test resources                                           |

