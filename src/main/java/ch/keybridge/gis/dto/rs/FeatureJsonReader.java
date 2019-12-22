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

import ch.keybridge.geojson.io.GeoJsonReader;
import ch.keybridge.gis.dto.Feature;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.stream.Collectors;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

/**
 * A MessageBodyReader of Feature objects from (geo)JSON.
 * <p>
 * Enables upload of (geo)JSON encoded features.
 *
 * @author Key Bridge
 * @since v1.0.0 added 06/17/19
 */
@Provider
@Consumes({MediaType.APPLICATION_JSON, "application/geo+json", "application/geojson"})
public class FeatureJsonReader implements MessageBodyReader<Feature> {

  /**
   * {@inheritDoc} Always returns TRUE.
   */
  @Override
  public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return true;
  }

  /**
   * {@inheritDoc} Use the GeoJsonReader to read a Feature.
   */
  @Override
  public Feature readFrom(Class<Feature> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
    try {
      String json = new BufferedReader(new InputStreamReader(entityStream)).lines().collect(Collectors.joining("\n"));
      return GeoJsonReader.readFeature(json);
    } catch (Exception exception) {
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }
  }

}
