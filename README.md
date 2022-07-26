# Ucum-java
FHIR Java library providing UCUM Services

The library provides a set of services around UCUM:

- validate a UCUM unit (and also against a particular base unit)
- decide whether one unit can be converted/compared to another
- translate a quantity from one unit to another 
- prepare a human readable display of a unit 
- multiply 2 quantities together

To use the library, download ucum-essence from http://unitsofmeasure.org, and then create a UCUMEssenceService:

UcumService ucumSvc = new UcumEssenceService(definitionFile);

# Usage

Declare dependency on `org.fhir:ucum`, one of the `ucum-essence` types of parsers `ucum.xmlpull` and one of the available implementations. 

For Java desktop/server, we recommend using XPP3: 
```xml
<dependency>
    <groupId>org.fhir</groupId>
    <artifactId>ucum</artifactId>
    <version>1.0.3</version>
</dependency>
<dependency>
  <groupId>org.fhir</groupId>
  <artifactId>ucum.xmlpull</artifactId>
  <version>1.0.3</version>
</dependency>
<dependency>
    <groupId>org.fhir</groupId>
    <artifactId>ucum.xpp3</artifactId>
    <version>1.0.3</version>
</dependency>
```

Android provides it's own XML Pull Parser implementation, use the `ucum.xmlpull.android` to avoid loading additional XML tools: 
```xml
<dependency>
    <groupId>org.fhir</groupId>
    <artifactId>ucum</artifactId>
    <version>1.0.3</version>
</dependency>
<dependency>
  <groupId>org.fhir</groupId>
  <artifactId>ucum.xmlpull</artifactId>
  <version>1.0.3</version>
</dependency>
<dependency>
    <groupId>org.fhir</groupId>
    <artifactId>ucum.xmlpull.android</artifactId>
    <version>1.0.3</version>
</dependency>
```

# Repository Layout

This repository contains the following modules:

| Module Name          | Description                                            |
| -------------------- |--------------------------------------------------------|
| ucum                 | Base implementation of Ucum                            | 
| ucum.xmlpull         | Ucum-essence Parser using XMLPull (not provided)       |
| ucum.xmlpull.xpp3    | Ucum-essence Parser using XMLPull from XPP3            |
| ucum.xmlpull.android | Ucum-essence Parser using XMLPull from the Android SDK |
| ucum.test-resources  | Shared Test resources                                  |

