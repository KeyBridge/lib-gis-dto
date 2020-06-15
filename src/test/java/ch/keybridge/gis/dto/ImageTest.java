/*
 * Copyright 2016 Key Bridge.
 *
 * All rights reserved. Use is subject to license terms.
 * This software is protected by copyright.
 *
 * See the License for specific language governing permissions and
 * limitations under the License.
 */
package ch.keybridge.gis.dto;

import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import static org.junit.Assert.assertTrue;

/**
 *
 * @author Key Bridge LLC
 */
public class ImageTest {

  @Test
  public void testBoundary() {

    Point point = new GeometryFactory().createPoint(new Coordinate(0, 0));
    Geometry circle = point.buffer(10);

    Image image = new Image();
    image.setEnvelope(circle.getEnvelopeInternal());

//    System.out.println("Geometry : " + circle);
    System.out.println("Boundary : " + image.getEnvelope());
    System.out.println("north : " + image.getNorth());
    System.out.println("south : " + image.getSouth());
    System.out.println("east  : " + image.getEast());
    System.out.println("west  : " + image.getWest());

    assertTrue(image.getNorth() > image.getSouth());
    assertTrue(image.getEast() > image.getWest());
    System.out.println("GIS Image boundary test OK. ");
  }

}
