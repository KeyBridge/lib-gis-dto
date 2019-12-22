/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.keybridge.gis.dto.rs;

import ch.keybridge.geojson.io.GeoJsonWriter;
import ch.keybridge.gis.dto.FeatureCollection;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

/**
 * A MessageBodyWriter of FeatureCollection object to (geo)JSON.
 * <p>
 * This media type is intended for GeoJSON applications currently using the
 * "application/ vnd.geo+json" or "application/json" media types, of which there
 * are several categories: web mapping, geospatial databases, geographic data
 * processing APIs, data analysis and storage services, and data dissemination.
 *
 * @author KeyBridge
 * @since v1.0.0 added 06/17/19
 * @see
 * <a href="https://www.iana.org/assignments/media-types/application/geo+json">Geo
 * JSON</a>
 */
@Provider
@Produces({MediaType.APPLICATION_JSON, "application/geo+json", "application/geojson"})
public class FeatureCollectionJsonWriter implements MessageBodyWriter<FeatureCollection> {

  /**
   * {@inheritDoc} Always returns TRUE.
   */
  @Override
  public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return true;
  }

  /**
   * {@inheritDoc}
   *
   * @deprecated deprecated by JAX-RS 2.0 and ignored by Jersey runtime
   */
  @Override
  public long getSize(FeatureCollection t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return 0;
  }

  /**
   * {@inheritDoc} Force the use of Jackson serializer.
   */
  @Override
  public void writeTo(FeatureCollection t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
    entityStream.write(GeoJsonWriter.write(t).getBytes(StandardCharsets.UTF_8));
  }
}
