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

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A Generic Data transfer object for an list of Image objects. This simple container
 * facilitates the exchange of generated images.
 *
 * @author Key Bridge LLC
 * @since 12/14/18
 */
@XmlRootElement(name = "GISImages")
@XmlType(name = "GISImages")
@XmlAccessorType(XmlAccessType.FIELD)
public class GISImages {

  /**
   * A list of images
   */
  @XmlElementWrapper(name = "Images")
  @XmlElement(name = "Image")
  List<GISImage> images = new ArrayList<>();

  public List<GISImage> getImages() {
    return images;
  }

  public void setImages(List<GISImage> images) {
    this.images = images;
  }

  public void addImages(GISImage... images) {
    this.images.addAll(Arrays.asList(images));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    GISImages gisImages = (GISImages) o;

    return images != null ? images.equals(gisImages.images) : gisImages.images == null;
  }

  @Override
  public int hashCode() {
    return images != null ? images.hashCode() : 0;
  }
}
