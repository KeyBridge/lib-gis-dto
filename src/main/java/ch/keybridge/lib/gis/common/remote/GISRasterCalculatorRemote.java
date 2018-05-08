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
package ch.keybridge.lib.gis.common.remote;

import ch.keybridge.lib.gis.dto.GISFeature;
import ch.keybridge.lib.gis.dto.GISFeatureCollection;
import ch.keybridge.lib.gis.dto.GISPosition;
import com.vividsolutions.jts.geom.Coordinate;

/**
 * EJB remote interface.
 * <p>
 * This interface defines various methods that are exposed by an EJB instance to
 * an application server and that may be accessed by another EJB via remote
 * annotation or JNDI lookup.
 *
 * @author Key Bridge
 * @since v3.2.2 added 05/15/17
 */
public interface GISRasterCalculatorRemote {

  /**
   * Calculate the ground height above average terrain (HAAT) in meters at the
   * indicated coordinate.
   * <p>
   * NOTE: This is the Height Above Average Terrain (HAAT) value. USE THIS
   * METHOD for FCC HAAT values. This method exists to support calculation of
   * antenna Height Above Average Terrain (HAAT) based on FCC Part 73.313(d).
   * Average terrain is calculated in 15-degree steps (8 radials).
   *
   * @param latitude  the query coordinate latitude
   * @param longitude the query coordinate longitude
   * @param elevation (optional) the position elevation; if provided this will
   *                  be used INSTEAD of the elevation model sample elevation.
   *                  Set to zero or to -1 to instruct the server to evaluate
   *                  this for you.
   * @param modelName the coverage model instance to use in this calculation
   * @param lowPower  boolean indicator that the Canadian LPTV configuration
   *                  should be used (i.e. 0 to 5 vs 3 to 16 km radials)
   * @return The ground height above average terrain (HAAT) in meters at the
   *         indicated coordinate.
   * @throws java.lang.Exception on error
   */
  GISPosition haat(double latitude, double longitude, double elevation, String modelName, boolean lowPower) throws Exception;

  /**
   * Calculate a collection of radial path elements.
   * <p>
   * This method supports arbitrary contour generation by providing radial
   * terrain profiles around a given position.
   *
   * @param latitude      the query coordinate latitude
   * @param longitude     the query coordinate longitude
   * @param distance      the desired path distance (meters)
   * @param azimuthalStep the stepping value (in degrees) by which the azimuth
   *                      should be incremented. If less than one the value 1 is
   *                      assigned. This adjusts the number of radials provided.
   * @param numPoints     the number of positions along the path. This is
   *                      automatically capped at the underlying raster
   *                      resolution.
   * @param modelName     the coverage model instance to use in this calculation
   * @return A collection of radial path elements
   * @throws java.lang.Exception on error
   */
  GISFeatureCollection radialPath(double latitude, double longitude, double distance, int azimuthalStep, int numPoints, String modelName) throws Exception;

  /**
   * Build a list of coordinates along a geodetic path between two numPositions
   * with their elevations set.
   *
   * @param startPosition the origin position
   * @param stopPosition  the termination position
   * @param numPoints     the number of positions along the path. Expected range
   *                      is 10 to 512.
   * @param modelName     the coverage model instance to use in this calculation
   * @return a GISFeature encapsulating an ordered list of coordinates along the
   *         path, from start to end, inclusive.
   * @throws Exception on error
   */
  GISFeature path(Coordinate startPosition, Coordinate stopPosition, int numPoints, String modelName) throws Exception;

}
