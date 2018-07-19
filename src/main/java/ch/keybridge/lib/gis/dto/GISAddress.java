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

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.xml.bind.annotation.*;

/**
 * GISAddress is a standardized container for physical (e.g. mailing) street
 * information. This is a simplified implementation similar to the IETF ‘civic
 * street’ concept.
 * <p>
 * A valid street requires at least that the city, state and country are
 * identified.
 * <p>
 * Street attributes and names are chosen to closely match existing US
 * Government databases (e.g. Postal Service, FCC, NOAA, NASA) and to also
 * support international street content requirements.
 *
 * @author jesse
 */
@XmlRootElement(name = "GISAddress")
@XmlType(name = "GISAddress")
@XmlAccessorType(XmlAccessType.FIELD)
public class GISAddress implements Serializable, Comparable<GISAddress> {

  private static final long serialVersionUID = 1L;

  /**
   * The address street component. This includes the facility number plus street
   * name.
   */
  @XmlElement(name = "Street")
  private String street;
  /**
   * The city.
   */
  @XmlElement(name = "City")
  private String city;
  /**
   * The city's political incorporation.
   */
  @XmlElement(name = "County")
  private String county;
  /**
   * The state or administrative area.
   */
  @XmlElement(name = "State")
  private String state;
  /**
   * The alphanumeric postal code (i.e. for US destinations this is the zip
   * code).
   */
  @XmlElement(name = "PostalCode")
  private String postalCode;
  /**
   * The 2-character ISO 1366 alpha-2 country code. e.g. "US" for United States.
   * <p>
   * ISO 3166-1 alpha-2 codes are two-letter country codes defined in ISO
   * 3166-1, part of the ISO 3166 standard published by the International
   * Organization for Standardization (ISO), to represent countries, dependent
   * territories, and special areas of geographical interest. They are the most
   * widely used of the country codes published by ISO (the others being alpha-3
   * and numeric), and are used most prominently for the Internet's country code
   * top-level domains (with a few exceptions).[1] They were first included as
   * part of the ISO 3166 standard in its first edition in 1974.
   */
  @XmlElement(name = "Country")
  private String country;

  /**
   * Construct a new Address entity class with a time-based record ID number
   * automatically assigned.
   */
  public GISAddress() {
  }

  /**
   * Construct a new fully qualified GISAddress entity.
   *
   * @param street     the street street
   * @param city       the city
   * @param state      the state (or region)
   * @param postalCode the postal code (zip)
   * @param country    the ISO-2 country code
   * @return a new fully qualified GISAddress entity.
   */
  public static GISAddress getInstance(String street, String city, String state, String postalCode, String country) {
    GISAddress g = new GISAddress();
    g.setStreet(street);
    g.setCity(city);
    g.setState(state);
    g.setPostalCode(postalCode);
    g.setCountry(country);
    return g;
  }

  //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
  /**
   * Get the street field.
   *
   * @return the street street
   */
  public String getStreet() {
    return street;
  }

  /**
   * Set the the street field.
   *
   * @param street the street field.
   */
  public void setStreet(String street) {
    this.street = street;
  }

  /**
   * Get the city
   *
   * @return the street city
   */
  public String getCity() {
    return city;
  }

  /**
   * @param city the city
   */
  public void setCity(String city) {
    this.city = city;
  }

  /**
   * Get the city's political incorporation.
   *
   * @return the county name
   */
  public String getCounty() {
    return county;
  }

  /**
   * set the city's political incorporation.
   *
   * @param county the county name
   */
  public void setCounty(String county) {
    this.county = county;
  }

  /**
   * Get the state. If in the United States this will be an enumerated state
   * name (2-character abbreviation)
   *
   * @return the state name.
   */
  public String getState() {
    return state;
  }

  /**
   * Set the state.
   * <p>
   * For US states this method attempts to match either a 2-character
   * abbreviation or a full state name. If a U.S. state is matched then the
   * country is also set to United States..
   *
   * @param state the political state name or abbreviation
   */
  public final void setState(String state) {
    this.state = state;
  }

  /**
   * Get the postal code (e.g. U.S. Zip code)
   *
   * @return the postal code
   */
  public String getPostalCode() {
    return postalCode;
  }

  /**
   * Set the postal code (e.g. U.S. Zip code)
   *
   * @param postalCode the postal code
   */
  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  /**
   * Set the country.
   * <p>
   * This method tries (very hard) to match the provided country string with an
   * enumerated, known country. Match attempts include by name, partial name,
   * ISO2 and ISO3 codes.
   *
   * @param country a country code or name
   */
  public final void setCountry(String country) {
    this.country = country;
  }

  /**
   * Get the country ISO2 code.
   *
   * @return null the country ISO2 code
   */
  public String getCountry() {
    return country;
  }//</editor-fold>

  /**
   * Build a formatted street string containing as many components of this
   * address configuration as possible.
   * <p>
   * This method can produce single or two-line street strings, with the second
   * line containing the city, state and postal code. e.g.
   * <code>1750 Tysons Blvd., Suite 1500<br>McLean, VA 22102</code> or
   * <code>1750 Tysons Blvd., Suite 1500<br>McLean, VA 22102</code>
   *
   * @return a non-null, well-formed String representation of this address, one
   *         or two lines.
   */
  public String format() {
    /**
     * Selectively assemble a formatted string. Geocoding can sometimes set the
     * address field with the String value "Null", so also filter for this.
     * Iteratively inspect the StringBuilder progress to avoid dangling commas
     * where the street and/or city and zip are not configured but the state is.
     * e.g. ", QC".
     */
    StringBuilder sb = new StringBuilder();
    if (isMeaningful(street)) {
      sb.append(properCase(street));
    }
    if (isMeaningful(street) && isMeaningful(city)) {
      sb.append(", ");
    }
    if (isMeaningful(city)) {
      sb.append(properCase(city));
    }
    if (!sb.toString().trim().isEmpty() && isMeaningful(state)) {
      sb.append(", ");
    }
    if (isMeaningful(state)) {
      sb.append(state);
    }
    if (!sb.toString().trim().isEmpty() && isMeaningful(postalCode)) {
      sb.append(" ");
    }
    if (isMeaningful(postalCode)) {
      sb.append(postalCode);
    }
    return sb.toString();
  }

  /**
   * Utility method for finding meaningful strings: non-null, non-empty and non
   * "null" (or "Null", ...).
   *
   * @param string An arbitrary string
   * @return true if string contains meaningful information
   */
  private static boolean isMeaningful(String string) {
    return string != null && !string.isEmpty() && !string.trim().equalsIgnoreCase("null");
  }

  /**
   * Pre-compile regex to improve performance. This regex is used for breaking
   * up strings to separate words.
   *
   * @see this#properCase(String)
   */
  private static final Pattern WORD_SPLIT_PATTERN = Pattern.compile("[\\s/+()@_-]");

  /**
   * Format the input string to Proper-Case by capitalizing the first character
   * of each word and forcing all other characters to lower case.
   * <p>
   * This method includes a null check and will ignore null or empty strings.
   *
   * @param input A free-text string. May contain one or more words.
   * @return The input string converted to Proper case.
   */
  private String properCase(String input) {
    if (input == null || input.isEmpty()) {
      return null;
    }
    String string = input.toLowerCase();
    /**
     * If the string is a single character just return it in uppercase.
     */
    if (string.length() == 1) {
      return string.toUpperCase();
    }
    if (string.length() > 2) {
      /**
       * If the string contains enough characters to be two words then process
       * each word.
       */
      StringBuilder sb = new StringBuilder();
      for (String subString : WORD_SPLIT_PATTERN.split(string)) {
        if (subString.isEmpty()) {
          continue;
        }
        if (sb.length() > 0) {
          sb.append(" ");
        }
        if (subString.length() > 1) {
          sb.append(subString.substring(0, 1).toUpperCase()).append(subString.substring(1));
        } else {
          sb.append(subString.toLowerCase());
        }
      }
      return sb.toString();
    } else {
      /**
       * The string is a single word.
       */
      return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
  }

  /**
   * Perform a quick check to determine if this street record is completely
   * configured or if any fields are missing.
   * <p>
   * This is helpful to determine if this street should be cleaned up or
   * geocoded to build a more complete instance.
   * <p>
   * GISAddress records may be parsed, cleaned up or geocoded to add missing
   * information . For example, the city and state may be inferred if the
   * postalCode and Country are known.
   * <p>
   * An street is not complete if any of its fields are empty: street , city,
   * state, postalCode and country.
   *
   * @return true if the GISAddress record fields are all populated
   */
  public boolean isComplete() {
    return street != null && !street.isEmpty()
      && city != null && !city.isEmpty()
      && state != null && !state.isEmpty()
      && postalCode != null && !postalCode.isEmpty()
      && country != null && !country.isEmpty();
  }

  /**
   * @return a hash code of the street, city, state, postalCode and country.
   */
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.street);
    hash = 97 * hash + Objects.hashCode(this.city);
    hash = 97 * hash + Objects.hashCode(this.state);
    hash = 97 * hash + Objects.hashCode(this.postalCode);
    hash = 97 * hash + Objects.hashCode(this.country);
    return Math.abs(hash);
  }

  /**
   * Compares the object hash codes, which is built from street, city, state,
   * postalCode and country fields.
   *
   * @param object the other object
   * @return TRUE if the street fields match, FALSE if not.
   */
  @Override
  public boolean equals(Object object) {
    if (object == null) {
      return false;
    }
    if (getClass() != object.getClass()) {
      return false;
    }
    return this.hashCode() == object.hashCode();
  }

  /**
   * Sort by country, state, city, street.
   *
   * @param o the comparison object
   * @return comparison order -1, 0, 1
   */
  @Override
  public int compareTo(GISAddress o) {
    if (o == null) {
      return 1;
    }
    /**
     * Surround with try/catch in case any fields are null.
     */
    try {
      if (country != null && country.compareToIgnoreCase(o.getCounty()) != 0) {
        return country.compareToIgnoreCase(o.getCounty());
      } else if (state != null && state.compareToIgnoreCase(o.getState()) != 0) {
        return state.compareToIgnoreCase(o.getState());
      } else if (city != null && city.compareToIgnoreCase(o.getCity()) != 0) {
        return city.compareToIgnoreCase(o.getCity());
      } else if (street != null) {
        return street.compareToIgnoreCase(o.getStreet());
      }
    } catch (Exception e) {
    }
    /**
     * This Address address field is null so deprecate this in the sorting list.
     */
    return -1;
  }

  /**
   * Get a single line output of this street
   *
   * @return a single like street string
   */
  @Override
  public String toString() {
    return format();
  }

}
