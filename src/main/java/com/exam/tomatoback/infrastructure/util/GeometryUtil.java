package com.exam.tomatoback.infrastructure.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Component;

@Component
public class GeometryUtil {
  private final static GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

  // (경도, 위도)
  public static Point exchangePoint(double x, double y) {
    Coordinate coordinate = new Coordinate(x, y);
    return geometryFactory.createPoint(coordinate);
  }
}
