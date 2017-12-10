/*
 *  Copyright (C) 2014 Caulfield IP Holdings (Caulfield) and/or its affiliates.
 *  All rights reserved. Use is subject to license terms.
 *
 *  Software Code is protected by Caulfield Copyrights. Caulfield hereby reserves
 *  all rights in and to Caulfield Copyrights and no license is granted under
 *  Caulfield Copyrights in this Software License Agreement. Caulfield generally
 *  licenses Caulfield Copyrights for commercialization pursuant to the terms of
 *  either Caulfield's Standard Software Source Code License Agreement or
 *  Caulfield's Standard Product License Agreement.
 *
 *  A copy of either License Agreement can be obtained on request by email from:
 *  info@caufield.org.
 */
package ch.keybridge.lib.gis.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import javax.xml.bind.annotation.*;

/**
 * A Generic GIS Data transfer object for collections of GIS Features. This
 * simple container facilitates the exchange of geographic features having an
 * associated geometry.
 * <p>
 * A <em>data transfer object</em> is an object that carries data between
 * processes. The motivation for its use has to do with the fact that
 * communication between processes is usually done resorting to remote
 * interfaces (e.g. web services), where each call is an expensive operation.
 * Because the majority of the cost of each call is related to the round-trip
 * time between the client and the server, one way of reducing the number of
 * calls is to use an object (the DTO) that aggregates the data that would have
 * been transferred by the several calls, but that is served by one call only.
 * <p>
 * The difference between data transfer objects and business objects or data
 * access objects is that a DTO does not have any behavior except for storage
 * and retrieval of its own data (accessors and mutators). DTOs are simple
 * objects that should not contain any business logic that would require
 * testing.
 *
 * @author Jesse Caulfield
 * @since v1.3.4 created 09/01/16
 * @since v1.2.0 updated 12/10/17 to extend AbstractGISFeature with properties
 * and styling
 */
@XmlRootElement(name = "GISFeatureCollection")
@XmlType(name = "GISFeatureCollection")
@XmlAccessorType(XmlAccessType.FIELD)
public final class GISFeatureCollection extends AbstractGISFeature implements Serializable, Comparable<GISFeatureCollection> {

  private static final long serialVersionUID = 1L;

  /**
   * The Feature Object lookup identifier.
   */
  @XmlElement(name = "ID")
  private String id;

  /**
   * The geographic feature type. e.g. border, boundary, zone, etc. This is used
   * to categories features.
   */
  @XmlAttribute(name = "featureType")
  private String featureType;

  /**
   * The feature name. This is a free-text, human readable name describing the
   * geographic feature. e.g. "Statue of Liberty".
   */
  @XmlElement(name = "Name")
  private String name;

  @XmlElementWrapper(name = "Features")
  @XmlElement(name = "Feature")
  protected Collection<GISFeature> features;

  /**
   * Construct a new GIS Feature Collection instance.
   */
  public GISFeatureCollection() {
  }

  /**
   * Construct a new Construct a new GIS Feature Collection instance with
   * complete configuration.
   *
   * @param id          the object feature id
   * @param featureType The object featureType. e.g. "border"
   * @param name        the name. e.g. "Alaska"
   * @param features    a collection of features
   * @return a GISFeatureCollection instance
   */
  public static GISFeatureCollection getInstance(String id, String featureType, String name, Collection<GISFeature> features) {
    GISFeatureCollection collection = new GISFeatureCollection();
    collection.setId(id);
    collection.setFeatureType(featureType);
    collection.setName(name);
    collection.setFeatures(features);
    return collection;
  }

  /**
   * Get the optional ID attribute. This may be the OSM ID, etc.
   *
   * @return the ID attribute
   */
  public String getId() {
    return id;
  }

  /**
   * Set the optional ID attribute. This may be the OSM ID, etc.
   * <p>
   * Note that the provided ID is converted to a String and its object type is
   * lost in translation.
   *
   * @param id the ID attribute
   */
  public void setId(Object id) {
    this.id = String.valueOf(id);
  }

  /**
   * Get the feature name. This is a free-text, human readable name describing
   * the geographic feature. e.g. "Statue of Liberty".
   *
   * @return the feature name.
   */
  public String getName() {
    return name;
  }

  /**
   * Set the feature name. This is a free-text, human readable name describing
   * the geographic feature. e.g. "Statue of Liberty".
   *
   * @param name the feature name.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get the geographic feature type. e.g. border, boundary, zone, etc.
   *
   * @return the geographic feature type
   */
  public String getFeatureType() {
    return featureType;
  }

  /**
   * Set the geographic feature type. e.g. border, boundary, zone, etc.
   *
   * @param featureType the geographic feature type
   */
  public void setFeatureType(String featureType) {
    this.featureType = featureType;
  }

  /**
   * Get the Features collection.
   *
   * @return a non-null TreeSet.
   */
  public Collection<GISFeature> getFeatures() {
    if (this.features == null) {
      this.features = new LinkedHashSet<>();
    }
    return features;
  }

  /**
   * Set the Features collection
   *
   * @param features the a collection of features.
   */
  public void setFeatures(Collection<GISFeature> features) {
    this.features = features;
  }

  /**
   * Set the Features collection
   *
   * @param features the a collection of features.
   */
  public void addFeatures(GISFeature... features) {
    if (features != null) {
      getFeatures().addAll(Arrays.asList(features));
    }
  }

  /**
   * Hashcode, Equality and Comparison are calculated from the NAME parameter.
   *
   * @return a hashcode
   */
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + Objects.hashCode(this.name);
    return hash;
  }

  /**
   * Hashcode, Equality and Comparison are calculated from the NAME parameter.
   *
   * @param obj the other object
   * @return TRUE if the names are equal
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final GISFeatureCollection other = (GISFeatureCollection) obj;
    return Objects.equals(this.name, other.getName());
  }

  /**
   * Hashcode, Equality and Comparison are calculated from the NAME parameter.
   *
   * @param o the other object
   * @return a sorted, alphabetic comparison
   */
  @Override
  public int compareTo(GISFeatureCollection o) {
    if (this.name != null) {
      return this.name.compareTo(o.getName());
    }
    /**
     * Cannot compare. Assume equal.
     */
    return 0;
  }

  /**
   * ToString returns the GIS feature name.
   *
   * @return the GIS feature name.
   */
  @Override
  public String toString() {
    return name;
  }

  public String toStringFull() {
    return "GISFeatureCollection"
      + " id [" + id
      + "] type [" + featureType
      + "] name [" + name
      + "] metadata [" + getProperties()
      + ']';
  }

}
