/*
 * Copyright 2016 Key Bridge.
 *
 * All rights reserved. Use is subject to license terms.
 * This software is protected by copyright.
 *
 * See the License for specific language governing permissions and
 * limitations under the License.
 */
package ch.keybridge.lib.gis.io;

import ch.keybridge.lib.gis.dto.GISFeature;
import ch.keybridge.lib.gis.dto.GISFeatureCollection;
import com.vividsolutions.jts.geom.*;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.Map;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.LenientFeatureFactoryImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geometry.jts.Geometries;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Utility class to simplify the writing of GeoJSON-formatted objects.
 * <p>
 * GeoJSON is a format for encoding a variety of geographic data structures.
 * GeoJSON supports the following geometry types: Point, LineString, Polygon,
 * MultiPoint, MultiLineString, and MultiPolygon. Geometric objects with
 * additional properties are Feature objects. Sets of features are contained by
 * FeatureCollection objects.
 * <p>
 * GeoJSON is a geospatial data interchange format based on JavaScript Object
 * Notation (JSON). It defines several types of JSON objects and the manner in
 * which they are combined to represent data about geographic features, their
 * properties, and their spatial extents. GeoJSON uses a geographic coordinate
 * reference system, World Geodetic System 1984, and units of decimal degrees.
 * <ul>
 * <li>A GeoJSON text is a JSON text and consists of a single GeoJSON
 * object.</li>
 * <li>A GeoJSON object represents a Geometry, Feature, or collection of
 * Features.</li>
 * </ul>
 * <strong>Accuracy &amp; Precision</strong>
 * <p>
 * For geographic coordinates with units of degrees, 6 decimal places (a default
 * common in, e.g., sprintf) amounts to about 10 centimeters, a precision well
 * within that of current GPS systems. Implementations should consider the cost
 * of using a greater precision than necessary.
 * <p>
 * Furthermore, the WGS 84 [WGS84] datum is a relatively coarse approximation of
 * the geoid, with the height varying by up to 5 m (but generally between 2 and
 * 3 meters) higher or lower relative to a surface parallel to Earth's mean sea
 * level.
 * <p>
 * <strong>Usage</strong>
 * <pre>
 * String geoJson = GeoJSONWriter.write(gisFeatures);
 * </pre>
 *
 * @see <a href="http://geojson.org">GeoJSON</a>
 * @see <a href="https://tools.ietf.org/html/rfc7946">RFC 7946: The GeoJSON
 * Format</a>
 * @author Key Bridge LLC
 * @since v2.3.0 created 09/01/16
 */
public class GeoJSONWriter {

  /**
   * Internal construction - only.
   */
  private GeoJSONWriter() {
  }

  /**
   * Write the {@code GISFeatureCollection} instance to a GeoJSON formatted
   * string.
   *
   * @param gisFeatures the GISFeatureCollection instance
   * @return a formatted GeoJSON string
   */
  public static String write(GISFeatureCollection gisFeatures) {
    /**
     * Instantiate a feature collection, then add each Feature to the
     * collection.
     */
    DefaultFeatureCollection featureCollection = new DefaultFeatureCollection(gisFeatures.getId());
    /**
     * We collect unique property key values across all the GISFeatures, so that
     * every SimpleFeature collection has the same attributes. GeoTools
     * JSONWriter requires this.
     */
    String[] keys = gisFeatures.getFeatures().stream()
            .flatMap(feature -> feature.getProperties().keySet().stream())
            .distinct().toArray(String[]::new);
    for (GISFeature feature : gisFeatures.getFeatures()) {
      featureCollection.add(buildSimpleFeature(feature, keys));
    }
    return writeFeature(featureCollection);
  }

  /**
   * Write the {@code GISFeature} instance to a GeoJSON formatted string.
   *
   * @param feature the GIS Feature instance
   * @return a formatted GeoJSON string
   */
  public static String write(GISFeature feature) {
    try {
      return writeFeature(buildSimpleFeature(feature, feature.getProperties().keySet().toArray(new String[0])));
    } catch (IOException e) {
      // will never happen since we're using a StringWriter, no IO involved!
      throw new RuntimeException(e);
    }
  }

  /**
   * Write a feature to GeoJSON
   *
   * @param feature the feature
   * @return a GeoJSON string
   */
  private static String writeFeature(SimpleFeature feature) throws IOException {
    StringWriter writer = new StringWriter();
    new FeatureJSON().writeFeature(feature, writer);
    return writer.toString();
  }

  /**
   * Write a feature collection to GeoJSON
   *
   * @param featureCollection the feature collection
   * @return a GeoJSON string
   */
  private static String writeFeature(FeatureCollection featureCollection) {
    StringWriter writer = new StringWriter();
    try {
      new FeatureJSON().writeFeatureCollection(featureCollection, writer);
      return writer.toString();
    } catch (IOException e) {
      // will never happen since we're using a StringWriter, no IO involved!
      throw new RuntimeException(e);
    }
  }

  /**
   * Convert the GISFeature to GeoJSON.
   *
   * @param feature the GIS Feature instance
   * @return a formatted GeoJSON string
   */
  private static SimpleFeature buildSimpleFeature(GISFeature feature, String[] requiredKeys) {
    /**
     * Construct a string array containing values for each element in
     * requiredKeys. If there is no value for a given key in the feature's
     * properties, we assign null. This is necessary because GeoTools'
     * GSONWriter expects each feature to have the same list of keys (and in the
     * same order).
     */
    Map<String, String> properties = feature.getProperties();
    String[] propertyValues = new String[requiredKeys.length];

    int i = 0;
    for (String key : requiredKeys) {
      // Get the actual value or set to null; the latter is correctly handled by the JSON serializer.
      propertyValues[i++] = properties.getOrDefault(key, null);
    }

    /**
     * Initialize the Feature Type container template.
     */
    SimpleFeatureType featureType = buildSimpleFeatureType(feature, requiredKeys);
    /**
     * Instantiate a builder for the Feature Type container template, then
     * populate the template.
     */
    SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureType, new LenientFeatureFactoryImpl());
    builder.add(feature.getFeatureType());
    builder.add(feature.getName());
    /**
     * Add the Extension Values.
     */
    for (String value : propertyValues) {
      builder.add(value);
    }
    builder.add(feature.getAddress() != null ? feature.getAddress().toString() : null);
    builder.add(feature.getPosition() != null ? feature.getPosition().asPoint() : null);
    builder.add(envelope(feature.getGeometry()));
    builder.add(feature.getGeometry());
    /**
     * Finalize the feature builder into a SimpleFeature.
     */
    return builder.buildFeature(feature.getId());
  }

  /**
   * Idea: Add service information to the GeoJSON object as METADATA. i.e.
   * convert the service to a GISFeature, then write the GISFeature to GeoJSON.
   */
  private static SimpleFeatureType buildSimpleFeatureType(GISFeature feature, String[] propertyKeys) {
    SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
    builder.setName("ch.keybridge.lib.common.dto.GISFeature");
    builder.setCRS(DefaultGeographicCRS.WGS84);
    /**
     * The geographic feature type. e.g. border, boundary, zone, etc. This is
     * used to categories features.
     */
    builder.add("featureType", String.class);
    builder.add("name", String.class);
    /**
     * Add the Extension Keys the GeoJSON properties map.
     */
    for (String propertyKey : propertyKeys) {
      builder.add(propertyKey, String.class);
    }
    builder.add("address", String.class); // address
    builder.add("position", Point.class); // position
    /**
     * Important: Finalize the feature type with bounds and contour.
     */
    return finalizeFeatureType(builder, feature.getGeometry());
  }

  /**
   * Calculate the envelope for the given geometry and encode in a JSON string.
   * Bounds are returned as a comma delimited string as {@code [x1, x2, y1, y2]}
   * which is {@code [west, east, south, north]}
   * <p>
   * Historically this used to return [north, south, east, west].
   *
   * @param geometry the geometry to inspect
   * @return a string array of decimal degree values ordered as
   *         {@code [west, east, south, north]}.
   */
  private static String envelope(Geometry geometry) {
    /**
     * Create an Envelope for a region defined by maximum and minimum values.
     * Format order: <br>
     * x1 - the first x-value<br>
     * x2 - the second x-value<br>
     * y1 - the first y-value <br>
     * y2 - the second y-value
     */
    Envelope e = geometry.getEnvelopeInternal();
    DecimalFormat df = new DecimalFormat("#.0000");
    return "[" + df.format(e.getMinX())
            + "," + df.format(e.getMaxX())
            + "," + df.format(e.getMinY())
            + "," + df.format(e.getMaxY())
            + "]";
  }

  /**
   * Internal method to add the Geometry instance and then to build a
   * SimpleFeatureType.
   *
   * @param builder  the SimpleFeatureTypeBuilder instance
   * @param geometry the geometry to add
   * @return a (finalized) SimpleFeatureType
   */
  private static SimpleFeatureType finalizeFeatureType(SimpleFeatureTypeBuilder builder, Geometry geometry) {
    /**
     * Add the envelope boundary. Envelope is pre-parsed into a String.
     */
    builder.add("envelope", String.class);
    /**
     * The geometry class-type must be declared and is recorded in the GeoJSON
     * "type" field.
     */
    switch (Geometries.get(geometry)) {
      case POINT:
        builder.add("point", Point.class);
        builder.setDefaultGeometry("point");
        break;
      case LINESTRING:
        builder.add("linestring", LineString.class);
        builder.setDefaultGeometry("linestring");
        break;
      case POLYGON:
        builder.add("polygon", Polygon.class);
        builder.setDefaultGeometry("polygon");
        break;
      case MULTIPOINT:
        builder.add("multipoint", MultiPoint.class);
        builder.setDefaultGeometry("multipoint");
        break;
      case MULTILINESTRING:
        builder.add("multilinestring", MultiLineString.class);
        builder.setDefaultGeometry("multilinestring");
        break;
      case MULTIPOLYGON:
        builder.add("multipolygon", MultiPolygon.class);
        builder.setDefaultGeometry("multipolygon");
        break;
      case GEOMETRY:
      case GEOMETRYCOLLECTION:
        builder.add("geometrycollection", GeometryCollection.class);
        builder.setDefaultGeometry("geometrycollection");
        break;
      default:
        throw new AssertionError(geometry.getClass().getSimpleName());
    }
    return builder.buildFeatureType();
  }

}
