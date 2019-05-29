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
package ch.keybridge.lib.gis.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.annotation.*;

/**
 * A Generic Data transfer object for an list of Image objects. This simple
 * container facilitates the exchange of generated images.
 *
 * @author Key Bridge LLC
 * @since 12/14/18
 */
@XmlRootElement(name = "Images")
@XmlType(name = "Images")
@XmlAccessorType(XmlAccessType.FIELD)
public class Images {

  /**
   * A list of images
   */
  @XmlElementWrapper(name = "Images")
  @XmlElement(name = "Image")
  private List<Image> images;

  public Images() {
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
    final Images other = (Images) obj;
    return this.images.containsAll(other.getImages()) && other.getImages().containsAll(images);
  }

}
