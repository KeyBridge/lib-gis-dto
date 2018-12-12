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

import ch.keybridge.lib.xml.JaxbUtility;
import ch.keybridge.lib.xml.adapter.XmlEnvelopeAdapter;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import javax.xml.bind.JAXBException;
import org.junit.Test;

/**
 *
 * @author Key Bridge LLC
 */
public class GISFeatureTest {

  @Test
  public void testFeature() throws JAXBException, Exception {
    GISPosition position = GISPosition.getInstance(34.123, -87.654, 12345.0, "NAD83", 20.0, 10.0);
    GISAddress address = GISAddress.getInstance("10101 Binary Blvd.", "Boolean", "IO", "090909", "CONGO");
    GISFeature feature = GISFeature.getInstance("featureType", "featureName", address, position, position.asPoint().buffer(.75));

    System.out.println(JaxbUtility.marshal(feature));

    Geometry geometry = position.asPoint().buffer(10);
//    System.out.println("Geometry " + geometry);
//    System.out.println("Boundary " + geometry.getEnvelope());
//    System.out.println("Envelope " + geometry.getEnvelopeInternal());

    XmlEnvelopeAdapter adapter = new XmlEnvelopeAdapter();

    System.out.println("Marshal");
    String env = adapter.marshal(geometry.getEnvelopeInternal());
    System.out.println(env);
    System.out.println("Unmarshal");
    Envelope envelope = adapter.unmarshal(env);
    System.out.println(envelope);

  }

}
