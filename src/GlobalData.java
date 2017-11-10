import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

public class GlobalData implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public transient volatile BufferedImage latestImage;
	public transient volatile Point2D.Double mousePos = new Point2D.Double();
	
	/**
	 * The color of the thing which marks the position
	 */
	public Color markerColor = Color.BLACK;
	
	/**
	 * The color of the background
	 */
	public Color neutralColor = Color.WHITE;
	
	/**
	 * Represents a straight line parallel to the gauge
	 */
	public Line edge;
	
	/**
	 * Represents a single point on the color sampled line parallel to
	 * the edge
	 */
	public Point2D.Double gaugeSampleLinePt;
	
	/**
	 * Points on the gauge and their values
	 */
	public ArrayList<CalibrationPoint> calibrationPoints;
	
	/**
	 * The degree of the polynomial to use for mathing the actual value
	 * TODO customize
	 */
	public int polynomialDegree = 2;
	
	/**
	 * TODO customize
	 */
	public double alpha = 10;
	
	public double beta = 0.1;
	
	// roughly where the gauge starts and ends
	public Point2D.Double startPoint;
	public Point2D.Double endPoint;
	
	public Line getGaugeLine()
	{
		if (edge == null || gaugeSampleLinePt == null) {
			return null;
		}
		return edge.parallelThru(gaugeSampleLinePt);
	}
	
	/**
	 * Figures out where we currently think the marker is
	 * @return
	 */
	public Point2D.Double whereWeAt()
	{
		try {
			Line gauge = this.getGaugeLine();
			List<Point> iPts = gauge.getIntegerPoints(startPoint, endPoint);
			
			double minScore = Double.POSITIVE_INFINITY;
			Point2D.Double bestPosition = null;
			for (int i = 0; i < iPts.size(); ++i) {
				for (int j = i; j < iPts.size(); ++j) {
					Point p1 = iPts.get(i);
					Point p2 = iPts.get(j);
					Color colorA, colorB;
					try {
						colorA = new Color(latestImage.getRGB(p1.x, p1.y));
						colorB = new Color(latestImage.getRGB(p2.x, p2.y));
					} catch (Exception e) {
						continue;
					}
					double diffA = Utils.colorDist(colorA, markerColor);
					double diffB = Utils.colorDist(colorB, neutralColor);
					double dist = j - i;
					double size = (i + j) / 2.0;
					// MAGIC FORMULA FOR IDENTIFYING BEST POSITION
					double score = this.alpha * dist + this.beta * (iPts.size() - size) + diffA + diffB;
					if (score < minScore) {
						minScore = score;
						bestPosition = Utils.midpoint(Utils.from(p1), Utils.from(p2));
					}
				}
			}
			return bestPosition;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Calculates the value at the current marker position
	 * @return
	 */
	public double calculateValue()
	{
		return calculateValue(whereWeAt());
	}
	
	/**
	 * Figures out the current value
	 * @param source where we think the marker currently is
	 * @return
	 */
	public double calculateValue(Point2D.Double source)
	{
		try {
			Line gauge = this.getGaugeLine();
			double valueDistance = gauge.distanceAlongPerpendicular(source);
			
			// Perform regression on gauge values
			final WeightedObservedPoints obs = new WeightedObservedPoints();
			for (CalibrationPoint cpt : calibrationPoints) {
					double cptDist = gauge.distanceAlongPerpendicular(cpt.point);
					obs.add(cptDist, cpt.value);
			}
			final PolynomialCurveFitter fitter = PolynomialCurveFitter.create(polynomialDegree);
			final double[] coeff = fitter.fit(obs.toList());
			
			double value = 0;
			for (int i = 0; i < coeff.length; ++i) {
				value += coeff[i] * Math.pow(valueDistance, i);
			}
			return value;
		} catch (Exception e) {
			return 0;
		}
	}
}
