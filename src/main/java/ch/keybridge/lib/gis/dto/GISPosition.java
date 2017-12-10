/*
 *  Copyright (C) 2012 Caulfield IP Holdings (Caulfield) and/or its affiliates.
 *  All rights reserved. Use is subject to license terms.
 *
 *  Software Code is protected by Caulfield Copyrights. Caulfield hereby
 *  reserves all rights in and to Caulfield Copyrights and no license is
 *  granted under Caulfield Copyrights in this Software License Agreement.
 *  Caulfield generally licenses Caulfield Copyrights for commercialization
 *  pursuant to the terms of either Caulfield's Standard Software Source Code
 *  License Agreement or Caulfield's Standard Product License Agreement.
 *  A copy of Caulfield's either License Agreement can be obtained on request
 *  by email from: info@caufield.org.
 */
package ch.keybridge.lib.gis.dto;

import ch.keybridge.lib.xml.adapter.XmlDouble02PrecisionAdapter;
import ch.keybridge.lib.xml.adapter.XmlDouble06PrecisionAdapter;
import ch.keybridge.lib.xml.adapter.XmlMapDoublesAdapter;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * GIS Coordinate data transfer object.
 * <p>
 * This is based upon the logical data model container for the WSIF coordinate
 * database table.
 * <p>
 * The WSIF coordinate object is influenced by and extends the W3C Geolocation
 * API Specification and the Google Geolocation API, which is an implementation
 * of the W3C Geolocation API Specification.
 * <p>
 * The WSIF coordinate definition supports machine-friendly access to
 * geographical location (position) information.
 * <p>
 * This entity class represents a persistable standard representation of a
 * geographic point location by coordinates. This specification is an extension
 * of the W3C Geolocation API Specification Coordinates interface with
 * accommodations made to support White Space location requirements.
 * <p>
 * <strong>Heritage</strong> The attribute definitions of entity include content
 * from the Google Gears (Deprecated) Geolocation API, Copyright ©2011 Google
 * and are reproduced here in accordance with the <a
 * href="http://www.google.com/accounts/TOS">Google Terms of Service</a>.
 * <p>
 * This entity class also extends the Geolocation API Specification, W3C
 * Candidate Recommendation 07 September 2010, Copyright © 2010 W3C, Editor:
 * Andrei Popescu, Google, Inc.
 * <p>
 * <strong>Validity</strong> Latitude values outside the allowable range should
 * be rejected as invalid. For example, a longitude value of +105.0 degrees
 * could indicate an incorrectly encoded longitude or an invalid latitude value,
 * either of which is impossible to authoritatively discern programmatically.
 * Longitude values outside the allowable range may be optionally rejected as
 * invalid or normalized according to a receiving party’s discretion. For
 * example, a longitude value of -45.0 degrees may be correctly normalized to
 * +270 degrees.
 * <p>
 * If heading information is not available the value of this attribute must be
 * null, and if the reporting device is stationary (i.e. the value of the speed
 * attribute is 0), then the value of the heading attribute must be NaN.
 * <p>
 * If the speed information is not available the value of the speed attribute
 * must be null. Otherwise, the value of the speed attribute must be a
 * non-negative real number.
 * <p>
 * If the x-datum attribute is set (not-null) then the latitude and longitude
 * attributes of this coordinate are measured in the indicated datum. If the
 * datum attribute is not set (null) then the latitude and longitude attributes
 * are measured in the WGS84 datum according to the W3C specification. The
 * presence of the datum attribute modifies how this WSIF contact MUST be
 * interpreted, and is slightly different from the standard definitions of
 * latitude and longitude as defined in the W3C document.
 * <p>
 * If the elevation was not measured by GPS but rather has been calculated
 * according to a digital elevation model then the elevation model must be
 * indicated in the x-elevationModel attribute.
 *
 * @see <a href="http://www.w3.org/TR/geolocation-API/">Geolocation API
 * Specification</a>
 * @see <a
 * href="http://dev.w3.org/geo/api/spec-source.html#coordinates_interface">W3c
 * Coordinates interface</a>
 * @see <a href="http://en.wikipedia.org/wiki/ISO_6709">ISO 6709: Standard
 * representation of coordinates</a>
 * @author jesse
 */
@XmlRootElement(name = "GISPosition")
@XmlType(name = "GISPosition")
@XmlAccessorType(XmlAccessType.FIELD)
public class GISPosition implements Serializable {

  private static final long serialVersionUID = 1L;
  /**
   * 6.
   * <p>
   * The significant digits that should be preserved when recording a coordinate
   * value. The accuracyHorizontal at the equator provided by a coordinate with
   * six decimal places is approximately 0.111 meter, or 10 centimeter.
   *
   * @see <a href="http://en.wikipedia.org/wiki/Decimal_degrees">Decimal
   * degrees</a>
   */
  public static final int SCALE_COORDINATE = 6;

  /**
   * A free text name or description of the position.
   */
  @XmlElement(name = "Name")
  private String name;
  /**
   * Latitude in decimal degrees using the World Geodetic System 1984 (WGS84)
   * datum.
   * <p>
   * Latitude value precision is limited to 6 significant digits according to
   * the number format '00.000000' ranging between the values of -90.0 and
   * +90.0.
   */
  @XmlElement(name = "Latitude", required = true)
  @XmlJavaTypeAdapter(XmlDouble06PrecisionAdapter.class)
  private Double latitude;
  /**
   * Longitude in decimal degrees (WGS84) datum.
   * <p>
   * Longitude value precision is limited to 6 significant digits according to
   * the number format '000.000000' ranging between the values of 0.0 and
   * +360.0.
   */
  @XmlElement(name = "Longitude", required = true)
  @XmlJavaTypeAdapter(XmlDouble06PrecisionAdapter.class)
  private Double longitude;
  /**
   * Elevation height in meters (WGS84 datum) above mean sea level, or null if
   * not available.
   * <p>
   * The elevation attribute denotes the height of the position, specified in
   * meters above the [DATUM (default WGS84)] ellipsoid. If the implementation
   * cannot provide elevation information, the value of this attribute must be
   * null. Number precision is one significant digit. e.g. '0.0'.
   */
  @XmlElement(name = "Elevation")
  @XmlJavaTypeAdapter(XmlDouble02PrecisionAdapter.class)
  private Double elevation;
  /**
   * Denotes the direction of travel of the reporting device in degrees counting
   * clockwise relative to the true north.
   * <p>
   * The allowable range of values is 0.0 to 360.0 degrees.
   * <p>
   * If in motion, this is the instantaneous heading of the measuring entity
   * when this coordinate was recorded.
   * <p>
   * The heading attribute denotes the direction of travel of the hosting device
   * and is specified in degrees, where 0° &#8804; heading &lt; 360°, counting
   * clockwise relative to the true north. If the implementation cannot provide
   * heading information, the value of this attribute must be null. If the
   * hosting device is stationary (i.e. the value of the speed attribute is 0),
   * then the value of the heading attribute must be NaN. Heading is noted in
   * degrees from 0 to 360 with 0 as True North.
   */
  @XmlElement(name = "Heading")
  @XmlJavaTypeAdapter(XmlDouble02PrecisionAdapter.class)
  private Double heading;
  /**
   * Denotes the magnitude of the horizontal component of the reporting device's
   * current velocity measured in meters per second.
   * <p>
   * If in motion, this is the instantaneous speed of the measuring entity when
   * this coordinate was recorded.
   * <p>
   * The speed attribute denotes the magnitude of the horizontal component of
   * the hosting device's current velocity and is specified in meters per
   * second. If the implementation cannot provide speed information, the value
   * of this attribute must be null. Otherwise, the value of the speed attribute
   * must be a non-negative real number.
   */
  @XmlElement(name = "Speed")
  @XmlJavaTypeAdapter(XmlDouble02PrecisionAdapter.class)
  private Double speed;

  /**
   * The Geodetic datum in which the latitude and longitude are measured. Unless
   * otherwise specified, the default is always WGS84.
   * <p>
   * For most GPS-derived coordinates this is “WGS84”, and for FCC- derived
   * coordinates this is “NAD-83”.
   * <p>
   * A geodetic datum (plural datums, not data) is a reference from which
   * measurements are made. In surveying and geodesy, a datum is a set of
   * reference points on the Earth's surface against which position measurements
   * are made and (often) an associated model of the shape of the Earth
   * (reference ellipsoid) to define a geodetic coordinate system.
   * <p>
   * Because the Earth is an imperfect ellipsoid, localized datums can give a
   * more accurate representation of the area of coverage than the global WGS 84
   * datum.
   */
  @XmlAttribute(name = "datum")
  private String datum;
  /**
   * Represents the time when the position was acquired and is represented as a
   * DOMTimeStamp.
   * <p>
   * The time when the Position object was acquired and is represented as a
   * DOMTimeStamp. The DOMTimeStamp type is used to store an absolute or
   * relative time and represents a number of milliseconds. For Java,
   * DOMTimeStamp is bound to the long type.
   *
   * @see <a
   * href="http://www.w3.org/TR/DOM-Level-3-Core/core.html#Core-DOMTimeStamp">DOMTimeStamp</a>
   */
  @XmlAttribute(name = "domTimeStamp")
  private Long domTimeStamp;
  /**
   * The horizontal accuracy of the position in meters, or null if not
   * available.
   * <p>
   * The accuracyHorizontal attribute (also "position accuracy") is specified in
   * meters and corresponds to a 95% confidence level of the latitude and
   * longitude coordinates. The value of the accuracyHorizontal attribute must
   * be a non-negative real number. Number precision is one significant digit.
   * e.g. '0.0'.
   */
  @XmlAttribute(name = "accuracyHorizontal")
  @XmlJavaTypeAdapter(XmlDouble02PrecisionAdapter.class)
  private Double accuracyHorizontal;
  /**
   * The vertical accuracy of the position in meters, or null if not available.
   * <p>
   * The accuracyVertical attribute (also "elevation accuracyHorizontal") is
   * specified in meters and corresponds to a 95% confidence level. If the
   * implementation cannot provide elevation information, the value of this
   * attribute must be null. Otherwise, the value of the accuracyVertical
   * attribute must be a non-negative real number. Number precision is one
   * significant digit. e.g. '0.0'.
   */
  @XmlAttribute(name = "accuracyVertical")
  @XmlJavaTypeAdapter(XmlDouble02PrecisionAdapter.class)
  private Double accuracyVertical;

  /**
   * The Height above average terrain (HAAT) in meters.
   * <p>
   * Also called EHAAT (Effective Height Above Average Terrain), HAAT is a
   * measure of how high an antenna site is above the surrounding landscape.
   * HAAT is used extensively in FM radio and television, as it is more
   * important than effective radiated power (ERP) in determining the range of
   * broadcasts (VHF and UHF in particular, as they are line of sight
   * transmissions).
   */
  @XmlElement(name = "HAAT")
  @XmlJavaTypeAdapter(XmlDouble02PrecisionAdapter.class)
  private Double haat;

  /**
   * The radial Height above average terrain (HAAT) in meters.
   * <p>
   * This is a ordered map of the HAAT values calculated in each of 360 radial
   * directions around a position.
   */
  @XmlElement(name = "RadialHAAT")
  @XmlJavaTypeAdapter(XmlMapDoublesAdapter.class)
  private Map<Double, Double> radialHaat;

  /**
   * Default no argument constructor.
   */
  public GISPosition() {
  }

  /**
   * Constructor building a fully qualified GIS Position in one stroke.
   *
   * @param latitude           the latitude in decimal degrees
   * @param longitude          the longitude in decimal degrees
   * @param datum              the geodetic datum. NULL or one of
   *                           {@code [WGS84, NAD83, NAD27]}
   * @param elevation          the elevation above mean sea level (meters)
   * @param accuracyVertical   the elevation vertical accuracy (meters)
   * @param accuracyHorizontal the elevation horizontal accuracy (meters)
   * @return a new GIS Position instance
   */
  public static GISPosition getInstance(Double latitude, Double longitude, Double elevation, String datum, Double accuracyVertical, Double accuracyHorizontal) {
    GISPosition g = new GISPosition();
    g.setLatitude(latitude);
    g.setLongitude(longitude);
    g.setElevation(elevation);
    g.setDatum(datum);
    g.setAccuracyVertical(accuracyVertical);
    g.setAccuracyHorizontal(accuracyHorizontal);
    g.normalizeLatitudeAndLongitude();
    return g;
  }

  /**
   * Standard constructor.
   *
   * @param latitude  the latitude in decimal degrees
   * @param longitude the longitude in decimal degrees
   * @return a new GIS Position instance
   */
  public static GISPosition getInstance(double latitude, double longitude) {
    GISPosition g = new GISPosition();
    g.setLatitude(latitude);
    g.setLongitude(longitude);
    g.normalizeLatitudeAndLongitude();
    return g;
  }

  /**
   * Construct a new Coordinate from a POINT geometry. If configured this
   * constructor will attempt to set the DATUM to match the geometry SRID value.
   *
   * @param point a POINT geometry.
   * @return a new GIS Position instance
   */
  public static GISPosition getInstance(Point point) {
    return GISPosition.getInstance(point.getY(), point.getX());
  }

  /**
   * Construct a new Coordinate from a JTS coordinate configuration.
   * <p>
   * JTS coordinate is a lightweight class used to store coordinates on the
   * 2-dimensional Cartesian plane; is distinct from {@code Point}, and only
   * contains ordinate values and accessor methods.
   *
   * @param point a JTS coordinate
   * @return a new GIS Position instance
   */
  public static GISPosition getInstance(Coordinate point) {
    return GISPosition.getInstance(point.y, point.x);
  }

  // <editor-fold defaultstate="collapsed" desc="Constructor Support methods">
  /**
   * Normalize the current latitude and longitude values such that latitude
   * [-90, 90] and longitude [-180, 180],
   * <p>
   * Note: While this method is automatically called, external logic should be
   * employed to detect and confirm input values are correct before persisting.
   */
  private void normalizeLatitudeAndLongitude() {
    /**
     * Null check to prevent null point exceptions.
     */
    if (latitude == null || longitude == null) {
      return;
    }
    /**
     * Normalize the latitude to range between [-90, +90].
     */
    double latDouble = latitude;
    if (Math.abs(latDouble) == 180) {
      latDouble = 0d;
    }
    if (latDouble > 90) {
      latDouble = 90 - (latDouble % 90);
    } else if (latDouble < -90) {
      latDouble = -90 - (latDouble % 90);
    }
    setLatitude(latDouble);
    /**
     * Normalize the longitude to range between [-180, +180]
     */
    double lonDouble = longitude;
    if (Math.abs(lonDouble) == 360) {
      lonDouble = 0d;
    }
    if (lonDouble > 180) {
      lonDouble = (lonDouble % 360) - 360;
    } else if (lonDouble < -180) {
      lonDouble = (lonDouble % 360) + 360;
    }
    setLongitude(lonDouble);
  }// </editor-fold>

  public String getName() {
    return name;
  }

  // <editor-fold defaultstate="collapsed" desc="Getter and Setter">
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return The latitude geographic coordinates specified in decimal degrees.
   */
  public Double getLatitude() {
    return latitude;
  }

  /**
   * @param latitude The latitude geographic coordinates specified in decimal
   *                 degrees.
   */
  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  /**
   * @return The longitude geographic coordinates specified in decimal degrees.
   */
  public Double getLongitude() {
    return longitude;
  }

  /**
   * @param longitude The longitude geographic coordinates specified in decimal
   *                  degrees.
   */
  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  /**
   * @return The latitude/longitude horizontal spatial accuracyHorizontal in
   *         meters.
   */
  public Double getAccuracyHorizontal() {
    return accuracyHorizontal;
  }

  /**
   * @param accuracyHorizontal The latitude/longitude horizontal spatial
   *                           accuracyHorizontal in meters.
   */
  public void setAccuracyHorizontal(Double accuracyHorizontal) {
    this.accuracyHorizontal = accuracyHorizontal;
  }

  /**
   * @return The accuracyVertical attribute is specified in meters and
   *         corresponds to a 95% confidence level.
   */
  public Double getAccuracyVertical() {
    return accuracyVertical;
  }

  /**
   * @param accuracyVertical The accuracyVertical attribute is specified in
   *                         meters and corresponds to a 95% confidence level.
   */
  public void setAccuracyVertical(Double accuracyVertical) {
    this.accuracyVertical = accuracyVertical;
  }

  /**
   * @return The elevation attribute denotes the height of the position,
   *         specified in meters above the [DATUM (default WGS84)] ellipsoid.
   */
  public Double getElevation() {
    return elevation;
  }

  /**
   * @param elevation The elevation attribute denotes the height of the
   *                  position, specified in meters above the [DATUM (default
   *                  WGS84)] ellipsoid.
   */
  public void setElevation(Double elevation) {
    this.elevation = elevation;
  }

  /**
   * @return The direction of travel of the hosting device and is specified in
   *         degrees, where 0° &#8804; heading &lt; 360°, counting clockwise
   *         relative to the true north.
   */
  public Double getHeading() {
    return heading;
  }

  /**
   * @param heading The direction of travel of the hosting device and is
   *                specified in degrees, where 0° &#8804; heading &lt; 360°,
   *                counting clockwise relative to the true north.
   */
  public void setHeading(Double heading) {
    this.heading = heading;
  }

  /**
   * @return The hosting device's current velocity specified in meters per
   *         second.
   */
  public Double getSpeed() {
    return speed;
  }

  /**
   * @param speed The hosting device's current velocity specified in meters per
   *              second.
   */
  public void setSpeed(Double speed) {
    this.speed = speed;
  }

  /**
   * @return Time when the Position object was acquired represented as a
   *         java.util.Date.
   */
  public Date getDomTimeStamp() {
    if (domTimeStamp != null) {
      return new Date(domTimeStamp);
    }
    return null;
  }

  /**
   * @param date Time when the Position object was acquired represented as a
   *             java.util.Date.
   */
  public void setDomTimeStamp(Date date) {
    if (date != null) {
      this.domTimeStamp = date.getTime();
    }
  }

  /**
   * @return Time when the Position object was acquired represented as a
   *         DOMTimeStamp (a number of milliseconds).
   */
  public Long getDomTimeStampMillis() {
    return domTimeStamp;
  }

  /**
   * @param domTimeStamp Time when the Position object was acquired represented
   *                     as a DOMTimeStamp (a number of milliseconds)
   */
  public void setDomTimeStampMillis(Long domTimeStamp) {
    this.domTimeStamp = domTimeStamp;
  }

  /**
   * Get the Geodetic datum in which the latitude and longitude are represented.
   *
   * @return The Geodetic datum in which the latitude and longitude are
   *         represented.
   */
  public String getDatum() {
    return datum;
  }

  /**
   * Set the Geodetic datum in which the latitude and longitude are represented.
   *
   * @param datum The Geodetic datum in which the latitude and longitude are
   *              represented.
   */
  public void setDatum(String datum) {
    this.datum = datum;
  }

  /**
   * Get the Height above average terrain (HAAT) in meters.
   *
   * @return the Height above average terrain (HAAT) in meters
   */
  public Double getHaat() {
    return haat;
  }

  /**
   * Set Height above average terrain (HAAT) in meters
   *
   * @param haat Height above average terrain (HAAT) in meters
   */
  public void setHaat(Double haat) {
    this.haat = haat;
  }

  /**
   * Get the radial Height above average terrain (HAAT) in meters.
   * <p>
   * This is a ordered map of the HAAT values calculated in each of 360 radial
   * directions around a position.
   *
   * @return the radial HAAT as a TreeMap
   */
  public Map<Double, Double> getRadialHaat() {
    if (radialHaat == null) {
      radialHaat = new TreeMap<>();
    }
    return radialHaat;
  }

  /**
   * Set the radial Height above average terrain (HAAT) in meters.
   *
   * @param radialHaat the radial HAAT
   */
  public void setRadialHaat(Map<Double, Double> radialHaat) {
    this.radialHaat = radialHaat != null ? new TreeMap<>(radialHaat) : null;
  }// </editor-fold>

  /**
   * Convert this WSIF coordinate into a POINT geometry. The x, y, z ordinals
   * are configured with the longitude, latitude and elevation, respectively.
   *
   * @return this WSIF coordinate as a POINT geometry
   */
  public Point asPoint() {
    normalizeLatitudeAndLongitude();
    return new GeometryFactory(new PrecisionModel(Math.pow(10, 6))).createPoint(elevation != null
                                                                                ? new com.vividsolutions.jts.geom.Coordinate(longitude, latitude, elevation)
                                                                                : new com.vividsolutions.jts.geom.Coordinate(longitude, latitude));
  }

  /**
   * Convert this WSIF coordinate into a JTS GISPosition. The x, y, z ordinals
   * are configured with the longitude, latitude and elevation, respectively.
   *
   * @return this WSIF coordinate as a JTS GISPosition
   */
  public com.vividsolutions.jts.geom.Coordinate asCoordinate() {
    normalizeLatitudeAndLongitude();
    return elevation != null
           ? new com.vividsolutions.jts.geom.Coordinate(longitude, latitude, elevation)
           : new com.vividsolutions.jts.geom.Coordinate(longitude, latitude);
  }

  /**
   * Returns true if the latitude and longitude are not null or close to zero
   * (e.g. not set to [0, 0] degrees decimal).
   *
   * @return true if the latitude and longitude are not null or close to zero
   */
  public boolean isComplete() {
    return latitude != null && longitude != null;
  }

  /**
   * @return a hash code of the latitude and longitude
   */
  @Override
  public int hashCode() {
    int hash = 3;
    hash = 41 * hash + Objects.hashCode(this.latitude);
    hash = 41 * hash + Objects.hashCode(this.longitude);
    return Math.abs(hash);
  }

  /**
   * Coordinates are considered to be equal if their latitute AND longitude are
   * within four (4) decimal places (0.0001) which is approximately 11.1 meter
   * at the equator.
   * <p>
   * Returns true if the other GISPosition is within an 11.1 meter box centered
   * on this coordinate (max matching distance is approx 15.7 meter, 51.5 feet)
   * <p>
   * Equality is calculated as:
   * <p>
   * EQUAL if ABS(lat1 - lat2) &lt; 0.00001 and ABS(lon1 - lon2) &lt; 0.00001
   *
   * @param coordinate the other GISPosition
   * @return TRUE if within 0.0001 degree decimal (~11.1 meter)
   * @see <a href="http://en.wikipedia.org/wiki/Decimal_degrees">Decimal
   * degrees: Accuracy</a>
   */
  @Override
  public boolean equals(Object coordinate) {
    if (!(coordinate instanceof GISPosition)) {
      return false;
    }
    double elevenMeter = 0.00010;
    GISPosition other = (GISPosition) coordinate;
    return (Math.abs(this.latitude - other.getLatitude()) <= elevenMeter)
      && (Math.abs(this.longitude - other.getLongitude()) <= elevenMeter);
  }

  /**
   * Returns a pretty print name of this GISPosition with latitude and longitude
   * values formatted in decimal.
   *
   * @return e.g. String formatted as "DEC, DEC". e.g.
   *         <code>12.123, -117.456</code>
   */
  @Override
  public String toString() {
    return latitude + ", " + longitude;
  }

  /**
   * Normalize an arbitrary azimuth into a well-formed heading value, ensuring
   * that the heading range is between [-180 and +180] with ZERO defined as TRUE
   * NORTH.
   * <p>
   * This method is useful when working with geodetic and geometric calculators
   * that require well-formed heading input values.
   *
   * @param azimuth An azimuthal direction value in degrees, may be positive or
   *                negative and without limit
   * @return A well-formed heading value ranging between [-180, +180]
   */
  public static double normalizeToHeading(double azimuth) {
    double heading = azimuth;
    if (Math.abs(heading) == 360) {
      heading = 0d;
    }
    if (heading > 180) {
      heading = (heading % 360) - 360;
    } else if (heading < -180) {
      heading = (heading % 360) + 360;
    }
    return heading;
  }

  /**
   * Normalize an arbitrary heading into a well-formed azimuth value, ensuring
   * that the azimuth range is between [0 and 359]. This method is useful when
   * handling input heading ranges of [-180 to +180].
   *
   * @param heading a heading value in degrees, may be positive or negative, and
   *                without limit
   * @return a well-formed azimuth value ranging between [0, +359]
   */
  public static double normalizeToAzimuth(double heading) {
    double azimuth = heading;
    if (Math.abs(azimuth) == 360) {
      azimuth = 0;
    }
    if (azimuth > 360) {
      azimuth = (azimuth % 360);
    } else if (azimuth < 0) {
      azimuth = (azimuth % 360) + 360;
    }
    return azimuth;
  }
}
