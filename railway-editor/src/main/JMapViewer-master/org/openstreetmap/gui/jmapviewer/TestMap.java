package org.openstreetmap.gui.jmapviewer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;



public class TestMap {

    public static class MapPolyLine extends MapPolygonImpl {
        public MapPolyLine(List<? extends ICoordinate> points) {
            super(null, null, points);
        }

        @Override
        public void paint(Graphics g, List<Point> points) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(5));
//            Path2D path = buildPath(points);
//            g2d.draw(path);
            int[]xPoints = {1,10,20,30,40};
            int[]yPoints = {1,10,20,30,40};

            g2d.drawPolyline(xPoints, yPoints, 5);
            g2d.dispose();
        }

        private Path2D buildPath(List<Point> points) {
            Path2D path = new Path2D.Double();
            if (points != null && points.size() > 0) {
                Point firstPoint = points.get(0);
                path.moveTo(firstPoint.getX(), firstPoint.getY());
                for (Point p : points) {
                    path.lineTo(p.getX(), p.getY());    
                }
            } 
            return path;
        }
    }

    private static void createAndShowUI() {
        JFrame frame = new JFrame("Demo");
        JMapViewer viewer = new JMapViewer();

        List<Coordinate> coordinates = new ArrayList<Coordinate>();
        coordinates.add(new Coordinate(50, 10));
        coordinates.add(new Coordinate(52, 15));
        coordinates.add(new Coordinate(55, 15));

//        MapPolyLine polyLine = new MapPolyLine((List<? extends ICoordinate>) coordinates);
//        viewer.addMapPolygon(polyLine);

        frame.add(viewer);
        viewer.repaint();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowUI();
            }
        });
    }
}

