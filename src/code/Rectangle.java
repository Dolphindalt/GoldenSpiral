package code;

import java.awt.geom.Rectangle2D;

public class Rectangle extends Rectangle2D {

	private double x, y, width, height;
	
	public Rectangle(double x, double y, double width, double height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public Rectangle2D createIntersection(Rectangle2D arg0) {
		return null;
	}

	@Override
	public Rectangle2D createUnion(Rectangle2D arg0) {
		return null;
	}

	@Override
	public int outcode(double arg0, double arg1) {
		return 0;
	}

	@Override
	public void setRect(double arg0, double arg1, double arg2, double arg3) {
		
	}

	@Override
	public double getHeight() {
		return height;
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

}
