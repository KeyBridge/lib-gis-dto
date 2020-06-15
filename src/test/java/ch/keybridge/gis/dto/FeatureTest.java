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

//import ch.keybridge.lib.json.JsonUtility;
import ch.keybridge.json.JsonUtility;
import ch.keybridge.xml.JaxbUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import javax.xml.bind.JAXBException;
import org.junit.Test;

/**
 *
 * @author Key Bridge LLC
 */
public class FeatureTest {

  @Test
  public void testToText() throws JAXBException, JsonProcessingException {
    Position position = Position.getInstance(34.0123456, -87.0654321, 12345.0, "NAD83", 20.0, 10.0);
    Address address = Address.getInstance("10101 Binary Blvd.", "Boolean", "IO", "090909", "CONGO");
//    GISFeature feature = GISFeature.getInstance("featureType", "featureName", address, position, position.asPoint().buffer(.75));
    Feature feature = Feature.getInstanceWithCss();
    feature.setFeatureType("featureType");
    feature.setName("featureName");
    feature.setAddress(address);
    feature.setPosition(position);
    feature.setShape(position.asPoint().buffer(.75));
    feature.setId(1024);
    feature.setDescription("featureDescription");

//    "featureType", "featureName", address, position, position.asPoint().buffer(.75));
    JsonUtility.marshal(feature);
    System.out.println("  Ye JSON marshalled OK");

    JaxbUtility.marshal(feature);
    System.out.println("  Ye XML  marshalled ok");

    System.out.println("Marshal to JSON and XML OK");

  }

}
