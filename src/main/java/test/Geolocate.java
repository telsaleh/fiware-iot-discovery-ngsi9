/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author te0003
 */

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.util.GeometricShapeFactory;
//import java.awt.Polygon;
import java.awt.geom.Path2D;
import java.util.ArrayList;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;



public class Geolocate {

  public static void main(final String[] args) {

    final GeometryFactory gf = new GeometryFactory();
    
    final Coordinate target = new Coordinate(49, -0.6);
    final Point point = gf.createPoint(target);
    
    final Coordinate bbox_NE = new Coordinate(50, -0.5);
    final Coordinate bbox_SW = new Coordinate(40, -1.0);

    final ArrayList<Coordinate> points = new ArrayList<>();
    points.add(new Coordinate(-10, -10));
    points.add(new Coordinate(-10, 10));
    points.add(new Coordinate(10, 10));
    points.add(new Coordinate(10, -10));
    points.add(new Coordinate(-10, -10));
    
    Geometry shape = gf.createPolygon(new LinearRing(new CoordinateArraySequence(points
        .toArray(new Coordinate[points.size()])), gf), null);    
    System.out.println("Point within Polygon: "+ point.within(shape));  
    
    shape = createCircle( 54.1, 0.5, 5);    
    System.out.println("Point within Circle: "+ point.within(shape));  
    
    Envelope env = new Envelope(bbox_NE, bbox_SW);    
    System.out.println("Point within Box: "+ env.contains(target));  
    
    
 //##### AWT ############
//    final Polygon polygon = new Polygon();
//    polygon.addPoint(-10, -10);
//    polygon.addPoint(-10, 10);
//    polygon.addPoint(10, 10);
//    polygon.addPoint(10, -10);
//
//    System.out.println(polygon.contains(50, 50));
      
    
   //AWT   
//double xCoord[] = {1,2,3,5,9,-5};
//double yCoord[] = {18,-32,1,100,-100,0};
//
//Path2D myPolygon = new Path2D.Double();
//
////Here you append all of your points to the polygon
//for(int i = 0; i < xCoord.length; i++) {
//    myPolygon.moveTo(xCoord[i], yCoord[i]);
//}
//myPolygon.closePath();
//
////Now we want to know if the point x, y is inside the Polygon:
//double x=0.0; //The x coord
//double y=0.0; //The y coord
//System.out.println(myPolygon.contains(x,y));
    
    


  }
  
  public static Geometry createCircle(double x, double y, final double RADIUS) {
    GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
    shapeFactory.setNumPoints(32);
    shapeFactory.setCentre(new Coordinate(x, y));
    shapeFactory.setSize(RADIUS * 2);
    return shapeFactory.createCircle();
}

}
