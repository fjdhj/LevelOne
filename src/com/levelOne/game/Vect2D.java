package com.levelOne.game;

import java.util.List;

public class Vect2D  extends Point2D{

	public Vect2D() {
		super();
	}
	
	public Vect2D(double x, double y) {
		super(x, y);
	}
	
	@Override
	public String toString() {
		return "Vect(" + getX() + "; " + getY()+ ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vect2D)
			return super.equals(obj);
		
		return false;
	}	

	public double getNorm() {
		return Math.sqrt(getX() * getX() + getY() * getY());
	}
	
	public Vect2D getUnitaryVect() {
		double norm = getNorm();

		if (norm <= 0.001 && norm >= -0.001)
			return new Vect2D(0, 0);
		
		return new Vect2D(getX() / norm, getY() / norm);
	}
	
	/**
	 * Scale the vector by a factor
	 * @param factor The factor to scale the vector
	 */
	public void scale(double factor) {
		setX(getX() * factor);
		setY(getY() * factor);
	}
	
	/**
	 * Sum a vector with another Vector
	 * @param vect The vector to add
	 */
	public void add(Vect2D vect) {
		setX(getX() + vect.getX());
		setY(getY() + vect.getY());
	}
	
	/**
	 * Sum a list of vectors with the current vector
	 * @param vects The list of vectors to add
	 */
	public void add(List<Vect2D> vects) {
		for (Vect2D vect : vects)
			add(vect);
	}
	
	/**
	 * Sum a list of vectors with the current vector
	 * @param vects The list of vectors to add
	 */
	public void add(Vect2D... vects) {
		for (Vect2D vect : vects)
			add(vect);
	}
}
