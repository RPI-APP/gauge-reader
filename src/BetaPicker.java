import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class BetaPicker implements Tool
{	
	private BetterWebcamPanel panel;
	private MainGui gui;
	
	public BetaPicker(MainGui gui, BetterWebcamPanel panel)
	{
		this.panel = panel;
		this.gui = gui;
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
	}

	@Override
	public void begin()
	{
		String resultStr = JOptionPane.showInputDialog(gui,
				"What value does beta have? (currently " + this.gui.getData().beta + ")");
		
		double result;
		try {
			result = Double.parseDouble(resultStr);
		} catch (NumberFormatException e1) {
			JOptionPane.showMessageDialog(gui, "ARGH INVALID INPUT WTF: " + resultStr);
			return;
		}
		this.gui.getData().beta = result;
	}

	@Override
	public void complete()
	{
	}

	@Override
	public String getToolName()
	{
		return "beta";
	}

	@Override
	public String getInstructions()
	{
		return null;
	}
}
