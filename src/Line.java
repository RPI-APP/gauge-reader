import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class Line implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public final double m;
	public final double b;
	
	public Line(double m, double b)
	{
		this.m = m;
		this.b = b;
	}
	
	/**
	 * Finds all the integer points along this line between the approximate points start and end
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Point> getIntegerPoints(Point2D.Double start, Point2D.Double end)
	{
		ArrayList<Point> result = new ArrayList<>();
		
		// How much we need to move x to get an increase of 1 along the line
		double deltaX = Math.sqrt(1.0 / (1.0 + m * m));
		
		double startX = start.x;
		double endX = end.x;
		boolean flippy = false;
		if (startX > endX) {
			deltaX = -deltaX;
			flippy = true;
		}
		
		for (double x = startX; (!flippy ? x <= endX : x >= endX); x += deltaX) {
			double y = this.valueAt(x);
			
			int ix = (int) Math.round(x);
			int iy = (int) Math.round(y);
			
			result.add(new Point(ix, iy));
		}
		
		return result;
	}
	
	public Line parallelThru(Point2D.Double p)
	{
		return new Line(this.m, p.y - this.m * p.x);
	}
	
	public Line perpendicularThru(Point2D.Double p)
	{
		return new Line(-1.0 / this.m, p.y + 1.0 / this.m * p.x);
	}
	
	public Point2D.Double intersection(Line l)
	{
		double m1 = this.m;
		double m2 = l.m;
		double b1 = this.b;
		double b2 = l.b;
		
		double x = (b2 - b1) / (m1 - m2);
		double y = valueAt(x);
		
		return new Point2D.Double(x, y);
	}
	
	/**
	 * Finds the point along this line which is perpendicular to another point
	 * @param p
	 * @return
	 */
	public Point2D.Double alongPerpendicular(Point2D.Double p)
	{
		Line perpen = perpendicularThru(p);
		return intersection(perpen);	
	}
	
	public double distanceAlongPerpendicular(Point2D.Double p)
	{
		return Utils.length(alongPerpendicular(p));
	}
	
	public double valueAt(double x)
	{
		return m * x + b;
	}
	
	public double xAt(double y)
	{
		return (y - b) / m;
	}

	@Override
	public String toString()
	{
		return "Line [" + m + " x + " + b + "]";
	}

}
