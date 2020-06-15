# lib-gis-dto

**GIS Data Transfer Objects**

A _Data Transfer Object_ is an object that is used to encapsulate data and send
it from one subsystem of an application to another.

![GISFeature](docs/images/gisFeature.png)

DTOs are most commonly used by the Services layer in an N-Tier application to transfer data between itself and the UI layer. In a distributed application a DTO provides a convenient, neutral message container that does not impose any design constraint on the receiving system.

The main benefit of a DTO is reducing the amount of data that must be exchanged in distributed applications. They also make great models in the MVC pattern.
A DTO is a dumb object - it just holds properties and has getters and setters, but no other logic of any significance (other than maybe a compare() or equals() implementation).

This library provide the following DTO classes:

  *  **FeatureCollection** - a collection of features
  *  **Feature** - a feature
  *  **Address** - a fully qualified (mailing) address
  *  **Position** - a fully qualified geographic position
  *  **Image** - describes a map image overlay
  *  **AbstractFeature** - provides support for map styles and other properties


## Uses

Key Bridge uses this the DTO classes in this library in our GIS API and other services.

**Schema**

Look in docs/xsd. Current is:

 * [XML Schema](gis-dto.2020-06-15.schema2.xsd)  XML Schema Document




## References

 * [Data Transfer Object](https://martinfowler.com/eaaCatalog/dataTransferObject.html)
 * [GeoJSON code snippets](docs/geojson/GeoJSON-methods.md)
 
# License

Copyright (C) Key Bridge. License is **Apache 2.0**.
 