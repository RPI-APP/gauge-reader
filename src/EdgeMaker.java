import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.apache.commons.math3.stat.regression.SimpleRegression;

public class EdgeMaker implements Tool {
	
	private BetterWebcamPanel panel;
	
	private ArrayList<Point2D.Double> points = null;
	private MainGui gui;
	
	public EdgeMaker(MainGui gui, BetterWebcamPanel panel)
	{
		this.panel = panel;
		this.gui = gui;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Point2D.Double p = Utils.from(e.getPoint());
		
		points.add(panel.screenToRaw(p));
	}

	@Override
	public void begin() {
		this.points = new ArrayList<>();
	}

	@Override
	public void complete() {
		SimpleRegression regression = new SimpleRegression();
		for (Point2D.Double pt : points) {
			regression.addData(pt.x, pt.y);
		}
		Line l = new Line(regression.getSlope(), regression.getIntercept());
		this.gui.getData().edge = l;
		System.out.println(l);
	}

	@Override
	public String getToolName() {
		return "Edge";
	}

	@Override
	public String getInstructions() {
		return "Click a bunch of pts in straight line to establish what straight means";
	}

}
