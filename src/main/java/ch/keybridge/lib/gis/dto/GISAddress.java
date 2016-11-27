/*
 *  Copyright (C) 2012 Caulfield IP Holdings (Caulfield) and/or its affiliates.
 *  All rights reserved. Use is subject to license terms.
 *
 *  Software Code is protected by Caulfield Copyrights. Caulfield hereby
 *  reserves all rights in and to Caulfield Copyrights and no license is
 *  granted under Caulfield Copyrights in this Software License Agreement.
 *  Caulfield generally licenses Caulfield Copyrights for commercialization
 *  pursuant to the terms of either Caulfield's Standard Software Source Code
 *  License Agreement or Caulfield's Standard Product License Agreement.
 *  A copy of Caulfield's either License Agreement can be obtained on request
 *  by email from: info@caufield.org.
 */
package ch.keybridge.lib.gis.dto;

import ch.keybridge.lib.common.TextUtility;
import java.io.Serializable;
import java.util.Objects;
import javax.xml.bind.annotation.*;

/**
 * GISAddress is a standardized container for physical (e.g. mailing) street
 * information. This is a simplified implementation similar to the IETF ‘civic
 * street’ concept.
 * <p>
 * A valid street requires at least that the city, state and country are
 * identified.
 * <p>
 * WSIF street attributes and names are chosen to closely match existing US
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
   * Construct a new fully qualified street entity.
   *
   * @param address    the street street
   * @param city       the city
   * @param state      the state (or region)
   * @param postalCode the postal code (zip)
   * @param country    the ISO-2 country code
   */
  public GISAddress(String address, String city, String state, String postalCode, String country) {
    this.street = address;
    this.city = city;
    this.state = state;
    this.postalCode = postalCode;
    this.country = country;
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
     */
    return new StringBuilder()
            .append(street != null && !street.isEmpty() && !street.trim().equalsIgnoreCase("null")
                    ? TextUtility.properCase(street + ", ")
                    : "")
            .append(city != null && !city.isEmpty() && !city.trim().equalsIgnoreCase("null")
                    ? " " + TextUtility.properCase(city)
                    : "")
            .append(state != null && !state.isEmpty() && !state.trim().equalsIgnoreCase("null")
                    ? ", " + state.toUpperCase() + " "
                    : "")
            .append(postalCode != null && !postalCode.isEmpty() && !postalCode.trim().equalsIgnoreCase("null")
                    ? postalCode
                    : "")
            .toString();
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
