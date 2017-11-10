import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class CalibrationPtPicker implements Tool
{	
	private BetterWebcamPanel panel;
	private MainGui gui;
	private ArrayList<CalibrationPoint> calPts;
	
	public CalibrationPtPicker(MainGui gui, BetterWebcamPanel panel)
	{
		this.panel = panel;
		this.gui = gui;
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		String resultStr = JOptionPane.showInputDialog(gui, "What value does this point have?");
		
		double result;
		try {
			result = Double.parseDouble(resultStr);
		} catch (NumberFormatException e1) {
			JOptionPane.showMessageDialog(gui, "ARGH INVALID INPUT WTF: " + resultStr);
			return;
		}
		
		Point2D.Double p = panel.screenToRaw(Utils.from(e.getPoint()));
		
		this.calPts.add(new CalibrationPoint(p, result));
	}

	@Override
	public void begin()
	{
		calPts = new ArrayList<>();
	}

	@Override
	public void complete()
	{
		GlobalData data = gui.getData();
		data.calibrationPoints = this.calPts;
	}

	@Override
	public String getToolName()
	{
		return "CalPts";
	}

	@Override
	public String getInstructions()
	{
		return "Pick some points on the gauge and say what value they have";
	}
}
