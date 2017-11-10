import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class Utils {
	public static Point from(Point2D.Double p)
	{
		return new Point((int) Math.round(p.x), (int) Math.round(p.y));
	}
	
	public static Point2D.Double from(Point p)
	{
		return new Point2D.Double(p.x, p.y);
	}
	
	public static double colorDist(Color a, Color b)
	{
		double redDiff = a.getRed() - b.getRed();
		double greenDiff = a.getGreen() - b.getGreen();
		double blueDiff = a.getBlue() - b.getBlue();
		
		return Math.sqrt(redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff);
	}
	
	/**
	 * Deep copies buffred image from source
	 * https://stackoverflow.com/a/26894825
	 * @param source
	 * @return
	 */
	public static BufferedImage copyImage(BufferedImage bi)
	{
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	
	public static double length(Point2D.Double p)
	{
		return Math.sqrt(p.x * p.x + p.y * p.y);
	}
	
	public static Point2D.Double midpoint(Point2D.Double a, Point2D.Double b)
	{
		return new Point2D.Double((a.x + b.x) / 2.0, (a.y + b.y) / 2.0);
	}
}
