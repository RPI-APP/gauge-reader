import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class ColorPicker implements Tool {
	
	private BetterWebcamPanel panel;
	private MainGui gui;
	private ColorVariable colorVar;
	
	public ColorPicker(MainGui gui, BetterWebcamPanel panel, ColorVariable colorVar)
	{
		this.panel = panel;
		this.gui = gui;
		this.colorVar = colorVar;
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		Point2D.Double p = panel.screenToRaw(Utils.from(e.getPoint()));
		
		GlobalData data = gui.getData();
		Color c = new Color(data.latestImage.getRGB((int) p.x, (int) p.y));
		
		System.out.println(c);
		
		switch (colorVar)
		{
			case Marker:
				data.markerColor = c;
				break;
			case Neutral:
				data.neutralColor = c;
				break;
		}
	}

	@Override
	public void begin() {
	}

	@Override
	public void complete() {
	}

	@Override
	public String getToolName() {
		return colorVar.toString();
	}

	@Override
	public String getInstructions() {
		return "Click the color which is " + colorVar.toString();
	}

	public enum ColorVariable {
		Marker, Neutral
	}
}
