/*
 * Copyright 2019 Key Bridge. All rights reserved. Use is subject to license
 * terms.
 *
 * This software code is protected by Copyrights and remains the property of
 * Key Bridge and its suppliers, if any. Key Bridge reserves all rights in and to
 * Copyrights and no license is granted under Copyrights in this Software
 * License Agreement.
 *
 * Key Bridge generally licenses Copyrights for commercialization pursuant to
 * the terms of either a Standard Software Source Code License Agreement or a
 * Standard Product License Agreement. A copy of either Agreement can be
 * obtained upon request by sending an email to info@keybridgewireless.com.
 *
 * All information contained herein is the property of Key Bridge and its
 * suppliers, if any. The intellectual and technical concepts contained herein
 * are proprietary.
 */
package ch.keybridge.gis.dto.rs;

import ch.keybridge.geojson.io.GeoJsonWriter;
import ch.keybridge.gis.dto.Image;
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
 * A MessageBodyWriter of an Image object to (geo)JSON. This writer first
 * converts the image to a Feature, then sends the feature in GeoJSON format.
 *
 * @author Key Bridge
 * @since v1.0.0 added 06/17/19
 */
@Provider
@Produces({MediaType.APPLICATION_JSON, "application/geo+json", "application/geojson"})
public class ImageJsonWriter implements MessageBodyWriter<Image> {

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
  public long getSize(Image t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return 0;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Convert the Image to a Feature, then write as GeoJson.
   */
  @Override
  public void writeTo(Image t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
    entityStream.write(GeoJsonWriter.write(t.asFeature()).getBytes(StandardCharsets.UTF_8));
  }

}
