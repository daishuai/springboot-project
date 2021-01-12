package com.kem.elastic.plugin.spatial;

import org.elasticsearch.geometry.Circle;
import org.elasticsearch.geometry.Polygon;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Locale;

/**
 * @author liupeiqi
 * @Type CircleProcessor.java
 * @date 2020/4/28 14:09
 * @Desc The circle-processor converts a circle shape definition into a valid regular polygon approximating the circle.
 */
public final class CircleProcessor {
    public static final String TYPE = "circle";
    static final int MINIMUM_NUMBER_OF_SIDES = 4;
    static final int MAXIMUM_NUMBER_OF_SIDES = 1000;
    private final double errorDistance;
    private final CircleShapeFieldType circleShapeFieldType;

    CircleProcessor(double errorDistance, CircleShapeFieldType circleShapeFieldType) {
        this.errorDistance = errorDistance;
        this.circleShapeFieldType = circleShapeFieldType;
    }

    public Polygon execute(Circle circle) {
        if (null == circle) {
            throw new IllegalArgumentException("circle is null");
        }
        Polygon polygon;
        try {
            int numSides = numSides(circle.getRadiusMeters());
            switch (circleShapeFieldType) {
                case SHAPE:
                    polygon = SpatialUtils.createRegularShapePolygon(circle, numSides);
                    break;
                case GEO_SHAPE:
                    polygon = SpatialUtils.createRegularGeoShapePolygon(circle, numSides);
                    break;
                default:
                    throw new IllegalStateException("invalid shape_type [" + circleShapeFieldType + "]");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid circle definition", e);
        }
        return polygon;
    }

    public static final class Factory {
        public CircleProcessor create() {
            return new CircleProcessor(20, CircleShapeFieldType.GEO_SHAPE);
        }
    }

    int numSides(double radiusMeters) {
        int val = (int) Math.ceil(2 * Math.PI / Math.acos(1 - errorDistance / radiusMeters));
        return Math.min(MAXIMUM_NUMBER_OF_SIDES, Math.max(MINIMUM_NUMBER_OF_SIDES, val));
    }

    public enum CircleShapeFieldType {
        SHAPE, GEO_SHAPE;

        public static CircleShapeFieldType parse(String value) {
            EnumSet<CircleShapeFieldType> validValues = EnumSet.allOf(CircleShapeFieldType.class);
            try {
                return valueOf(value.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("illegal [shape_type] value [" + value + "]. valid values are " +
                        Arrays.toString(validValues.toArray()));
            }
        }
    }
}

