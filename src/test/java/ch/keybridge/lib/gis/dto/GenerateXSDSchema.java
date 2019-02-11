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

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import org.junit.Test;

/**
 * Using JAXBContext to Generate an XML Schema from
 * http://wiki.eclipse.org/EclipseLink/Examples/MOXy/JAXB/GenerateSchema
 *
 * @author jesse
 */
public class GenerateXSDSchema {

  /**
   * Generate a XML Schema and store it in the docs/xsd directory.
   *
   * @throws JAXBException
   * @throws IOException
   */
  @Test
  public void testGenerateSchema() throws JAXBException, IOException {

    System.out.println("Generate a XML Schema and store it in the docs/xsd directory");

    Set<Class> classes = new HashSet<>();
    classes.add(FeatureCollection.class);
    classes.add(Feature.class);
    classes.add(Address.class);
    classes.add(Position.class);
//    classes.add(GISImage.class);

    JAXBContext jaxb = JAXBContext.newInstance(classes.toArray(new Class[classes.size()]));
    SchemaOutputResolver resolver = new MySchemaOutputResolver();
    jaxb.generateSchema(resolver);

  }

  /**
   * First you must create a class that extends
   * javax.xml.bind.SchemaOutputResolver.
   */
  private class MySchemaOutputResolver extends SchemaOutputResolver {

    @Override
    public Result createOutput(String namespaceURI, String suggestedFileName) throws IOException {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      /**
       * suggestedFileName is "schema1.xsd"
       */
      java.nio.file.Path path = Paths.get("docs", "xsd", "gis-dto." + sdf.format(new Date()) + ".xsd");
      File file = path.toFile();
      System.out.println("  Writing XSD Schema file to " + file);
      StreamResult result = new StreamResult(file);
      result.setSystemId(file.toURI().toURL().toString());
      return result;
    }

  }

}
