import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.io.Serializable;

public final class CalibrationPoint implements Serializable
{	
	private static final long serialVersionUID = 1L;
	
	public final Point2D.Double point;
	public final double value;
	
	public CalibrationPoint(Double point, double value)
	{
		this.point = point;
		this.value = value;
	}

	public Point2D.Double getPoint()
	{
		return point;
	}

	public double getValue()
	{
		return value;
	}

}
