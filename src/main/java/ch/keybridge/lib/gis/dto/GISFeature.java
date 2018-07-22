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

import ch.keybridge.xml.adapter.XmlEnvelopeAdapter;
import ch.keybridge.xml.adapter.XmlGeometryAdapter;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
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
 * @since v0.0.1 created 12/17/14
 * @since v1.2.0 updated 12/10/17 to extend AbstractGISFeature with properties
 * and styling
 * @since v1.4.0 move id, name, type, description fields to AbstractGISFeature
 */
@XmlRootElement(name = "GISFeature")
@XmlType(name = "GISFeature")
@XmlAccessorType(XmlAccessType.FIELD)
public final class GISFeature extends AbstractGISFeature {

  private static final long serialVersionUID = 1L;

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
   * Construct a new GIS Object instance.
   */
  public GISFeature() {
  }

  /**
   * Construct a new GISFeature instance with minimal configuration.
   *
   * @param name     the name. e.g. Alaska
   * @param geometry the geometry
   * @return a new GISFeature instance
   */
  public static GISFeature getInstance(String name, Geometry geometry) {
    GISFeature f = new GISFeature();
    f.setName(name);
    f.setGeometry(geometry);
    return f;
  }

  /**
   * Construct a new GIS Object instance supporting GEOCODING.
   *
   * @param featureType the feature type
   * @param name        the feature name
   * @param address     the address
   * @param position    the position
   * @param geometry    the geometry
   * @return a new GISFeature instance
   */
  public static GISFeature getInstance(String featureType, String name, GISAddress address, GISPosition position, Geometry geometry) {
    GISFeature f = new GISFeature();
    f.setFeatureType(featureType);
    f.setName(name);
    f.setAddress(address);
    f.setPosition(position);
    f.setGeometry(geometry);
    return f;
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
  public boolean isSetPosition() {
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
   * Get the 2-character ISO 3166-1 alpha-2 country code.
   *
   * @return the ISO2 country code. e.g. 'US'
   */
  @XmlElement(name = "Country")
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

}
