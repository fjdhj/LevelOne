package com.levelOne.game;

public class Point2D {
	
	private double x;
	private double y;

	public Point2D() {
		this(0, 0);
	}
	
	public Point2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Point2D) {
			Point2D pt = (Point2D) obj;
			return pt.getX() == getX() && pt.getY() == getY();
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return "Point(" + getX() + "; " + getY()+ ")";
	}
	
	public static double distance(Point2D pt1, Point2D pt2) {
		return Math.sqrt(Math.pow(pt2.getX() - pt1.getX(), 2) + Math.pow(pt2.getY() - pt1.getY(), 2));
	}
}
