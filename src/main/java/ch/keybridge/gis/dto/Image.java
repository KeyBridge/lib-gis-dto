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
package ch.keybridge.gis.dto;

import ch.keybridge.xml.adapter.XmlBase64CompressedAdapter;
import ch.keybridge.xml.adapter.XmlEnvelopeAdapter;
import ch.keybridge.xml.adapter.XmlZonedDateTimeAdapter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

/**
 * A Generic Data transfer object for an Image reference. This simple container
 * facilitates the exchange of generated images.
 *
 * @author Key Bridge LLC
 * @since 10/12/15
 */
@XmlRootElement(name = "Image")
@XmlType(name = "Image")
@XmlAccessorType(XmlAccessType.FIELD)
public class Image {

  /**
   * The ID for the image.
   */
  @XmlElement(name = "Id")
  private String id;
  /**
   * The filename, if requested.
   */
  @XmlElement(name = "Name")
  private String name;
  /**
   * Description of the image.
   */
  @XmlElement(name = "Description")
  private String description;
  /**
   * An image category to help sort and organize the image.
   */
  @XmlElement(name = "Category")
  private String category;
  /**
   * Date Time stamp when this image was created.
   *
   * @since v3.4.0 change from Data to ZonedDateTime
   */
  @XmlAttribute(name = "dateCreated")
  @XmlJavaTypeAdapter(XmlZonedDateTimeAdapter.class)
  private ZonedDateTime dateCreated;
  /**
   * Image MIME type.
   */
  @XmlElement(name = "MimeType")
  private String mimeType;
  /**
   * The width of the image in pixels.
   */
  @XmlElement(name = "Width")
  private Integer width;
  /**
   * The height of the image in pixels.
   */
  @XmlElement(name = "Height")
  private Integer height;
  /**
   * The size of the image in bytes.
   */
  @XmlElement(name = "Size")
  private Integer size;
  /**
   * The direct URL link to the the image.
   */
  @XmlElement(name = "URL")
  private String url;

  /**
   * The geographic boundary of this image.
   */
  @XmlElement(name = "Envelope")
  @XmlJavaTypeAdapter(XmlEnvelopeAdapter.class)
  private Envelope envelope;

  /**
   * The binary image data. XML output is encoded hexBinary is binary data
   * encoded in hexadecimal.
   * <p>
   * The xsd:hexBinary type represents binary data as a sequence of binary
   * octets. It uses hexadecimal encoding, where each binary octet is a
   * two-character hexadecimal number.
   */
  @XmlElement(name = "Image")
  @XmlJavaTypeAdapter(value = XmlBase64CompressedAdapter.class)
//  @JsonSerialize(using = JsonBase64Adapter.Serializer.class)
//  @JsonDeserialize(using = JsonBase64Adapter.Deserializer.class)
  private byte[] image;

  public Image() {
  }

  /**
   * Read image data from an InputStream. The image type must also be provided.
   * <p>
   * Calls ImageIO to build a BufferedImage as the result of decoding a supplied
   * InputStream with an ImageReader chosen automatically from among those
   * currently registered. This method does not close the provided InputStream
   * after the read operation has completed; it is the responsibility of the
   * caller to close the stream, if desired.
   *
   * @param inputStream an InputStream to read from.
   * @param formatName  a String containg the informal name of the image format.
   *                    e.g. [png, jpg, jpeg, gif]
   * @throws IOException if the inputstream fails to read
   */
  public void readImageData(InputStream inputStream, String formatName) throws IOException {
    BufferedImage bufferedImage = ImageIO.read(inputStream);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ImageIO.write(bufferedImage, formatName, outputStream);
    this.image = outputStream.toByteArray();
  }

  //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
  /**
   * Get the id for the image
   *
   * @return The ID for the image
   */
  public String getId() {
    return id;
  }

  /**
   * Set the id for the image
   *
   * @param id The ID for the image
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Get description of the image.
   *
   * @return Description of the image.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Set description of the image.
   *
   * @param description Description of the image.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Get time uploaded. Expect UTC timezone.
   *
   * @return Time uploaded.
   */
  public ZonedDateTime getDateCreated() {
    return dateCreated;
  }

  /**
   * Set time uploaded. Expect UTC timezone.
   *
   * @param dateCreated Time uploaded
   */
  public void setDateCreated(ZonedDateTime dateCreated) {
    this.dateCreated = dateCreated;
  }

  /**
   * Get image mime type.
   *
   * @return Image MIME type.
   */
  public String getMimeType() {
    return mimeType;
  }

  /**
   * Set image mime type.
   *
   * @param mimeType Image MIME type.
   */
  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  /**
   * Get the width of the image in pixels
   *
   * @return The width of the image in pixels
   */
  public Integer getWidth() {
    return width;
  }

  /**
   * Set the width of the image in pixels
   *
   * @param width The width of the image in pixels
   */
  public void setWidth(Integer width) {
    this.width = width;
  }

  /**
   * Get the height of the image in pixels
   *
   * @return The height of the image in pixels
   */
  public Integer getHeight() {
    return height;
  }

  /**
   * Set the height of the image in pixels
   *
   * @param height The height of the image in pixels
   */
  public void setHeight(Integer height) {
    this.height = height;
  }

  /**
   * Get the size of the image in bytes
   *
   * @return The size of the image in bytes
   */
  public Integer getSize() {
    return size;
  }

  /**
   * Set the size of the image in bytes
   *
   * @param size The size of the image in bytes
   */
  public void setSize(Integer size) {
    this.size = size;
  }

  /**
   * Get optional, the original filename, if you're logged in as the image owner
   *
   *
   * @return OPTIONAL, the original filename, if you're logged in as the image
   *         owner
   */
  public String getName() {
    return name;
  }

  /**
   * Set optional, the original filename, if you're logged in as the image owner
   *
   *
   * @param name OPTIONAL, the original filename, if you're logged in as the
   *             image owner
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get if the image has been categorized by our backend then this will contain
   * the section the image belongs in. (funny, cats, adviceanimals, wtf, etc)
   *
   *
   * @return If the image has been categorized by our backend then this will
   *         contain the section the image belongs in. (funny, cats,
   *         adviceanimals, wtf, etc)
   */
  public String getCategory() {
    return category;
  }

  /**
   * Set if the image has been categorized by our backend then this will contain
   * the section the image belongs in. (funny, cats, adviceanimals, wtf, etc)
   *
   *
   * @param category If the image has been categorized by our backend then this
   *                 will contain the section the image belongs in. (funny,
   *                 cats, adviceanimals, wtf, etc)
   */
  public void setCategory(String category) {
    this.category = category;
  }

  /**
   * Get the direct url to the the image. (note: if fetching an animated gif
   * that was over 20mb in original size, a .gif thumbnail will be returned)
   *
   *
   * @return The direct url to the the image. (Note: if fetching an animated GIF
   *         that was over 20MB in original size, a .gif thumbnail will be
   *         returned)
   */
  public String getUrl() {
    return url;
  }

  /**
   * Set the direct url to the the image. (note: if fetching an animated gif
   * that was over 20mb in original size, a .gif thumbnail will be returned)
   *
   *
   * @param url The direct url to the the image. (Note: if fetching an animated
   *            GIF that was over 20MB in original size, a .gif thumbnail will
   *            be returned)
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * Set the image binary data.
   *
   * @return the image data
   */
  public byte[] getImage() {
    return image;
  }

  /**
   * Get the image binary data.
   *
   * @param image the image data
   */
  public void setImage(byte[] image) {
    this.image = image;
  }

  /**
   * Get the image geographic boundary. This is a square polygon.
   *
   * @return the image geographic boundary.
   */
  public Envelope getEnvelope() {
    return envelope;
  }

  /**
   * Set the image geographic boundary.
   * <p>
   * Note: The ENVELOPE of the input geometry type is recorded, not the actual
   * input geometry.
   *
   * @param envelope the image geographic boundary.
   */
  public void setEnvelope(Envelope envelope) {
    this.envelope = envelope;
  }

  /**
   * Get then Northern boundary extent.
   *
   * @return the Northern boundary extent.
   */
  public double getNorth() {
    return envelope.getMaxY();
  }

  /**
   * Get then Southern boundary extent.
   *
   * @return the Southern boundary extent.
   */
  public double getSouth() {
    return envelope.getMinY();
  }

  /**
   * Get then Eastern boundary extent.
   *
   * @return the Eastern boundary extent.
   */
  public double getEast() {
    return envelope.getMaxX();
  }

  /**
   * Get then Western boundary extent.
   *
   * @return the Western boundary extent.
   */
  public double getWest() {
    return envelope.getMinX();
  }//</editor-fold>

  /**
   * Convert this Image to a Feature.
   *
   * @return this image as a Feature
   * @since v3.4.0 added 06/17/19 to support geojson encoding
   */
  public Feature asFeature() {
    Feature f = new Feature();
    f.setId(id);
    f.setName(name);
    f.setDescription(description);
    f.setFeatureType("image");
    f.setProperty("category", category);
    f.setProperty("dateCreated", dateCreated);
    f.setProperty("mimeType", mimeType);
    f.setProperty("width", width);
    f.setProperty("height", height);
    f.setProperty("size", size);
    f.setProperty("url", url);
    f.setShape(toGeometry(envelope));
    return f;
  }

  /**
   * Converts an envelope to a JTS polygon using the given JTS geometry factory.
   * <p>
   * <p>
   * The resulting polygon contains an outer ring with vertices:
   * (x1,y1),(x2,y1),(x2,y2),(x1,y2),(x1,y1)
   *
   * @param envelope The original envelope.
   * @return The envelope as a polygon.
   * @see copied from org.geotools.geometry.jts.JTS.toGeometry() method
   * @since v3.4.0 added 06/17/19 to support geojson encoding
   */
  private Polygon toGeometry(final Envelope envelope) {
    GeometryFactory factory = new GeometryFactory();
    return factory.createPolygon(
      factory.createLinearRing(
        new Coordinate[]{
          new Coordinate(envelope.getMinX(), envelope.getMinY()),
          new Coordinate(envelope.getMaxX(), envelope.getMinY()),
          new Coordinate(envelope.getMaxX(), envelope.getMaxY()),
          new Coordinate(envelope.getMinX(), envelope.getMaxY()),
          new Coordinate(envelope.getMinX(), envelope.getMinY())
        }),
      null);
  }

  /**
   * Equals and Hash Code are based upon the ID field.
   *
   * @return a unique hashcode
   */
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 89 * hash + Objects.hashCode(this.id);
    return hash;
  }

  /**
   * Equals and Hash Code are based upon the ID field.
   *
   * @param obj the other object
   * @return TRUE if the ID fields are equal.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Image other = (Image) obj;
    return Objects.equals(this.id, other.id);
  }

  /**
   * ToString returns the URL url.
   *
   * @return the URL url if configured, else an empty (non-null) String.
   */
  @Override
  public String toString() {
    return url != null ? url : "";
  }

}
