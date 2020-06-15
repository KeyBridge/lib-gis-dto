/*
 * Copyright 2017 Key Bridge. All rights reserved.
 * Use is subject to license terms.
 *
 * Software Code is protected by Copyrights. Author hereby reserves all rights
 * in and to Copyrights and no license is granted under Copyrights in this
 * Software License Agreement.
 *
 * Key Bridge generally licenses Copyrights for commercialization pursuant to
 * the terms of either a Standard Software Source Code License Agreement or a
 * Standard Product License Agreement. A copy of either Agreement can be
 * obtained upon request from: info@keybridgewireless.com
 */
package ch.keybridge.gis.dto;

import ch.keybridge.xml.JaxbUtility;
import javax.xml.bind.JAXBException;
import junit.framework.TestCase;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;

/**
 *
 * @author Key Bridge LLC
 */
public class PositionTest {

  public PositionTest() {
  }

  @Test
  public void testPositionXYZ() throws JAXBException {
    Position position = Position.getInstance(10.0, 20.0, 30.0, "WGS84", 0.5, 0.5);
    System.out.println(JaxbUtility.marshal(position));

    Coordinate coordinate = position.asCoordinate();
    System.out.println("as coordinate " + coordinate);

    /**
     * The X, Y and Z coordinates must match.
     */
    TestCase.assertEquals(position.getLongitude(), coordinate.x);
    TestCase.assertEquals(position.getLatitude(), coordinate.y);
    TestCase.assertEquals(position.getElevation(), coordinate.z);

  }

  @Test
  public void testPositionNullZ() throws JAXBException {
    Position position = Position.getInstance(10.0, 20.0);

    System.out.println(position.toString());

    System.out.println(JaxbUtility.marshal(position));

    Coordinate coordinate = position.asCoordinate();
    System.out.println("as coordinate " + coordinate);
    TestCase.assertEquals(Double.NaN, coordinate.z);
  }

}
