package client.GUI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


import javax.swing.JOptionPane;
import javax.swing.JPanel;



public class DrawListener extends MouseAdapter{
	
	private int x1, y1, x2, y2;
	
	private Color color = Color.BLACK;
	
	private Graphics2D pen;
	
	private String shapeType = "circle";
	
	private ClientGUI clientGUI;
	
	private DrawArea da;
	
	
	public DrawListener(DrawArea da, ClientGUI clientGUI, JPanel panel) {
		this.da = da;
		this.clientGUI = clientGUI;
		pen = (Graphics2D)panel.getGraphics();
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		
		x1 = e.getX();
		y1 = e.getY();
		
		if ("text".equals(shapeType)) {
			String input = JOptionPane.showInputDialog(
					"Please enter the text:");
			if(input != null) {
				pen.setColor(color);
				pen.drawString(input,x1,y1);
				Shape text = new Shape(x1, y1, input);
				text.setColor(color);
				text.setType("text");
				da.addShape(text);
				
				clientGUI.getClientController().synchronizeImage();
			}
		}
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		x2 = e.getX();
		y2 = e.getY();
		
		pen.setStroke(new BasicStroke(3));
		pen.setColor(color);
		if ("line".equals(shapeType)) {
			pen.drawLine(x1, y1, x2, y2);
		}else if ("rectangle".equals(shapeType)) {
			pen.drawRect(Math.min(x2, x1), Math.min(y2, y1), Math.abs(x2 - x1), Math.abs(y2 - y1));
		}else if ("circle".equals(shapeType)) {
			pen.drawOval(Math.min(x2, x1), Math.min(y2, y1), Math.abs(x2 - x1), Math.abs(y2 - y1));
		}else if ("triangle".equals(shapeType)) {
			int x3;
			if (x2 < x1) {
				x3 = x2 + 2 * (x1 - x2);
			} else {
				x3 = x2 - 2 * (x2 - x1);
			}
			pen.drawPolygon(new int[] {x1, x2, x3}, new int[] {y1, y2, y2}, 3);
			// x1 Math.abs(x2-x1)
		}
		Shape shape = new Shape(x1, y1, x2, y2);
		shape.setColor(color);
		shape.setType(shapeType);
		da.addShape(shape);
		
		clientGUI.getClientController().synchronizeImage();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		x2 = e.getX();
		y2 = e.getY();
		
		pen.setStroke(new BasicStroke(3));
		pen.setColor(color);
		if ("pen".equals(shapeType)) {
			pen.drawLine(x1, y1, x2, y2);
			x1 = x2;
			y1 = y2;
		}
		
	}

	public void setColor(Color color) {
		this.color = color;
	}


	public void setShape(String shapeType) {
		this.shapeType = shapeType;
	}

	
	
}
