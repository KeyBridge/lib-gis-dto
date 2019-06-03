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
package ch.keybridge.lib.gis.dto;

import ch.keybridge.lib.xml.adapter.XmlBase64Adapter;
import ch.keybridge.lib.xml.adapter.XmlDateTimeAdapter;
import ch.keybridge.lib.xml.adapter.XmlEnvelopeAdapter;
import java.util.Date;
import java.util.Objects;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.locationtech.jts.geom.Envelope;

/**
 * A Generic Data transfer object for an Image. This simple container
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
  @XmlElement(name = "ID")
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
   */
  @XmlAttribute(name = "dateTimeCreated")
  @XmlJavaTypeAdapter(type = Date.class, value = XmlDateTimeAdapter.class)
//  @JsonSerialize(using = JsonDateTimeAdapter.Serializer.class)
//  @JsonDeserialize(using = JsonDateTimeAdapter.Deserializer.class)
  private Date dateCreated;
  /**
   * Image MIME type.
   */
  @XmlAttribute(name = "mimeType")
  private String mimeType;
  /**
   * The width of the image in pixels.
   */
  @XmlAttribute(name = "width")
  private Integer width;
  /**
   * The height of the image in pixels.
   */
  @XmlAttribute(name = "height")
  private Integer height;
  /**
   * The size of the image in bytes.
   */
  @XmlAttribute(name = "size")
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
//  @JsonSerialize(using = JsonGeometryAdapter.Serializer.class)
//  @JsonDeserialize(using = JsonGeometryAdapter.Deserializer.class)
  private Envelope envelope;

  /**
   * The image. XML output is encoded hexBinary is binary data encoded in
   * hexadecimal.
   * <p>
   * The xsd:hexBinary type represents binary data as a sequence of binary
   * octets. It uses hexadecimal encoding, where each binary octet is a
   * two-character hexadecimal number.
   */
  @XmlElement(name = "Image")
  @XmlJavaTypeAdapter(value = XmlBase64Adapter.class)
//  @JsonSerialize(using = JsonBase64Adapter.Serializer.class)
//  @JsonDeserialize(using = JsonBase64Adapter.Deserializer.class)
  private byte[] image;

  public Image() {
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
   * Get time uploaded, epoch time
   *
   * @return Time uploaded, epoch time
   */
  public Date getDateCreated() {
    return dateCreated != null ? new Date(dateCreated.getTime()) : null;
  }

  /**
   * Set time uploaded, epoch time
   *
   * @param dateCreated Time uploaded, epoch time
   */
  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated != null ? new Date(dateCreated.getTime()) : null;
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
   * Set the image
   *
   * @return the image
   */
  public byte[] getImage() {
    return image;
  }

  /**
   * Get the image.
   *
   * @param image the image
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
