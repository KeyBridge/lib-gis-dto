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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
 * @since v1.4.0 move id, name, type, description fields to AbstractGISFeature
 */
@XmlRootElement(name = "FeatureCollection")
@XmlType(name = "FeatureCollection")
@XmlAccessorType(XmlAccessType.FIELD)
public final class FeatureCollection extends AbstractFeature {

  private static final long serialVersionUID = 1L;

  /**
   * The collection of GISFeature. This is a simple ArrayList; sorting and
   * uniqueness should be externally established.
   */
  @XmlElementWrapper(name = "Features")
  @XmlElement(name = "Feature")
  protected Collection<Feature> features;

  /**
   * Construct a new GIS Feature Collection instance.
   */
  public FeatureCollection() {
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
  public static FeatureCollection getInstance(String id, String featureType, String name, Collection<Feature> features) {
    FeatureCollection collection = new FeatureCollection();
    collection.setId(id);
    collection.setFeatureType(featureType);
    collection.setName(name);
    collection.setFeatures(features);
    return collection;
  }

  /**
   * Get a GISFeatureCollection instance with CSS configurations. This
   * facilitates rendering when conversion to GeoJSON.
   *
   * @return a GISFeatureCollection with stroke and fill configurations.
   */
  public static FeatureCollection getInstanceWithCss() {
    FeatureCollection f = new FeatureCollection();
    f.setMarkerSize("medium");
    f.setMarkerColor("7e7e7e");
    f.setStroke("999999");
    f.setStrokeOpacity(1.0);
    f.setStrokeWidth(2.0);
    f.setFill("555555");
    f.setFillOpacity(0.6);
    return f;
  }

  /**
   * Get the Features collection.
   *
   * @return a non-null ArrayList.
   */
  public Collection<Feature> getFeatures() {
    if (this.features == null) {
      this.features = new ArrayList<>();
    }
    return features;
  }

  /**
   * Set the Features collection
   *
   * @param features the a collection of features.
   */
  public void setFeatures(Collection<Feature> features) {
    this.features = features;
  }

  /**
   * Set the Features collection
   *
   * @param features the a collection of features.
   */
  public void addFeatures(Feature... features) {
    if (features != null) {
      getFeatures().addAll(Arrays.asList(features));
    }
  }

}
