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

import ch.keybridge.lib.gis.common.GISCalculator;
import ch.keybridge.lib.gis.dto.GISAddress;
import ch.keybridge.lib.gis.dto.GISFeature;
import ch.keybridge.lib.gis.dto.GISFeatureCollection;
import ch.keybridge.lib.gis.dto.GISPosition;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import java.io.*;
import java.util.Arrays;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 *
 * @author Key Bridge LLC
 */
public class GeoJSONWriterTest {

  public GeoJSONWriterTest() {
  }

//  @Test
  public void testPoint() throws IOException {
    /**
     * This was to evaluate whether to offer (or NOT) GISPositions formatted in
     * GeoJSON. The answer was NO as there is no real value.
     */
    Coordinate c = new Coordinate(40, -80, 1234.65);
    Point point = new GeometryFactory().createPoint(c);

    /**
     * Build the feature type template.
     */
    SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
    builder.setName("ch.keybridge.lib.common.dto.GISPosition");
    builder.setCRS(DefaultGeographicCRS.WGS84);
    builder.add("point", Point.class);
    SimpleFeatureType featureType = builder.buildFeatureType();

    SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
    featureBuilder.add(point);
    SimpleFeature simpleFeature = featureBuilder.buildFeature(null);
    StringWriter writer = new StringWriter();
    new FeatureJSON().writeFeature(simpleFeature, writer);
    System.out.println("Point feature\n" + writer.toString());

  }

//  @Test
  public void testFeature() throws IOException {
    /**
     * Build a feature
     */
    GISFeature feature = buildFeature(new Coordinate(40, -80));
    /**
     * Write it.
     */
    String json = GeoJSONWriter.write(feature);
    /**
     * Validate the JSON at https://jsonformatter.curiousconcept.com/
     */
    System.out.println(json);
  }

//  @Test
  public void testFeatureCollection() throws IOException {
    /**
     * Build a collection
     */
    GISFeatureCollection collection = GISFeatureCollection.getInstance("id", "collecitontype", "collectionname", null);
    /**
     * Add two features
     */
    collection.addFeatures(buildFeature(new Coordinate(40, -80)));
    collection.addFeatures(buildFeature(new Coordinate(20, -60)));
    /**
     * Write it.
     */
    String json = GeoJSONWriter.write(collection);
    /**
     * Validate the JSON at https://jsonformatter.curiousconcept.com/
     */
    System.out.println(json);

  }

  @Test
  public void featureCollectionWriter() throws IOException, ParseException {
    DefaultFeatureCollection featureCollection = new DefaultFeatureCollection();
    featureCollection.add(featureWriter());
    featureCollection.add(featureWriter2());

    FeatureJSON featureJSON = new FeatureJSON();
    StringWriter writer = new StringWriter();
    featureJSON.writeFeatureCollection(featureCollection, writer);
    String json = writer.toString();

    System.out.println("JSON featureCollection");
    System.out.println(json);

  }

  public SimpleFeature featureWriter() throws IOException {
    SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
//set the name
    b.setName("property");

//add some properties
    b.add("name", String.class);
    b.add("classification", Integer.class);
    b.add("height", Double.class);
    //set the coordinate reference system
    b.setCRS(DefaultGeographicCRS.WGS84);

    //add some geometry properties (first added is the default)
//    b.add("geometry", Polygon.class);
    b.add("point", com.vividsolutions.jts.geom.Point.class);
//    b.add("network", MultiLineString.class);
    //set the default geometry
//    b.setDefaultGeometry("region");
    SimpleFeatureType featureType = b.buildFeatureType();

    SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureType);
    builder.add("Canada POINT");
    builder.add(10);
    builder.add(20.5);
//    builder.add(new Point(-124, 52));

    GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
    builder.add(geometryFactory.createPoint(new Coordinate(-124, 52)));

    SimpleFeature feature = builder.buildFeature(null);

    FeatureJSON fjson = new FeatureJSON();
    StringWriter writer = new StringWriter();
    fjson.writeFeature(feature, writer);
    String json = writer.toString();

    System.out.println("JSON");
    System.out.println(json);

    return feature;

  }

  public SimpleFeature featureWriter2() throws IOException, ParseException {
    SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
//set the name
    b.setName("property");

//add some properties
    b.add("name", String.class);
    b.add("classification", Integer.class);
    b.add("height", Double.class);
    //set the coordinate reference system
    b.setCRS(DefaultGeographicCRS.WGS84);

    //add some geometry properties (first added is the default)
    b.add("geometry", Polygon.class);
    b.add("point", com.vividsolutions.jts.geom.Point.class);
//    b.add("network", MultiLineString.class);
    //set the default geometry
//    b.setDefaultGeometry("region");
    SimpleFeatureType featureType = b.buildFeatureType();

    SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureType);
    builder.add("Canada contour");
    builder.add(100);
    builder.add(200.5);
//    builder.add(new Point(-124, 52));

//    GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
    String polyWKT = "POLYGON((-118.16327459206458 32.53051522923972,-118.16327459206458 33.715278650109425,-116.03675535110257 33.715278650109425,-116.03675535110257 32.53051522923972,-118.16327459206458 32.53051522923972))";
    WKTReader wktReader = new WKTReader();
    System.out.println("\n Try the default WKT READER");
    Geometry jtsGeo = wktReader.read(polyWKT);

    builder.add(jtsGeo);

    SimpleFeature feature = builder.buildFeature(null);

    FeatureJSON fjson = new FeatureJSON();
    StringWriter writer = new StringWriter();
    fjson.writeFeature(feature, writer);
    String json = writer.toString();

    System.out.println("JSON");
    System.out.println(json);

    return feature;

  }

  public void reader() throws IOException {
    GeometryJSON gjson = new GeometryJSON();
// be sure to strip whitespace
    String json = "{\"type\":\"Point\",\"coordinates\":[100.1,0.1]}";

    Reader reader = new StringReader(json);
    com.vividsolutions.jts.geom.Point p = gjson.readPoint(reader);
    System.out.println("point p " + p);

  }

  public void test2() {
    SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
    builder.setName("Location");
    builder.setCRS(DefaultGeographicCRS.WGS84); // <- Coordinate reference system

    // add attributes in order
    builder.add("geometry", java.awt.Point.class);
    builder.length(15).add("Name", String.class); // <- 15 chars width for name field

    // build the type
    final SimpleFeatureType LOCATION = builder.buildFeatureType();

    System.out.println(LOCATION);

//        return LOCATION;
  }

  public void test1() throws SchemaException {

    DefaultFeatureCollection featureCollection = new DefaultFeatureCollection();

    System.out.println("DefaultFeatureCollection \n" + featureCollection);

    SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
    b.setName("Flag");

//add some properties
    b.add("name", String.class);
    b.add("classification", Integer.class);
    b.add("height", Double.class);

//add a geometry property
    b.setCRS(DefaultGeographicCRS.WGS84); // set crs first
    b.add("location", com.vividsolutions.jts.geom.Point.class); // then add geometry

//build the type
    final SimpleFeatureType FLAG = b.buildFeatureType();

    System.out.println("flag " + FLAG);

//    new GeometryJSON().t
//    GeoJSON.write(FLAG, null);
    //create the builder
    SimpleFeatureBuilder builder = new SimpleFeatureBuilder(FLAG);

//add the values
    builder.add("Canada");
    builder.add(1);
    builder.add(20.5);

    GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);

    builder.add(geometryFactory.createPoint(new Coordinate(-124, 52)));

//build the feature with provided ID
    SimpleFeature feature = builder.buildFeature("fid.1");

    System.out.println("feature " + feature);

    /*
     * We use the DataUtilities class to create a FeatureType that will describe
     * the data in our shapefile.
     *
     * See also the createFeatureType method below for another, more flexible
     * approach.
     */
    final SimpleFeatureType TYPE = DataUtilities.createType("Location",
                                                            "location:Point,"
                                                            + // <- the geometry attribute: Point type
                                                            "name:String,"
                                                            + // <- a String attribute
                                                            "number:Integer" // a number attribute
    );

    System.out.println("DEBUG TYPE  " + TYPE);

    /*
     * We create a FeatureCollection into which we will put each Feature created
     * from a record in the input csv data file
     */
    SimpleFeatureCollection collection = new DefaultFeatureCollection();
//    FeatureCollections.newCollection();
    /*
     * GeometryFactory will be used to create the geometry attribute of each
     * feature (a Point object for the location)
     */
//    GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
    SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);

  }

  /**
   * Internal method to build a test feature
   *
   * @param c a coordinate
   * @return a feature
   */
  private GISFeature buildFeature(Coordinate c) {
    GISFeature feature = new GISFeature();
    feature.setId("featureID " + c.hashCode());
    feature.setName("testFeatureName " + c.hashCode());
    feature.setFeatureType("testFeatureType");
    feature.setGeometry(new GeometryFactory().createPoint(c).buffer(5));
    feature.setProperty("this", "that");
    feature.setProperty("one", "1-1");
    feature.setProperty("two", "2-2");
    feature.setProperty("three", "3-3");
    feature.setAddress(GISAddress.getInstance(c.hashCode() + " binary blvd", "boolean", "IO", "10101", "IC"));
    feature.setPosition(GISPosition.getInstance(c));
    return feature;
  }

//  @Test
//  public void testWriteGeoJSON() throws IOException, Exception {
//    /**
//     * Write a single tile with tick marks as a GeoJSON feature. This may be
//     * copied into the coverage.html file and displayed.
//     */
//    Path libraryPath = Paths.get("/var/data/elevation/nrcan/cdem/093");
//    File dir = libraryPath.toFile();
//    GISFeatureCollection featureCollection = new GISFeatureCollection();
//    for (File file : dir.listFiles()) {
//      if (!file.getName().endsWith("tif") || !file.getName().contains("093M")) {
//        continue;
//      }
//      /**
//       * Read the tiff file. Write it to the feature collection (as a
//       * rectangular boundary).
//       */
//      GridCoverage2D gridCoverage = read(file.toPath());
//      Envelope2D envelope = gridCoverage.getEnvelope2D();
//      Polygon poly = asPolygon(envelope);
//      GISFeature feature = GISFeature.getInstance(file.getName(), poly);
//      featureCollection.addFeatures(feature);
//      /**
//       * Use the envelope to generate tic marks along the boundary.
//       */
//      GeometryFactory geometryFactory = new GeometryFactory();
//      double step = 0.25;
//      for (double x = envelope.getMinX() + step; x < envelope.getMaxX() - step; x += step) {
//        GISFeature ticFeatureTop = GISFeature.getInstance(String.valueOf("tic" + x + "top"), geometryFactory.createLineString(
//                                                          new Coordinate[]{
//                                                            new Coordinate(x, envelope.getMaxY()),
//                                                            new Coordinate(x, envelope.getMaxY() - 0.025)
//                                                          }));
//        GISFeature ticFeatureBottom = GISFeature.getInstance(String.valueOf("tic" + x + "bottom"), geometryFactory.createLineString(
//                                                             new Coordinate[]{
//                                                               new Coordinate(x, envelope.getMinY()),
//                                                               new Coordinate(x, envelope.getMinY() + 0.025)
//                                                             }));
//        featureCollection.addFeatures(ticFeatureTop);
//        featureCollection.addFeatures(ticFeatureBottom);
//      }
//      for (double y = envelope.getMinY() + step; y < envelope.getMaxY() - step; y += step) {
//        GISFeature ticFeatureLeft = GISFeature.getInstance(String.valueOf("tic" + y + "left"), geometryFactory.createLineString(
//                                                           new Coordinate[]{
//                                                             new Coordinate(envelope.getMinX(), y),
//                                                             new Coordinate(envelope.getMinX() + 0.025, y)
//                                                           }));
//        GISFeature ticFeatureRight = GISFeature.getInstance(String.valueOf("tic" + y + "right"), geometryFactory.createLineString(
//                                                            new Coordinate[]{
//                                                              new Coordinate(envelope.getMaxX(), y),
//                                                              new Coordinate(envelope.getMaxX() - 0.025, y)
//                                                            }));
//
//        featureCollection.addFeatures(ticFeatureLeft);
//        featureCollection.addFeatures(ticFeatureRight);
//      }
//    }
//
//    /**
//     * Dump the feature collection as geojson. Copy this output to the HTML.
//     */
//    System.out.println(GeoJSONWriter.write(featureCollection).replaceAll("\\{", "\n{"));
//
//  }
  /**
   * Write a polygon.
   *
   * @param envelope
   * @return
   */
  private Polygon asPolygon(Envelope2D envelope) {
    Coordinate[] coordinates = new Coordinate[5];
    /**
     * GeoJSON polygons MUST obey the right-hand-rule, which specifies
     * counter-clockwise ordering for polygon coordinates.
     */
    coordinates[0] = new Coordinate(envelope.getMinX(), envelope.getMaxY());
    coordinates[1] = new Coordinate(envelope.getMinX(), envelope.getMinY());
    coordinates[2] = new Coordinate(envelope.getMaxX(), envelope.getMinY());
    coordinates[3] = new Coordinate(envelope.getMaxX(), envelope.getMaxY());

    coordinates[4] = new Coordinate(envelope.getMinX(), envelope.getMaxY());
    return (Polygon) new GeometryFactory().createPolygon(coordinates);
  }

  /**
   * This method reads in the TIFF image, assigns the WGS84 CRS, determines the
   * math transform from raster to the CRS model, and constructs a GridCoverage.
   * <p>
   * Developer note: This method tries to read a GeoTIFF file using the method
   * implemented in the {@code org.geoserver} unit tests.
   *
   * @param file the GeoTIFF file
   * @return grid coverage represented by the image
   * @throws DataSourceException if the GeoTiffReader fails to initialize
   * @throws IOException         on any IO related troubles
   */
//  public GridCoverage2D read(Path file) throws DataSourceException, IOException, Exception {
//    GeoTiffReader readerTarget = new GeoTiffReader(file.toFile()); // throws DataSourceException
//    GridCoverage2D gridCoverage = null;
//    try {
//      gridCoverage = readerTarget.read(null); // throws IOException
//    } catch (Exception e) {
//      // ok, not a geotiff!
//    } finally {
//      try {
//        readerTarget.dispose();
//      } catch (Exception e) {
//        throw new IOException(e);
//      }
//    }
//    return gridCoverage;
//  }
  @Test
  public void testGeoJSONWriterFail() {
    GISFeatureCollection collection = new GISFeatureCollection();
    Geometry someGeometry = GISCalculator.asPolygon(Arrays.asList(
      new Coordinate(0, 0), new Coordinate(0, 1),
      new Coordinate(1, 0), new Coordinate(0, 0)));

    GISFeature one = new GISFeature();
    one.setName("one");
    one.setFeatureType("featureType1");
    one.setGeometry(someGeometry);
    one.setProperty("foo", "bar");
    one.setProperty("foo2", "bar2");
    one.setAddress(GISAddress.getInstance("street", "city", "CA", "123", "USA"));
    collection.addFeatures(one);

    GISFeature two = new GISFeature();
    two.setName("two");
    two.setFeatureType("featureType2");
    two.setGeometry(someGeometry);
    two.setProperty("foo", "bar");
    collection.addFeatures(two);

    System.out.println(GeoJSONWriter.write(one));
    System.out.println(GeoJSONWriter.write(two));
    System.out.println(GeoJSONWriter.write(collection));
  }

}
