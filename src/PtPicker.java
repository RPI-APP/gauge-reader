import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class PtPicker implements Tool
{	
	private BetterWebcamPanel panel;
	private MainGui gui;
	private PtType ptType;
	
	public PtPicker(MainGui gui, BetterWebcamPanel panel, PtType ptType)
	{
		this.panel = panel;
		this.gui = gui;
		this.ptType = ptType;
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		Point2D.Double p = panel.screenToRaw(Utils.from(e.getPoint()));
		
		GlobalData data = gui.getData();
		
		switch (ptType)
		{
			case GaugePt:
				data.gaugeSampleLinePt = p;
				break;
			case StartPt:
				data.startPoint = p;
				break;
			case EndPt:
				data.endPoint = p;
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
		return ptType.toString();
	}

	@Override
	public String getInstructions() {
		switch (ptType)
		{
			case GaugePt:
				return "Click a pt along the imaginary line parallel to the edge along which we will read colors";
			case StartPt:
				return "Click the lowest value you will want to measure";
			case EndPt:
				return "Click the highest value you will want to measure";
		}
		return "Oh Canada, my home an native landdd";
	}
	
	public enum PtType {
		GaugePt, StartPt, EndPt
	}
}
