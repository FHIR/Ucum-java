# Ucum-java
[![OpenSSF Scorecard](https://api.scorecard.dev/projects/github.com/FHIR/Ucum-java/badge)](https://scorecard.dev/viewer/?uri=github.com/FHIR/Ucum-java)

|                           CI Status (master)                           |                                            Current Release |      Latest SNAPSHOT       |
|:----------------------------------------------------------------------:|-----------------------------------------------------------:|:--------------------------:|
| [![Build Status][Badge-AzureMasterPipeline]][Link-AzureMasterPipeline] | [![Badge-MavenCentralReleases]][Link-MavenCentralReleases] | ![Badge-SonatypeSnapshots] |


FHIR Java library providing UCUM Services

The library provides a set of services around UCUM:

- validate a UCUM unit (and also against a particular base unit)
- decide whether one unit can be converted/compared to another
- translate a quantity from one unit to another 
- prepare a human readable display of a unit 
- multiply 2 quantities together

To use the library, download ucum-essence from http://unitsofmeasure.org, and then create a UCUMEssenceService:

UcumService ucumSvc = new UcumEssenceService(definitionFile);

[Link-AzureMasterPipeline]: https://dev.azure.com/fhir-pipelines/ucum-java/_build/latest?definitionId=51&branchName=master
[Badge-AzureMasterPipeline]: https://dev.azure.com/fhir-pipelines/ucum-java/_apis/build/status%2FMaster%20Branch%20Pipeline?branchName=master
[Link-MavenCentralReleases]: https://central.sonatype.com/artifact/org.fhir/ucum
[Badge-MavenCentralReleases]: https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Frepo1.maven.org%2Fmaven2%2Forg%2Ffhir%2Fucum%2Fmaven-metadata.xml "Maven Central Releases"
[Badge-SonatypeSnapshots]:https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Forg%2Ffhir%2Fucum%2Fmaven-metadata.xml "Sonatype Snapshots"
