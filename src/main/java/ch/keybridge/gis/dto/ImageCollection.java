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
package ch.keybridge.gis.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.annotation.*;

/**
 * A Generic Data transfer object for a collection of Image objects. This simple
 * container facilitates the exchange of generated images.
 *
 * @author Key Bridge LLC
 * @since 12/14/18
 * @since v3.4.0 renamed from 'Images' to 'ImageCollection' to follow Feature
 * example.
 */
@XmlRootElement(name = "ImageCollection")
@XmlType(name = "ImageCollection")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImageCollection {

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
   * A list of images.
   */
  @XmlElement(name = "Images")
  private List<Image> images;

  public ImageCollection() {
    this.images = new ArrayList<>();
  }

  public List<Image> getImages() {
    return images;
  }

  public void setImages(List<Image> images) {
    this.images = images;
  }

  public void addImages(Image... images) {
    this.images.addAll(Arrays.asList(images));
  }

  public void addImage(Image image) {
    images.add(image);
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 97 * hash + Objects.hashCode(this.images);
    return hash;
  }

  /**
   * Equality requires that the image lists exactly match.
   *
   * @param obj the other instance.
   * @return true if the image lists exactly match.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final ImageCollection other = (ImageCollection) obj;
    return this.images.containsAll(other.getImages()) && other.getImages().containsAll(images);
  }

}
