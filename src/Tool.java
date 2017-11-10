import java.awt.event.MouseEvent;

public interface Tool {
	
	void mouseReleased(MouseEvent e);
	
	void begin();
	void complete();
	
	String getToolName();
	
	String getInstructions();

}
