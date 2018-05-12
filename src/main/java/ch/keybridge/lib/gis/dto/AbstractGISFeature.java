/*
 * Copyright 2018 Key Bridge.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.keybridge.lib.gis.dto;

import ch.keybridge.lib.xml.adapter.XmlMapStringAdapter;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * A generic, abstract GIS Data transfer object supporting styled GIS features.
 * <p>
 * This abstract class implements {@code simplestyle}, a a set of agreed-upon
 * 'special values' for the GeoJSON data standard that define how a GeoJSON
 * Feature or FeatureCollection should be rendered and styled on a slippy map.
 * <p>
 * The entirety of the {@code simplestyle} specification is optional so any
 * combination of specified properties are valid. When properties are not given
 * the defaults are assumed by implementations.
 * <p>
 * "Multi" features are assumed to have the same styling rules as single
 * features. That is: MultiPoints are styled as Points, MultiPolygons as
 * Polygons, and MultiLineStrings as LineStrings.
 * <p>
 * Marker styles do not affect stroke and fill rules and vice versa. The
 * behavior of fill rules on LineStrings is undefined but implementations are
 * encouraged to set fill to 0 by default in that case.
 * <p>
 * Colors may be presented in short form "#ace" or long form "#aaccee" with or
 * without the # prefix. Colors are interpreted the same as in CSS in #RRGGBB
 * and #RGB order. Other color formats or named colors are not supported.
 *
 * @author Key Bridge
 * @see <a href="https://github.com/mapbox/simplestyle-spec">SimplyStyle</a>
 * @since v1.2.0 created 10/10/17
 * @since v1.4.0 move id, name, type, description fields to AbstractGISFeature
 */
@XmlType(name = "AbstractGISFeature")
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractGISFeature implements Serializable, Comparable<AbstractGISFeature> {

  /**
   * The Feature Object lookup identifier.
   */
  @XmlElement(name = "ID")
  protected String id;

  /**
   * The geographic feature type. e.g. border, boundary, zone, etc. This is used
   * to categories features.
   */
  @XmlAttribute(name = "featureType")
  protected String featureType;

  /**
   * The feature name. This is a free-text, human readable name describing the
   * geographic feature. e.g. "Statue of Liberty".
   */
  @XmlElement(name = "Name")
  protected String name;

  /**
   * The feature description. This is a free-text, human readable description of
   * the geographic feature. e.g. "Statue of Liberty". This is typically a
   * (markdown) description to show when this item is clicked or hovered over.
   */
  @XmlElement(name = "Description")
  protected String description;

  /**
   * URI-encoded key value pairs.
   */
  @XmlElement(name = "Properties")
  @XmlJavaTypeAdapter(XmlMapStringAdapter.class)
  protected Map<String, String> properties;

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
   * The feature description. This is a free-text, human readable description of
   * the geographic feature. e.g. "Statue of Liberty". This is typically a
   * (markdown) description to show when this item is clicked or hovered over.
   *
   * @return the value. Default is null.
   */
  public String getDescription() {
    return description;
  }

  /**
   * A description to show when this item is clicked or hovered over.
   *
   * @param description the value
   */
  public void setDescription(String description) {
    this.description = description;
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
     * <p>
     * Update 05/24/17: sorted map no longer required as of gis-geojson-1.0.2.
     * Changed to HashMap because it has better performance characteristics
     * (constant time vs long(n) as in TreeMap for common operations).
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
   * Extracts the desired value encoded within the extension field addressable
   * by the key.
   *
   * @param key          the extension index KEY
   * @param defaultValue the default value if the KEY is not set
   * @return the value, null if not present
   */
  public String getProperty(String key, String defaultValue) {
    return isSetProperty(key) ? getProperties().get(key) : defaultValue;
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
   * Returns true if the properties contains a mapping for the specified key.
   *
   * @param key the key index
   * @return TRUE if a mapping exists for the key index
   */
  public final boolean isSetProperty(String key) {
    return getProperties().containsKey(key);
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
  public double getPropertyDouble(String key) {
    try {
      return Double.parseDouble(getProperty(key));
    } catch (NumberFormatException | NullPointerException exception) {
      return 0.0;
    }
  }

  /**
   * Returns a Double object holding the double value represented by the
   * argument string s.
   * <p>
   * Fails silently: If the value is null or misconfigured a
   * NumberFormatException is caught and NULL is returned.
   *
   * @param key          the key index
   * @param defaultValue the default value if not set
   * @return the value, null on error
   */
  public double getPropertyDouble(String key, double defaultValue) {
    try {
      return isSetProperty(key)
             ? Double.parseDouble(getProperty(key))
             : defaultValue;
    } catch (NumberFormatException | NullPointerException exception) {
      return 0.0;
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
   * @param properties a map of KEY/VALUE pairs
   */
  public final void setProperties(Map<String, String> properties) {
    clearProperties();
    if (properties != null) {
      getProperties().putAll(properties);
    }
  }

  /**
   * Add a collection of properties at once. This ammends the current extension
   * configuration.
   *
   * @param properties a map of KEY/VALUE pairs
   */
  public final void addProperties(Map<String, String> properties) {
    clearProperties();
    if (properties != null) {
      getProperties().putAll(properties);
    }
  }

  /**
   * Helper method to clear the metadata extensions.
   */
  public void clearProperties() {
    getProperties().clear();
  }

  /**
   * A title to show when this item is clicked or hovered over.
   *
   * @return the value. Default is null.
   */
  @XmlElement(name = "Title")
  public String getTitle() {
    return getProperty("title");
  }

  /**
   * A title to show when this item is clicked or hovered over.
   *
   * @param title the value. Default is null.
   */
  public void setTitle(String title) {
    setProperty("title", title);
  }

  /**
   * The size of the marker.
   * <p>
   * Sizes can be different pixel sizes in different implementations Value must
   * be one of [ "small", "medium", "large"] Default is "medium" if not set.
   *
   * @return the value.
   */
  @XmlElement(name = "MarkerSize")
  public String getMarkerSize() {
    return getProperty("markerSize", "medium");
  }

  /**
   * The size of the marker.
   * <p>
   * Sizes can be different pixel sizes in different implementations Value must
   * be one of ["small", "medium", "large"] Default is "medium" if not set.
   *
   * @param markerSize the value
   */
  public void setMarkerSize(String markerSize) {
    setProperty("markerSize", markerSize);
  }

  /**
   * A symbol to position in the center of this icon.
   * <p>
   * If not provided or null then no symbol is overlaid and only the marker is
   * shown. Allowed values include
   * <ul>
   * <li> Icon ID </li>
   * <li> An integer 0 through 9</li>
   * <li> A lowercase character "a" through "z"</li></ul>
   *
   * @return the value
   */
  @XmlElement(name = "MarkerSymbol")
  public String getMarkerSymbol() {
    return getProperty("markerSymbol");
  }

  /**
   * A symbol to position in the center of this icon.
   * <p>
   * If not provided or null then no symbol is overlaid and only the marker is
   * shown. Allowed values include
   * <ul>
   * <li> Icon ID </li>
   * <li> An integer 0 through 9</li>
   * <li> A lowercase character "a" through "z"</li></ul>
   *
   * @param markerSymbol the value
   */
  public void setMarkerSymbol(String markerSymbol) {
    setProperty("markerSymbol", markerSymbol);
  }

  /**
   * The marker's color.
   * <p>
   * Value must follow COLOR RULES. Default is "7e7e7e" if not set.
   *
   * @return the value.
   */
  @XmlElement(name = "MarkerColor")
  public String getMarkerColor() {
    return getProperty("markerColor", "7e7e7e");
  }

  /**
   * The marker's color.
   * <p>
   * Value must follow COLOR RULES. Default is "7e7e7e" if not set.
   *
   * @param markerColor the value
   */
  public void setMarkerColor(String markerColor) {
    setProperty("markerColor", markerColor);
  }

  /**
   * The color of a line as part of a polygon, polyline, or multigeometry
   * <p>
   * Value must follow COLOR RULES. Default is "555555" if not set.
   *
   * @return the value
   */
  @XmlElement(name = "Stroke")
  public String getStroke() {
    return getProperty("stroke", "555555");
  }

  /**
   * The color of a line as part of a polygon, polyline, or multigeometry
   * <p>
   * Value must follow COLOR RULES. Default is "555555" if not set.
   *
   * @param stroke the value
   */
  public void setStroke(String stroke) {
    setProperty("stroke", stroke);
  }

  /**
   * The opacity of the line component of a polygon, polyline, or multigeometry
   * <p>
   * Value must be a floating point number greater than or equal to zero and
   * less or equal to than one. Default is "1.0" if not set.
   *
   * @return the value
   */
  @XmlElement(name = "StrokeOpacity")
  public double getStrokeOpacity() {
    return getPropertyDouble("strokeOpacity", 1.0);
  }

  /**
   * The opacity of the line component of a polygon, polyline, or multigeometry
   * <p>
   * Value must be a floating point number greater than or equal to zero and
   * less or equal to than one. Default is "1.0" if not set.
   *
   * @param strokeOpacity the value
   */
  public void setStrokeOpacity(double strokeOpacity) {
    setProperty("strokeOpacity", strokeOpacity);
  }

  /**
   * The width of the line component of a polygon, polyline, or multigeometry
   * <p>
   * Value must be a floating point number greater than or equal to 0. Default
   * is "2" if not set.
   *
   * @return the value
   */
  @XmlElement(name = "StrokeWidth")
  public double getStrokeWidth() {
    return getPropertyDouble("strokeWidth", 2.0);
  }

  /**
   * The width of the line component of a polygon, polyline, or multigeometry
   * <p>
   * Value must be a floating point number greater than or equal to 0. Default
   * is "2" if not set.
   *
   * @param strokeWidth the value
   */
  public void setStrokeWidth(double strokeWidth) {
    setProperty("strokeWidth", strokeWidth);
  }

  /**
   * The color of the interior of a polygon.
   * <p>
   * Value must follow COLOR RULES. Default is "555555" if not set.
   *
   * @return the value
   */
  @XmlElement(name = "Fill")
  public String getFill() {
    return getProperty("fill", "555555");
  }

  /**
   * The color of the interior of a polygon.
   * <p>
   * Value must follow COLOR RULES. Default is "555555" if not set.
   *
   * @param fill the value
   */
  public void setFill(String fill) {
    setProperty("fill", fill);
  }

  /**
   * The opacity of the interior of a polygon. Implementations may choose to set
   * this to 0 for line features.
   * <p>
   * Value must be a floating point number greater than or equal to zero and
   * less or equal to than one. Default is "0.6" if not set.
   *
   * @return the value
   */
  @XmlElement(name = "FillOpacity")
  public double getFillOpacity() {
    return getPropertyDouble("fillOpacity", 0.6);
  }

  /**
   * The opacity of the interior of a polygon. Implementations may choose to set
   * this to 0 for line features.
   * <p>
   * Value must be a floating point number greater than or equal to zero and
   * less or equal to than one. Default is "0.6" if not set.
   *
   * @param fillOpacity the value
   */
  public void setFillOpacity(double fillOpacity) {
    setProperty("fillOpacity", fillOpacity);
  }

  /**
   * Hashcode and Equality are calculated from the ID parameter.
   *
   * @return a hashcode
   */
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + Objects.hashCode(this.id);
    return hash;
  }

  /**
   * Hashcode and Equality are calculated from the ID parameter.
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
    return Objects.equals(this.id, other.getId());
  }

  /**
   * Comparison is calculated from the NAME parameter.
   *
   * @param o the other object
   * @return a sorted, alphabetic comparison
   */
  @Override
  public int compareTo(AbstractGISFeature o) {
    if (this.name != null) {
      return this.name.compareTo(o.getName());
    }
    /**
     * Cannot compare. Assume not equal.
     */
    return -1;
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

}
