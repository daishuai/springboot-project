package com.kem.elastic.plugin;

import com.kem.elastic.plugin.spatial.CircleProcessor;
import org.elasticsearch.common.geo.builders.LineStringBuilder;
import org.elasticsearch.common.geo.builders.PolygonBuilder;
import org.elasticsearch.common.geo.builders.ShapeBuilder;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.geometry.*;
import org.locationtech.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liupeiqi
 * @Type CircleQueryBuilder.java
 * @date 2020/4/28 14:17
 * @Desc  构建圆形
 */
public class CircleQueryBuilder {

    private DistanceUnit unit = DistanceUnit.DEFAULT;
    private double radius;
    private double lon;
    private double lat;

    private CircleProcessor.Factory factory;

    CircleQueryBuilder() {
        factory = new CircleProcessor.Factory();
    }

    public static CircleQueryBuilder newCircleBuilder() {
        return new CircleQueryBuilder();
    }

    /**
     * 圆形的中心点
     *
     * @param lon
     * @param lat
     * @return
     */
    public CircleQueryBuilder center(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
        return this;
    }

    /**
     * 圆形的半径
     *
     * @param radius
     * @param unit
     * @return
     */
    public CircleQueryBuilder radius(double radius, DistanceUnit unit) {
        if (radius <= 0) {
            throw new IllegalArgumentException("invalid radius, radius must > 0");
        }
        this.unit = unit;
        this.radius = radius;
        return this;
    }

    /**
     * 构建圆形的build
     *
     * @return
     * @throws Exception
     */
    public ShapeBuilder build() {
        try {
            CircleProcessor circleProcessor = factory.create();
            Circle circle = new Circle(this.lon, this.lat, this.unit.toMeters(this.radius));
            Polygon polygon = circleProcessor.execute(circle);
            return geometryToShapeBuilder(polygon);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid circle build", e);
        }
    }

    /**
     * 几何图形构建
     *
     * @param geometry
     * @return
     * @throws Exception
     */
    private static ShapeBuilder geometryToShapeBuilder(Geometry geometry) throws Exception {
        ShapeBuilder shapeBuilder = geometry.visit(new GeometryVisitor<ShapeBuilder, Exception>() {
            @Override
            public ShapeBuilder visit(Circle circle) {
                return null;
            }

            @Override
            public ShapeBuilder visit(GeometryCollection<?> collection) {
                return null;
            }

            @Override
            public ShapeBuilder visit(Line line) {
                List<Coordinate> coordinates = new ArrayList<>();
                for (int i = 0; i < line.length(); i++) {
                    coordinates.add(new Coordinate(line.getX(i), line.getY(i), line.getZ(i)));
                }
                return new LineStringBuilder(coordinates);
            }

            @Override
            public ShapeBuilder visit(LinearRing ring) {
                return null;
            }

            @Override
            public ShapeBuilder visit(MultiLine multiLine) {
                return null;
            }

            @Override
            public ShapeBuilder visit(MultiPoint multiPoint) {
                return null;
            }

            @Override
            public ShapeBuilder visit(MultiPolygon multiPolygon) {
                return null;
            }

            @Override
            public ShapeBuilder visit(Point point) {
                return null;
            }

            @Override
            public ShapeBuilder visit(Polygon polygon) {
                PolygonBuilder polygonBuilder =
                        new PolygonBuilder((LineStringBuilder) visit((Line) polygon.getPolygon()),
                                ShapeBuilder.Orientation.RIGHT, false);
                for (int i = 0; i < polygon.getNumberOfHoles(); i++) {
                    polygonBuilder.hole((LineStringBuilder) visit((Line) polygon.getHole(i)));
                }
                return polygonBuilder;
            }

            @Override
            public ShapeBuilder visit(Rectangle rectangle) {
                return null;
            }
        });
        return shapeBuilder;
    }
}

