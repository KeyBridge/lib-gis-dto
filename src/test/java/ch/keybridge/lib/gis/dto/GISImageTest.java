/*
 * Copyright 2016 Key Bridge.
 *
 * All rights reserved. Use is subject to license terms.
 * This software is protected by copyright.
 *
 * See the License for specific language governing permissions and
 * limitations under the License.
 */
package ch.keybridge.lib.gis.dto;

import ch.keybridge.lib.gis.dto.GISImage;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import junit.framework.TestCase;

/**
 *
 * @author Key Bridge LLC
 */
public class GISImageTest extends TestCase {

  public GISImageTest(String testName) {
    super(testName);
  }

  public void testBoundary() {

    Point point = new GeometryFactory().createPoint(new Coordinate(0, 0));
    Geometry circle = point.buffer(10);

    GISImage image = new GISImage();
    image.setBoundary(circle);

//    System.out.println("Geometry : " + circle);
    System.out.println("Boundary : " + image.getBoundary());
    System.out.println("north : " + image.getNorth());
    System.out.println("south : " + image.getSouth());
    System.out.println("east  : " + image.getEast());
    System.out.println("west  : " + image.getWest());

    assertTrue(image.getNorth() > image.getSouth());
    assertTrue(image.getEast() > image.getWest());
    System.out.println("GIS Image boundary test OK. ");
  }

}
