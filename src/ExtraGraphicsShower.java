import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import com.github.sarxos.webcam.WebcamImageTransformer;

public class ExtraGraphicsShower implements WebcamImageTransformer {

	private GlobalData data;
	private BetterWebcamPanel panel;
	private PrintWriter out;
	
	public ExtraGraphicsShower(GlobalData data, BetterWebcamPanel panel)
	{
		this.data = data;
		this.panel = panel;
		
		// TODO move this elsewhere
		Socket socket = null;
	    try {
	        socket = new Socket("127.0.0.1", 64502);
	        OutputStream outstream = socket.getOutputStream(); 
	        this.out = new PrintWriter(outstream);
	        System.out.println("Connected?");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	@Override
	public BufferedImage transform(BufferedImage image)
	{
		// TODO: This isn't really the kosher place to be setting this...
		data.latestImage = Utils.copyImage(image);
		
		Graphics2D g2 = image.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		int h = image.getHeight();
		int w = image.getWidth();
		
		Line edge = data.edge;
		
		Point2D.Double mouse = data.mousePos;
		
		// crosshairs
		g2.setColor(Color.GRAY);
		g2.drawLine(0, (int) mouse.y, w, (int) mouse.y);
		g2.drawLine((int) mouse.x, 0, (int) mouse.x, h);
		
		// calc'd position
		try {
			Line where = edge.perpendicularThru(data.whereWeAt());
			g2.setColor(Color.RED);
			g2.drawLine((int) where.xAt(0), 0, (int) where.xAt(h), h);
		} catch (Exception e) {
		}
		
		// Send to the db
		// TODO: yah, this should be somewhere else
		double actualValue = data.calculateValue();
		System.out.println("Sending");
		out.print(actualValue + "" + (char) 0x1c + "669167de-c593-11e7-971b-6c0b843e9461" + (char) 0x1e);
		out.flush();
		
		// Show debug info
		panel.setDebugInfo("Mouse value: " + data.calculateValue(mouse) +
				"\nActual value: " + actualValue);
		
		// alignment lines
		if (edge != null) {
			g2.setColor(Color.BLUE);
			g2.drawLine((int) edge.xAt(0), 0, (int) edge.xAt(h), h);
			
			Point2D.Double sample = data.gaugeSampleLinePt;
			if (sample != null) {
				Line sampled = edge.parallelThru(sample);
				
				g2.setColor(Color.GREEN);
				g2.drawLine((int) sampled.xAt(0), 0, (int) sampled.xAt(h), h);
			}
		}
		
		return image;
	}

}
