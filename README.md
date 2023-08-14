# Ucum-java

[![Build Status](https://dev.azure.com/fhir-pipelines/ucum-java/_apis/build/status%2FMaster%20Branch%20Pipeline?branchName=master)](https://dev.azure.com/fhir-pipelines/ucum-java/_build/latest?definitionId=51&branchName=master)

FHIR Java library providing UCUM Services

The library provides a set of services around UCUM:

- validate a UCUM unit (and also against a particular base unit)
- decide whether one unit can be converted/compared to another
- translate a quantity from one unit to another 
- prepare a human readable display of a unit 
- multiply 2 quantities together

To use the library, download ucum-essence from http://unitsofmeasure.org, and then create a UCUMEssenceService:

UcumService ucumSvc = new UcumEssenceService(definitionFile);
