# GeoJSON conversion code

The following LEGACY code is provided for reference only. It is deprecated and not in use.

These methods were previously used to directly convert a `WhitespaceRegistration` service record
to GeoJSON for map rendering. The current strategy uses automated media type readers and writers.



```java

  /**
   * Convert all the WirelessService records belonging to a single License to a
   * GeoJSON FeatureCollection.
   * <p>
   * For each service: If no WhitespaceRegistration records are present then the
   * station (POINT) locations are provided as an alternative.
   * <p>
   * @param license
   * @return
   */
  public static String toGeoJson(License license) {
    DefaultFeatureCollection featureCollection = new DefaultFeatureCollection(license.getCallSign());
    for (WirelessService wirelessService : license.getWirelessServiceList()) {
      /**
       * Add the stations.
       */
      for (Station station : wirelessService.getStationList()) {
        featureCollection.add(buildFeature(station.getLocation(),
                                           wirelessService.getWirelessServiceType(),
                                           wirelessService.getLicense().getCallSign()));
      }
      /**
       * Add the registrations. Do not add adjacent television registrations, as
       * their geometry mirrors the co-channel transmitter and so need not be
       * present.
       */
      for (WhitespaceRegistration whitespaceRegistration : wirelessService.getWhitespaceRegistrationList()) {
        if (whitespaceRegistration.getWhitespaceType().isTelevisionTransmitter()) {
          if (!whitespaceRegistration.isAdjacent()) {
            featureCollection.add(buildFeature(new WhitespaceRegistrationDTO(whitespaceRegistration)));
          }
        } else {
          featureCollection.add(buildFeature(new WhitespaceRegistrationDTO(whitespaceRegistration)));
        }
      }
    }
    StringWriter writer = new StringWriter();
    try {
      new FeatureJSON().writeFeatureCollection(featureCollection, writer);
    } catch (IOException ex) {
      Logger.getLogger(GoogleAPIUtility.class.getName()).log(Level.SEVERE, null, ex);
    }
    return writer.toString();
  }

  /**
   * Convert a WirelessService record to a GeoJSON FeatureCollection. If no
   * WhitespaceRegistration records are present then the station (POINT)
   * locations are provided as an alternative.
   * <p>
   * @param wirelessService a service record
   * @return a GeoJSON FeatureCollection containing the WhitespaceRegistration
   *         records (DEFAULT) or Station Location coordinates (if no
   *         WhitespaceRegistration are defined)
   */
  public static String toGeoJson(WirelessService wirelessService) {
    DefaultFeatureCollection featureCollection = new DefaultFeatureCollection(wirelessService.getUuid());
    /**
     * Add the stations.
     */
    for (Station station : wirelessService.getStationList()) {
      featureCollection.add(buildFeature(station.getLocation(),
                                         wirelessService.getWirelessServiceType(),
                                         wirelessService.getLicense().getCallSign()));
    }
    /**
     * Add the registrations. Do not add adjacent television TX or RX adjacent
     * contours as their geometry mirrors the co-channel transmitter.
     */
    for (WhitespaceRegistration whitespaceRegistration : wirelessService.getWhitespaceRegistrationList()) {
      if (whitespaceRegistration.getWhitespaceType().isTelevisionTransmitter() || whitespaceRegistration.getWhitespaceType().isTelevisionReceiver()) {
        if (!whitespaceRegistration.isAdjacent()) {
          featureCollection.add(buildFeature(new WhitespaceRegistrationDTO(whitespaceRegistration)));
          /**
           * If the device is a TV receiver then try to plot the transmitter.
           */
          if (whitespaceRegistration.getDevice() != null && whitespaceRegistration.getDevice().isReceiver()) {
            try {
              /**
               * @TODO: add a flag to specify the feature icon color
               */
              featureCollection.add(buildFeature(whitespaceRegistration.getDevice().getTransmitter().getStation().getLocation(),
                                                 whitespaceRegistration.getDevice().getTransmitter().getWirelessServiceType(),
                                                 whitespaceRegistration.getDevice().getTransmitter().getName() + " " + whitespaceRegistration.getDevice().getTransmitter().getChannelList()));
            } catch (Exception e) {
              System.err.println("RXTV failed to add upstream transmitter feature " + e.getMessage());
            }
          }
        }
      } else {
        featureCollection.add(buildFeature(new WhitespaceRegistrationDTO(whitespaceRegistration)));
      }
    }
    StringWriter writer = new StringWriter();
    try {
      new FeatureJSON().writeFeatureCollection(featureCollection, writer);
    } catch (IOException ex) {
      Logger.getLogger(GoogleAPIUtility.class.getName()).log(Level.SEVERE, null, ex);
    }
    return writer.toString();
  }

  /**
   * Convert a WhitespaceRegistration record to a GeoJSON Feature. This is
   * useful when plotting a registration contour on a map.
   * <p>
   * The properties object contains a google.maps.Data.StyleOptions
   * configuration.
   * <p>
   * @param whitespaceRegistration a registration record
   * @return a GeoJSON feature
   * @see <a
   * href="http://geojson.org/geojson-spec.html#appendix-a-geometry-examples">GeoJSON</a>
   * @see <a
   * href="https://developers.google.com/maps/documentation/javascript/3.exp/reference?hl=es#Data.StyleOptions">StyleOptions</a>
   */
  public static String toGeoJson(WhitespaceRegistrationDTO whitespaceRegistration) {
    StringWriter writer = new StringWriter();
    try {
      new FeatureJSON().writeFeature(buildFeature(whitespaceRegistration), writer); // IOException
    } catch (IOException ex) {
      Logger.getLogger(GoogleAPIUtility.class.getName()).log(Level.SEVERE, null, ex);
    }
    return writer.toString();
  }

  /**
   * Build an S3 bucket prefix (e.g. a URL path) as the first three characters
   * of the call sign. The prefix is converted to lower case and includes the
   * trailing slash "/". For example, if the call sign "WABC" is provided the
   * returned S3 Prefix would be "w/a/b/".
   * <p>
   * This is used to keep each S3 bucket size to (generally) below 200 images.
   * <p>
   * Important: S3 prefixes must NOT include the a leading slash "/".
   * <p>
   * @param callSign a wireless service call sign
   * @return an S3 bucket prefix.
   */
  private static String getS3Prefix(String callSign) {
    StringBuilder sb = new StringBuilder();
    int numCharacters = 0;
    for (char c : callSign.toCharArray()) {
      sb.append(c).append("/");
      numCharacters++;
      if (numCharacters >= 3) {
        break;
      }
    }
    /**
     * Return the path as lower case.
     */
    return sb.toString().toLowerCase();
  }

  /**
   * Build a single GeoJSON Feature describing a WhitespaceRegistration contour.
   * <p>
   * Feature is a GeoJSON container for geometry type with an associated
   * FeatureSet (properties) attributes container.
   * <p>
   * The contour is simplified to COORDINATES_MAX_MAP (180 coordinates)
   * <p>
   * @param whitespaceRegistration the contour geometry to describe
   * @return a GeoJSON Feature object
   */
  private static SimpleFeature buildFeature(WhitespaceRegistrationDTO whitespaceRegistration) {
    SimpleFeatureBuilder builder = new SimpleFeatureBuilder(buildFeatureType(whitespaceRegistration.getContour()));
    builder.add(mapColor(whitespaceRegistration.getWhitespaceType())); // fillColor
//    builder.add(whitespaceRegistration.isAdjacent() ? OPACITY_TRANSPARENT : OPACITY_10); // fillOpacity
    builder.add(OPACITY_TRANSPARENT); // fillOpacity
    builder.add(mapColor(whitespaceRegistration.getWhitespaceType())); // strokeColor
    builder.add(OPACITY_SOLID); // strokeOpacity
    builder.add(whitespaceRegistration.isAdjacent() ? STROKE_WEIGHT_ONE : STROKE_WEIGHT_TWO); // strokeWeight
    builder.add(whitespaceRegistration.getCallSign()); // title
    builder.add(iconLabel(whitespaceRegistration.getCallSign(), whitespaceRegistration.isAdjacent())); // icon
    builder.add(whitespaceRegistration.getWirelessServiceType().name()); // type
    builder.add(whitespaceRegistration.isAdjacent()); // adjacent
    /**
     * Add bounds, contour, location.
     */
    builder.add(bounds(whitespaceRegistration.getContour())); // bounds
    builder.add(GISUtility.simplify(whitespaceRegistration.getContour(), null, true, COORDINATES_MAX_MAP)); // geometry
    return builder.buildFeature(whitespaceRegistration.getUuid());
  }

  /**
   * Build a single GeoJSON Feature describing a WhitespaceRegistration contour.
   * <p>
   * Feature is a GeoJSON container for geometry type with an associated
   * FeatureSet (properties) attributes container.
   * <p>
   * @param location            the described location
   * @param wirelessServiceType the service type at this location - this
   *                            determines the color to use
   * @param name                the location name - if a point this determines
   *                            the title
   * @return a GeoJSON Feature object
   */
  private static SimpleFeature buildFeature(Location location,
                                            EWirelessServiceType wirelessServiceType,
                                            String name) {
    SimpleFeatureBuilder builder = new SimpleFeatureBuilder(buildFeatureType(location.getGeometry()));
    builder.add(mapColor(wirelessServiceType.getWhitespaceType()));
    builder.add(OPACITY_10);
    builder.add(mapColor(wirelessServiceType.getWhitespaceType()));
    builder.add(OPACITY_SOLID);
    builder.add(STROKE_WEIGHT_ONE);
    builder.add(name);
    builder.add(icon(wirelessServiceType));
    builder.add(wirelessServiceType.name());
    builder.add(Boolean.FALSE);
    builder.add(bounds(location.getGeometry()));
    builder.add(location.getGeometry());
    return builder.buildFeature(null);
  }

  /**
   * Build a FeatureType corresponding to a google.maps.Data.StyleOptions
   * description.
   * <p>
   * FeatureType is a GeoJSON collection of attributes associated with a
   * geographic feature. Features in GeoJSON contain a <code>geometry</code>
   * object and additional <code>properties</code>. The fields within the
   * <code>properties</code> attributes container are programmatically defined
   * by a FeatureType.
   * <p>
   * Developer note: Once created the type is immutable (cannot be changed) and
   * so is often instantiated as a static constant.
   * <p>
   * @return a FeatureType (mostly) matching the fields within a
   *         google.maps.Data.StyleOptions description
   */
  private static SimpleFeatureType buildFeatureType(Geometry geometry) {
    SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
    b.setName("google.maps.Data.StyleOptions");
    b.setCRS(DefaultGeographicCRS.WGS84);
    b.add("fillColor", String.class);
    b.add("fillOpacity", Double.class);
    b.add("strokeColor", String.class);
    b.add("strokeOpacity", Double.class);
    b.add("strokeWeight", Integer.class);
    b.add("title", String.class);
    b.add("icon", String.class);
    b.add("type", String.class);
    b.add("adjacent", Boolean.class);
    b.add("bounds", String.class);

    switch (Geometries.get(geometry)) {
      case POINT:
        b.add("point", Point.class);
        break;
      case LINESTRING:
        b.add("linestring", LineString.class);
        break;
      case POLYGON:
        b.add("polygon", Polygon.class);
        break;
      case MULTIPOINT:
        b.add("multipoint", MultiPoint.class);
        break;
      case MULTILINESTRING:
        b.add("multilinestring", MultiLineString.class);
        break;
      case MULTIPOLYGON:
        b.add("multipolygon", MultiPolygon.class);
        break;
      case GEOMETRY:
      case GEOMETRYCOLLECTION:
        b.add("geometrycollection", GeometryCollection.class);
        break;
      default:
        throw new AssertionError(geometry.getClass().getSimpleName());
    }

    //    b.add("visible", Boolean.class);
    //    b.add("zIndex", Integer.class);
    //    b.add("clickable", Boolean.class);
    return b.buildFeatureType();
  }

  /**
   * Build a FeatureType corresponding to a google.maps.Data.StyleOptions
   * description.
   * <p>
   * FeatureType is a GeoJSON collection of attributes associated with a
   * geographic feature. Features in GeoJSON contain a <code>geometry</code>
   * object and additional <code>properties</code>. The fields within the
   * <code>properties</code> attributes container are programmatically defined
   * by a FeatureType.
   * <p>
   * Developer note: Once created the type is immutable (cannot be changed) and
   * so is often instantiated as a static constant.
   * <p>
   * @return a FeatureType (mostly) matching the fields within a
   *         google.maps.Data.StyleOptions description
   */
  private static SimpleFeatureType buildFeatureType(License license) {
    SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
    b.setName("com.keybridge.wirelessServiceList");
    b.add("wirelessServiceList", String.class);
    return b.buildFeatureType();
  }

  /**
   * Calculate the SW and NE bounds for the given geometry. Bounds are returned
   * as a comma delimited string as [latitude,longitude].
   * <p>
   * @param geometry the geometry to inspect
   * @return a string array containing two entries. [0] = SW and [1] = NE.
   */
  private static String bounds(Geometry geometry) {
    Double n = null;
    Double s = null;
    Double e = null;
    Double w = null;
    for (com.vividsolutions.jts.geom.Coordinate coordinate : geometry.getEnvelope().getCoordinates()) {
      n = n == null ? coordinate.y : n < coordinate.y ? coordinate.y : n;
      s = s == null ? coordinate.y : s > coordinate.y ? coordinate.y : s;
      e = e == null ? coordinate.x : e < coordinate.x ? coordinate.x : e;
      w = w == null ? coordinate.x : w > coordinate.x ? coordinate.x : w;
    }
    return "[" + n + "," + s + "," + e + "," + w + "]";
  }





  /**
   * Helper method to get a WhitespaceType specific stroke color.
   * <p/>
   * This method is also accessed by the Spectrum Explorer and Data Browser to
   * explain what the contours mean.
   * <p/>
   * @param whitespaceType the Enum_WhitespaceType
   * @return an HTML color.
   */
  public static String mapColor(EWhitespaceType whitespaceType) {
    String strokeColor = COLOR_BLUE;
    if (whitespaceType == null) {
      return strokeColor;
    }
    switch (whitespaceType) {
      case KEYBRIDGE_DEMO:
        strokeColor = "#000000"; // BLACK
        break;
      case GOV_FCC_WHITESPACETYPE_ASTR:
        strokeColor = "#151B8D"; // Radio Astronomy (NAVY)
        break;
      case GOV_FCC_WHITESPACETYPE_ORS:
        strokeColor = "#FFFC17"; // Off Shore Radio Telephone (YELLOW)
        break;
      case GOV_FCC_WHITESPACETYPE_URBAN:
        strokeColor = "#6C2DC7"; // Metro PLMRS (PURPLE)
        break;
      case GOV_FCC_WHITESPACETYPE_LMRS:
        strokeColor = "#4AA02C"; // Waivered PLMRS (GREEN)
        break;
      case GOV_FCC_WHITESPACETYPE_LPAUX:
        strokeColor = "#F87217"; // Low Power Auxilliary (ORANGE)
        break;
      case GOV_FCC_WHITESPACETYPE_TV:
        strokeColor = "#C11B17"; // Television (MAROON)
        break;
      case GOV_FCC_WHITESPACETYPE_TV_RX:
        strokeColor = "#7E3817"; // TV Translators (BROWN)
        break;
      case GOV_FCC_WHITESPACETYPE_TV_DD:
        strokeColor = "#C11B17"; // Television (MAROON)
        break;
      case GOV_FCC_WHITESPACETYPE_BCAUX:
        strokeColor = "#C031C7"; // Broadcast Auxilliary Service (BAS) (PINK)
        break;
      case GOV_FCC_WHITESPACETYPE_TV_RX_MVPD:
        strokeColor = "#57FEFF"; // MVPD (Cable Head-Ends, etc.) (AQUA BLUE)
        break;
      case CA_GC_IC_WHITESPACETYPE_TV:
        strokeColor = "#C11B17"; // Canadian and Mexican TV (MAROON)
        break;
      case CA_GC_IC_WHITESPACETYPE_TV_RX:
        strokeColor = "#7E3817"; // TV Translators (BROWN)
        break;
      case MX_GOV_COFETEL_WHITESPACETYPE_TV:
        strokeColor = "#C11B17"; // Canadian and Mexican TV (MAROON)
        break;
      case MX_GOV_COFETEL_WHITESPACETYPE_TV_RX:
        strokeColor = "#7E3817"; // TV Translators (BROWN)
        break;
      default:
        throw new AssertionError(whitespaceType.name());
    }
    return strokeColor;
  }

  /**
   * Internal method to get a URI icon corresponding to the wireless service
   * type.
   * <p>
   * @param wirelessServiceType the wireless service type
   * @return a URL pointing to an image icon stored on S3
   */
  public static String icon(EWirelessServiceType wirelessServiceType) {
    String urlBase = "https://s3.amazonaws.com/keybridgeglobal/images/icon/map/";
    /**
     * Set the marker Icon as an image URL.
     */
    String icon;
    /**
     * All others set the icon according to the service type.
     */
    switch (wirelessServiceType.getParent()) {
      case GOV_FCC_WIRELESSSERVICETYPE_ASTR:
        icon = "red/entertainment/planetarium-2.png";
//        markerIconUri = "red/industry/observatory.png";
        break;
      case GOV_FCC_WIRELESSSERVICETYPE_ORS:
        icon = "red/office/telephone.png";
        break;
      case GOV_FCC_WIRELESSSERVICETYPE_URBAN:
        icon = "red/office/police.png";
        break;
      case GOV_FCC_WIRELESSSERVICETYPE_LPAUX:
        icon = "red/entertainment/music_live.png";
        break;
      case GOV_FCC_WIRELESSSERVICETYPE_TV:
        icon = "red/industry/radio-station-2.png";
        break;
      case GOV_FCC_WIRELESSSERVICETYPE_BCAUX:
        icon = "red/industry/mobilephonetower.png";
        break;
      case GOV_FCC_WIRELESSSERVICETYPE_TV_RX:
//        icon = "red/industry/powerlinepole.png";
        icon = "red/industry/mobilephonetower.png";
        break;
      case GOV_FCC_WIRELESSSERVICETYPE_TV_RX_MVPD:
//        icon = "red/industry/powerlinepole.png";
        icon = "red/industry/metronetwork.png";
        break;
      case GOV_FCC_WIRELESSSERVICETYPE_WSD:
        icon = "red/computer/phones.png";
        break;
      default:
        icon = "red/office/wifi.png";
        break;
    }
    return urlBase + icon;
  }

  /**
   * @deprecated This Google API is deprecated and may go away Internal method
   * to get a URI icon according to the wirelessServiceType and channel.
   * <p/>
   * @param labelName the label name. This is typically a call sign
   * @param adjacent  indicator that the marker is adjacent or not. FALSE
   *                  returns a blue label, TRUE returns grey.
   * @return an icon label uri.
   * @see <a
   * href="https://google-developers.appspot.com/chart/infographics/docs/dynamic_icons#frame_style_constants">Dynamic
   * Icons</a>
   */
  public static String iconLabel(String labelName, boolean adjacent) {
    StringBuilder sb = new StringBuilder("https://chart.googleapis.com/chart?");
    sb.append("chst=d_bubble_text_small&").
      //      append("chld=edge_bc|").
      append("chld=bbT|").
      append(labelName.replace(" ", "+")).
      /**
       * Label color is |'background'|'text'
       */
      append(adjacent
        ? "|cccccc|222222"
        : "|0078ae|ffffff"); // blue
//        : "|e14f1c|ffffff"); // orange
    return sb.toString();
  }

  /**
   * Base method to build a map marker URI from a whitespace type and channel ID
   * number. This method is called by other getIconUriNumber builders.
   * <p/>
   * @param whitespaceType
   * @param number
   * @return
   */
  @SuppressWarnings("AssignmentToMethodParameter")
  public static String iconNumber(EWhitespaceType whitespaceType, long number) {
    String urlBase = "https://s3.amazonaws.com/keybridgeglobal/images/icon/map/";

    String color;
    if (whitespaceType == null) {
      return urlBase + "red/industry/mobilephonetower.png";
    }
    switch (whitespaceType) {
//      case KEYBRIDGE_DEMO:        color = "pink";        break;
      case GOV_FCC_WHITESPACETYPE_ASTR:
//        color = "green";        break;
      case GOV_FCC_WHITESPACETYPE_ORS:
//        color = "green";        break;
      case GOV_FCC_WHITESPACETYPE_URBAN:
        color = "green";
        break;
      case GOV_FCC_WHITESPACETYPE_LPAUX:
        color = "orange";
        break;
      case GOV_FCC_WHITESPACETYPE_TV:
        color = "blue";
        break;
      case GOV_FCC_WHITESPACETYPE_TV_DD:
        color = "blue";
        break;
      case GOV_FCC_WHITESPACETYPE_BCAUX:
        color = "pink";
        /**
         * If the BAS link number is zero then it is the transmitter. Plot a TX
         * tower.
         */
        if (number == 0) {
          return urlBase + "red/industry/mobilephonetower.png";
        }
        break;
      case GOV_FCC_WHITESPACETYPE_TV_RX_MVPD:
        color = "pink";
        break;
      case GOV_FCC_WHITESPACETYPE_LMRS:
        color = "blue";
        break;
      default:
        color = "red";
        break;
    }
    /**
     * Inspect the number to ensure a valid icon is returned.
     */
    if (number > 99) {
      number %= 100;
    } else if (number == 0) {
      return urlBase + "marker/" + color + "/blank.png";
    }
    return urlBase + "marker/" + color + "/marker" + number + ".png";
  }

```