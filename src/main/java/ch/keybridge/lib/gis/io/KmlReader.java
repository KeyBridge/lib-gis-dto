/*
 * Copyright 2018 Key Bridge. All rights reserved. Use is subject to license
 * terms.
 *
 * This software code is protected by Copyrights and remains the property of
 * Key Bridge and its suppliers, if any. Key Bridge reserves all rights in and to
 * Copyrights and no license is granted under Copyrights in this Software
 * License Agreement.
 *
 * Key Bridge generally licenses Copyrights for commercialization pursuant to
 * the terms of either a Standard Software Source Code License Agreement or a
 * Standard Product License Agreement. A copy of either Agreement can be
 * obtained upon request by sending an email to info@keybridgewireless.com.
 *
 * All information contained herein is the property of Key Bridge and its
 * suppliers, if any. The intellectual and technical concepts contained herein
 * are proprietary.
 */
package ch.keybridge.lib.gis.io;

import ch.keybridge.lib.gis.dto.AbstractGISFeature;
import ch.keybridge.lib.gis.dto.GISFeature;
import ch.keybridge.lib.gis.dto.GISFeatureCollection;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.opengis.kml.*;

/**
 * KML reader.
 * <p>
 * This utility reads KML files and returns a collection of
 * {@code GISFeatureCollection} and {@code GISFeature} DTO object instances.
 *
 * @author Key Bridge
 * @since v1.4.0 added 05/08/18 to support KML ETL
 */
public class KmlReader {

  /**
   * Supplies a set of utility methods for building Geometry objects from lists
   * of Coordinates.
   * <p>
   * This factory is constructed with a precision model supporting up to 6
   * significant digits. Precision is decimal places + 1.
   */
  private final GeometryFactory geometryFactory;

  public KmlReader() {
    /**
     * Initialize the GeometryFactory with a precision model.
     */
    this.geometryFactory = new GeometryFactory(new PrecisionModel(Math.pow(10, 7)));
  }

  /**
   * Transform a KML file into a collection of well-formed GIS DTO objects.
   * <p>
   * This transformer converts Kml {@code Folder} into
   * {@code GISFeatureCollection} DTO and Kml {@code Placemark} into
   * {@code GISFeature} DTO.
   * <p>
   * Use the {@code unmarshal} methods in the Kml file to read files.
   *
   * @param kml a KML document object
   * @return a non-null ArrayList
   */
  public Collection<AbstractGISFeature> transformKml(Kml kml) {
    /**
     * Initialize an abstract collection of GSI DTO features.
     */
    Collection<AbstractGISFeature> gisFeatures = new ArrayList<>();
    /**
     * Get the Kml document. The document should NEVER be null, but check just
     * in case.
     */
    Document document = (Document) kml.getFeature();
    if (document == null) {
      return gisFeatures;
    }
    /**
     * Scan the document features. We are only looking for Folders, which
     * contain a collection of Placemarks, and Placemarks.
     */
    for (Feature feature : document.getFeature()) {
      /**
       * If the feature is a Folder then process the folder contents as a
       * collection of features, i.e. a FeatureCollection :). A Folder is a
       * Feature with a collection of Placemarks.
       */
      if (feature instanceof Folder) {
        GISFeatureCollection gisFeatureCollection = transformFolder((Folder) feature);
        System.out.println(gisFeatureCollection);
        /**
         * Collect the feature collection.
         */
        gisFeatures.add(gisFeatureCollection);
      }

      /**
       * If the feature is a Placemark then process the placemark. A Placemark
       * is a Feature with an associated Geometry.
       */
      if (feature instanceof Placemark) {
        Placemark placemark = (Placemark) feature;
        GISFeature gisFeature = transformPlacemark(placemark);
        /**
         * Collect the feature.
         */
        gisFeatures.add(gisFeature);
      }

      /**
       * Other possible features are mostly for display and eye candy:
       * <pre>
       * Container
       * Document
       * Folder           (capture)
       * GroundOverlay
       * NetworkLink
       * Overlay
       * PhotoOverlay
       * Placemark        (capture)
       * ScreenOverlay
       * Tour
       * </pre>
       */
    }

    return gisFeatures;
  }

  /**
   * Transform a Folder into a GISFeatureCollection instance.
   * <p>
   * A Folder is used to arrange other Features hierarchically (Folders,
   * Placemarks, NetworkLinks, or Overlays). A Feature is visible only if it and
   * all its ancestors are visible.
   *
   * @param folder a KML Folder
   * @return a GISFeatureCollection instance.
   */
  protected GISFeatureCollection transformFolder(Folder folder) {
    /**
     * Construct a new GIS Feature. If the feature has extended data then set
     * the feature properties.
     */
    GISFeatureCollection featureCollection = new GISFeatureCollection();
    featureCollection.setId(folder.getId());
    featureCollection.setName(folder.getName());
    featureCollection.setDescription(folder.getDescription());
    featureCollection.addProperties(transformExtendedData(folder));
    /**
     * We only care about the placemark contents of the folder.
     */
    for (Feature feature : folder.getFeature()) {
      /**
       * Transform the feature if it is a Placemark.
       */
      if (feature instanceof Placemark) {
        Placemark placemark = (Placemark) feature;
        featureCollection.addFeatures(transformPlacemark(placemark));
      }
    }
    return featureCollection;
  }

  /**
   * Transform a KML Placemark into a GISFeature entity.
   * <p>
   * A Placemark is a Feature with associated Geometry.
   *
   * @param placemark a KML Placemark
   * @return a GISFeature instance
   */
  protected GISFeature transformPlacemark(Placemark placemark) {
    /**
     * Construct a new GIS Feature. If the feature has extended data then set
     * the feature properties.
     */
    GISFeature feature = new GISFeature();
    feature.setId(placemark.getId());
    feature.setName(placemark.getName());
    feature.setDescription(placemark.getDescription());
    feature.addProperties(transformExtendedData(placemark));

    /**
     * A string value representing an unstructured address written as a standard
     * street, city, state address, and/or as a postal code. You can use the
     * <address> tag to specify the location of a point instead of using
     * latitude and longitude coordinates.
     */
//    String address = placemark.getAddress();
    /**
     * Set the geometry.
     */
    feature.setGeometry(transformGeometry(placemark.getGeometry()));
    /**
     * Done.
     */
    return feature;
  }

  /**
   * Transform a Feature ExtendedData element to a simple Map. This method
   * copies the extended data [name, value] elements into a simple HashMap.
   *
   * @param feature a Feature instance
   * @return a non-null HashMap
   */
  protected Map<String, String> transformExtendedData(Feature feature) {
    Map<String, String> properties = new HashMap<>();
    if (feature.getExtendedData() != null) {
      for (Data data : feature.getExtendedData().getData()) {
        properties.put(data.getName(), data.getValue());
      }
    }
    return properties;
  }

  /**
   * Transform a KML geometry type to a JTS geometry type
   *
   * @param kmlGeometry a KML geometry type
   * @return a JTS geometry type
   */
  protected com.vividsolutions.jts.geom.Geometry transformGeometry(Geometry kmlGeometry) {
    com.vividsolutions.jts.geom.Geometry geometry = null;

    switch (kmlGeometry.getClass().getSimpleName()) {
      /**
       * A closed line string, typically the outer boundary of a Polygon.
       */
      case "LinearRing": {
        geometry = transformLinearRing((LinearRing) kmlGeometry);
        break;
      }
      /**
       * A connected set of line segments.
       */
      case "LineString": {
        geometry = transformLineString((LineString) kmlGeometry);
        break;
      }
      /**
       * Model is a Google Earth proprietary 3D object described in a COLLADA
       * file. Models are created in their own coordinate space and then
       * located, positioned, and scaled in Google Earth.
       */
      case "Model": {
        // NOT SUPPORTED
        break;
      }
      /**
       * A container for zero or more geometry primitives associated with the
       * same feature.
       */
      case "MultiGeometry": {
        geometry = transformMultiGeometry((MultiGeometry) kmlGeometry);
        break;
      }
      /**
       * Track is Google Earth proprietary object.
       */
      case "MultiTrack": {
        // NOT SUPPORTED
        break;
      }
      /**
       * A geographic location defined by longitude, latitude, and (optional)
       * altitude.
       */
      case "Point": {
        geometry = transformPoint((Point) kmlGeometry);
        break;
      }
      /**
       * A Polygon is defined by an outer boundary and 0 or more inner
       * boundaries. The boundaries, in turn, are defined by LinearRings.
       */
      case "Polygon": {
        geometry = transformPolygon((Polygon) kmlGeometry);
        break;
      }
      /**
       * Track is Google Earth proprietary object.
       */
      case "Track": {
        // NOT SUPPORTED
      }
      default: {
        throw new IllegalArgumentException("Geometry type " + kmlGeometry.getClass().getSimpleName() + " not supported yet.");
      }

    }
    /**
     * Done.
     */
    return geometry;

  }

  /**
   * Transform a KML Point to a JS Point.
   *
   * @param point a KML Point
   * @return a JS Point
   */
  protected com.vividsolutions.jts.geom.Point transformPoint(Point point) {
    Coordinate coordinate = point.getCoordinates().get(0);
    return geometryFactory.createPoint(transformCoordinate(coordinate));
  }

  /**
   * Transform a KML MultiGeometry to a JTS GeometryCollection.
   * <p>
   * MultiGeometry is a container for zero or more geometry primitives
   * associated with the same feature.
   *
   * @param multiGeometry a KML MultiGeometry
   * @return a JTS GeometryCollection
   */
  protected GeometryCollection transformMultiGeometry(MultiGeometry multiGeometry) {
    Collection<com.vividsolutions.jts.geom.Geometry> geometries = new ArrayList<>();
    for (Geometry geometry : multiGeometry.getGeometry()) {
      geometries.add(transformGeometry(geometry));
    }
    return geometryFactory.createGeometryCollection(geometries.toArray(new com.vividsolutions.jts.geom.Geometry[geometries.size()]));
  }

  /**
   * Transform a KML LineString to a JTS LineString geometry.
   *
   * @param lineString a KML LineString
   * @return a JTS LineString
   */
  protected com.vividsolutions.jts.geom.LineString transformLineString(LineString lineString) {
    Collection<com.vividsolutions.jts.geom.Coordinate> coordinates = new ArrayList<>();
    for (Coordinate coordinate : lineString.getCoordinates()) {
      coordinates.add(transformCoordinate(coordinate));
    }
    return geometryFactory.createLineString(coordinates.toArray(new com.vividsolutions.jts.geom.Coordinate[coordinates.size()]));
  }

  /**
   * Transform a KML LinearRing to a JTS LinearRing geometry.
   *
   * @param linearRing a KML LinearRing
   * @return a JTS LinearRing
   */
  protected com.vividsolutions.jts.geom.LinearRing transformLinearRing(LinearRing linearRing) {
    Collection<com.vividsolutions.jts.geom.Coordinate> coordinates = new ArrayList<>();
    for (Coordinate coordinate : linearRing.getCoordinates()) {
      coordinates.add(transformCoordinate(coordinate));
    }
    return geometryFactory.createLinearRing(coordinates.toArray(new com.vividsolutions.jts.geom.Coordinate[coordinates.size()]));
  }

  /**
   * Transform a KML Polygon to a JTS polygon geometry.
   *
   * @param polygon a KML Polygon
   * @return a JTS polygon
   */
  protected com.vividsolutions.jts.geom.Polygon transformPolygon(Polygon polygon) {
    /**
     * Outer ring
     */
    com.vividsolutions.jts.geom.LinearRing outerRing = transformLinearRing(polygon.getOuterBoundaryIs().getLinearRing());
    /**
     * Inner rings. A Polygon can contain multiple innerBoundaryIs elements,
     * which create multiple cut-outs inside the Polygon.
     */
    Collection<com.vividsolutions.jts.geom.LinearRing> innerRings = new ArrayList<>();
    for (Boundary innerBoundary : polygon.getInnerBoundaryIs()) {
      com.vividsolutions.jts.geom.LinearRing innerRing = transformLinearRing(innerBoundary.getLinearRing());
      innerRings.add(innerRing);
    }
    /**
     * Constructs a Polygon with the given exterior boundary and interior
     * boundaries.
     */
    return geometryFactory.createPolygon(outerRing, innerRings.toArray(new com.vividsolutions.jts.geom.LinearRing[innerRings.size()]));
  }

  /**
   * Transform a KML coordinate to a JTS coordinate.
   *
   * @param coordinate a KML coordinate
   * @return a JTS coordinate.
   */
  protected com.vividsolutions.jts.geom.Coordinate transformCoordinate(Coordinate coordinate) {
    return new com.vividsolutions.jts.geom.Coordinate(coordinate.getLongitude(),
                                                      coordinate.getLatitude(),
                                                      coordinate.getAltitude());
  }

}
