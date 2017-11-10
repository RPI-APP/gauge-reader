import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.github.sarxos.webcam.Webcam;

/**
 * 
 * 
 * @author Daniel Centore
 *
 */
public class MainGui extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private Tool activeTool = new NullTool();
	
	private final GlobalData data;
	
	public MainGui(Webcam webcam)
	{
		super("Gauge Reader 3000 - Dan Centore");
		
		// TODO Deserialize
		data = new GlobalData();
		
		
		BetterWebcamPanel panel = new BetterWebcamPanel(webcam);
		panel.setFPSDisplayed(true);
		panel.setDisplayDebugInfo(true);
		panel.setImageSizeDisplayed(true);
		panel.setMirrored(true);
		
		webcam.setImageTransformer(new ExtraGraphicsShower(data, panel));
		
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
		    cursorImg, new Point(0, 0), "blank cursor");
		panel.setCursor(blankCursor);
		
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				MainGui.this.activeTool.mouseReleased(e);
			}
		});
		
		panel.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				MainGui.this.data.mousePos = panel.screenToRaw(Utils.from(e.getPoint()));
			}
		});
		
		JMenuBar menuBar = new JMenuBar();
		
		menuBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		menuBar.add(createTool(new NullTool()));
		menuBar.add(createTool(new EdgeMaker(this, panel)));
		menuBar.add(createTool(new ColorPicker(this, panel, ColorPicker.ColorVariable.Marker)));
		menuBar.add(createTool(new ColorPicker(this, panel, ColorPicker.ColorVariable.Neutral)));
		menuBar.add(createTool(new PtPicker(this, panel, PtPicker.PtType.GaugePt)));
		menuBar.add(createTool(new PtPicker(this, panel, PtPicker.PtType.StartPt)));
		menuBar.add(createTool(new PtPicker(this, panel, PtPicker.PtType.EndPt)));
		menuBar.add(createTool(new CalibrationPtPicker(this, panel)));
		menuBar.add(createTool(new AlphaPicker(this, panel)));
		menuBar.add(createTool(new BetaPicker(this, panel)));

		this.setJMenuBar(menuBar);
		
		menuBar.setVisible(true);
		this.add(panel);
		this.setResizable(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
	}
	
	private JMenuItem createTool(Tool tool)
	{
		JMenuItem potato = new JMenuItem(tool.getToolName());
		potato.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainGui.this.activeTool.complete();
				MainGui.this.activeTool = tool;
				tool.begin();
				if (tool.getInstructions() != null) {
					JOptionPane.showMessageDialog(MainGui.this, tool.getInstructions());
				}
			}
		});
		return potato;
	}

	public GlobalData getData()
	{
		return data;
	}
	
	public static void main(String[] args)
	{
		System.out.println("== Gauge Reader 3000 ==");
		System.out.println("by Daniel Centore");
		System.out.println();
		
		// Set look and feel
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		}
		
		// Select webcam
		List<Webcam> webcams = Webcam.getWebcams();
		if (webcams.isEmpty()) {
			System.out.println("No webcams found :(");
			return;
		}
		
		System.out.println("Webcams we found:");
		for (int i = 0; i < webcams.size(); ++i) {
			System.out.printf("[%d] %s\n", i, webcams.get(i).getName());
		}
		System.out.println();
		Scanner scan = new Scanner(System.in);
		int idx = -1;
		if (webcams.size() > 1) {
			while (idx < 0 || idx > webcams.size()) {
				System.out.print("Which one u want? ");
				String s = scan.nextLine();
				try {
					idx = Integer.parseInt(s);
				} catch (NumberFormatException e) {
				}
			}
		} else {
			System.out.println("Assuming u want the only available one.");
			idx = 0;
		}
		scan.close();
		
		Webcam webcam = webcams.get(idx);
		
		// Set resolution to the max supported
		Dimension maxSize = null;
		for (Dimension d : webcam.getViewSizes()) {
			if (maxSize == null || maxSize.height * maxSize.width < d.height * d.width) {
				maxSize = d;
			}
		}
		webcam.setViewSize(maxSize);
		
		// Make the gui
		MainGui mainGui = new MainGui(webcam);
		mainGui.setVisible(true);
	}

}
