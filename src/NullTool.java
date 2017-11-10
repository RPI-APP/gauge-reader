import java.awt.event.MouseEvent;

public class NullTool implements Tool {

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void begin() {
	}

	@Override
	public void complete() {
	}

	@Override
	public String getToolName() {
		return "Apply";
	}

	@Override
	public String getInstructions() {
		return null;
	}

}
