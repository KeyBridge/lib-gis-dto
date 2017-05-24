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

import ch.keybridge.lib.xml.adapter.XmlEnvelopeAdapter;
import ch.keybridge.lib.xml.adapter.XmlGeometryAdapter;
import ch.keybridge.lib.xml.adapter.XmlMapStringAdapter;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * A Generic GIS Data transfer object. This simple container facilitates the
 * exchange of geographic features having an associated geometry.
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
 * <p>
 * 10/09/15 - rename from "GISObject" to "GISFeature", rename "type" field to
 * "featureType", rename "Extension" XML element to "Metadata"
 *
 * @author Jesse Caulfield
 * @since 12/17/14
 */
@XmlRootElement(name = "GISFeature")
@XmlType(name = "GISFeature")
@XmlAccessorType(XmlAccessType.FIELD)
public final class GISFeature implements Serializable, Comparable<GISFeature> {

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

  @XmlElement(name = "Address")
  private GISAddress address;

  @XmlElement(name = "Position")
  private GISPosition position;

  /**
   * The geometric shape of this GIS Object.
   * <p>
   * This is a JTS Geometry representing the 2D configuration of the location.
   * It is typically a POLYGON.
   * <p>
   * Note that the envelope is automatically provided by the Getter method
   * below.
   */
  @XmlElement(name = "Shape", required = true)
  @XmlJavaTypeAdapter(XmlGeometryAdapter.class)
  private Geometry geometry;

  /**
   * URI-encoded key value pairs.
   */
  @XmlElement(name = "Properties")
  @XmlJavaTypeAdapter(XmlMapStringAdapter.class)
  private Map<String, String> properties;

  /**
   * Construct a new GIS Object instance.
   */
  public GISFeature() {
  }

  /**
   * Construct a new GIS Object instance with minimal configuration.
   *
   * @param name     the name. e.g. Alaska
   * @param geometry the geometry
   */
  public GISFeature(String name, Geometry geometry) {
    this(null, null, name, geometry);
  }

  /**
   * Construct a new GIS Object instance with complete configuration (except
   * ID).
   *
   * @param featureType The object featureType. e.g. "border"
   * @param name        the name. e.g. "Alaska"
   * @param geometry    the geometry
   */
  public GISFeature(String featureType, String name, Geometry geometry) {
    this(null, featureType, name, geometry);
  }

  /**
   * Construct a new GIS Object instance with complete configuration (except
   * ID).
   *
   * @param id          the object feature id
   * @param featureType The object featureType. e.g. "border"
   * @param name        the name. e.g. "Alaska"
   * @param geometry    the geometry
   */
  public GISFeature(String id, String featureType, String name, Geometry geometry) {
    this.id = id;
    this.featureType = featureType;
    this.name = name;
    this.geometry = geometry;
  }

  /**
   * Construct a new GIS Object instance supporting GEOCODING.
   *
   * @param featureType the feature type
   * @param name        the feature name
   * @param address     the address
   * @param position    the position
   * @param geometry    the geometry
   */
  public GISFeature(String featureType, String name, GISAddress address, GISPosition position, Geometry geometry) {
    this.featureType = featureType;
    this.name = name;
    this.address = address;
    this.position = position;
    this.geometry = geometry;
  }

  /**
   * Get the geometric shape of this GIS Object.
   *
   * @return the configured geometry
   */
  public Geometry getGeometry() {
    return geometry;
  }

  /**
   * Set the geometric shape of this GIS Object.
   *
   * @param geometry any valid geometry type
   */
  public void setGeometry(Geometry geometry) {
    this.geometry = geometry;
  }

  /**
   * The envelope of the geometric shape of this GIS Object. This marshals as
   * {@code [Xmin, Ymin, Xmax, Ymax]}, which is the format used by GML and WFS.
   * <p>
   * @return this returns a JTS envelope.
   */
  @XmlElement(name = "Envelope", required = true)
  @XmlJavaTypeAdapter(XmlEnvelopeAdapter.class)
  public Envelope getEnvelope() {
    return this.geometry != null ? this.geometry.getEnvelopeInternal() : null;
  }

  /**
   * Get the position
   *
   * @return the position
   */
  public GISPosition getPosition() {
    return position;
  }

  /**
   * Set the position
   *
   * @param position the position
   */
  public void setPosition(GISPosition position) {
    this.position = position;
  }

  /**
   * Indicator that the position is configured.
   *
   * @return TRUE if the position is not null.
   */
  public boolean isSetCoordinate() {
    return this.position != null;
  }

  /**
   * Get the Address.
   *
   * @return the Address
   */
  public GISAddress getAddress() {
    return address;
  }

  /**
   * Set the address
   *
   * @param address the Address
   */
  public void setAddress(GISAddress address) {
    this.address = address;
  }

  /**
   * Indicator that the address is configured.
   *
   * @return TRUE if the address is not null.
   */
  public boolean isSetAddress() {
    return this.address != null;
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
   * Get a sorted Map containing all of the extensions in this GISFeature
   * container.
   *
   * @return a non-null TreeMap instance
   */
  public Map<String, String> getProperties() {
    /**
     * Developer note: A sorted TreeMap is required for GeoJSON encoding as the
     * key/value pairs are formatted and encoded separately. Sorting is required
     * to preserve the association.
     * Update: sorted map no longer required as of gis-geojson-1.0.2. Changed to HashMap
     * because it has better performance characteristics (constant time vs long(n)
     * as in TreeMap for common operations).
     */
    if (properties == null) {
      properties = new HashMap<>();
    }
    return properties;
  }

  /**
   * Extracts the desired value encoded within the extension field addressable
   * by the key.
   *
   * @param key the extension index KEY
   * @return the value, null if not present
   */
  public String getProperty(String key) {
    return getProperties().get(key);
  }

  /**
   * Encodes the provided value within the extension, associated with the
   * provided key. This method will discard the entry if the key is null or
   * empty.
   *
   * @param key   the key
   * @param value the value
   */
  public final void setProperty(String key, Object value) {
    if (key != null && !key.isEmpty()) {
      if (value == null) {
        getProperties().remove(key);
      } else if (value instanceof Date) {
        getProperties().put(key, new SimpleDateFormat().format((Date) value));
      } else {
        getProperties().put(key, String.valueOf(value));
      }
    }
  }

  /**
   * Returns a Boolean with a value represented by the specified string. The
   * Boolean returned represents a true value if the string argument is not null
   * and is equal, ignoring case, to the string "true".
   *
   * @param key the key index
   * @return the value, FALSE if not set.
   */
  public Boolean getPropertyBoolean(String key) {
    return Boolean.valueOf(getProperty(key));
  }

  /**
   * Returns a Date with a value represented by the specified string.
   *
   * @param key the key index
   * @return the value, NULL if not set.
   */
  public Date getPropertyDate(String key) {
    try {
      return new SimpleDateFormat().parse(getProperty(key));
    } catch (ParseException | NullPointerException exception) {
      return null;
    }
  }

  /**
   * Returns a Double object holding the double value represented by the
   * argument string s.
   * <p>
   * Fails silently: If the value is null or misconfigured a
   * NumberFormatException is caught and NULL is returned.
   *
   * @param key the key index
   * @return the value, null on error
   */
  public Double getPropertyDouble(String key) {
    try {
      return Double.valueOf(getProperty(key));
    } catch (NumberFormatException | NullPointerException exception) {
      return null;
    }
  }

  /**
   * Returns an Integer object holding the value of the specified String. The
   * argument is interpreted as representing a signed decimal integer, exactly
   * as if the argument were given to the Integer.parseInt(java.lang.String)
   * method. The result is an Integer object that represents the integer value
   * specified by the string.
   * <p>
   * Fails silently: If the value is null or misconfigured a
   * NumberFormatException is caught and NULL is returned.
   *
   * @param key the key index
   * @return the value, null on error
   */
  public Integer getPropertyInteger(String key) {
    try {
      return Integer.valueOf(getProperty(key));
    } catch (NumberFormatException | NullPointerException exception) {
      return null;
    }
  }

  /**
   * Returns a Long object holding the value of the specified String. The
   * argument is interpreted as representing a signed decimal long, exactly as
   * if the argument were given to the Long.parseLong(java.lang.String) method.
   * The result is a Long object that represents the integer value specified by
   * the string.
   * <p>
   * Fails silently: If the value is null or misconfigured a
   * NumberFormatException is caught and NULL is returned.
   *
   * @param key the key index
   * @return the value, null on error
   */
  public Long getPropertyLong(String key) {
    try {
      return Long.valueOf(getProperty(key));
    } catch (NumberFormatException | NullPointerException exception) {
      return null;
    }
  }

  /**
   * Set all extensions at once. This replaces the current extension
   * configuration.
   *
   * @param extensions a map of KEY/VALUE pairs
   */
  public final void setProperties(Map<String, String> extensions) {
    clearProperties();
    if (extensions != null) {
      getProperties().putAll(extensions);
    }
  }

  /**
   * Helper method to clear the metadata extensions.
   */
  public void clearProperties() {
    getProperties().clear();
  }

  /**
   * Get the 2-character ISO 3166-1 alpha-2 country code.
   *
   * @return the ISO2 country code. e.g. 'US'
   */
  public String getIso2() {
    return getProperty("iso2");
  }

  /**
   * Set the 2-character ISO 3166-1 alpha-2 country code.
   *
   * @param iso2 the ISO2 country code. e.g. 'US'
   */
  public void setIso2(String iso2) {
    setProperty("iso2", iso2);
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
    final GISFeature other = (GISFeature) obj;
    return Objects.equals(this.name, other.getName());
  }

  /**
   * Hashcode, Equality and Comparison are calculated from the NAME parameter.
   *
   * @param o the other object
   * @return a sorted, alphabetic comparison
   */
  @Override
  public int compareTo(GISFeature o) {
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
    return "GISFeature"
            + " id [" + id
            + "] type [" + featureType
            + "] name [" + name
            + "] geometry [" + (geometry != null ? geometry.getGeometryType() + "[" + geometry.getCoordinates().length + "]" : "")
            + "] metadata [" + properties
            + ']';
  }

}
