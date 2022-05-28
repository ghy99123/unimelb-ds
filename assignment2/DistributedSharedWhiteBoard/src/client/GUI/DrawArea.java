package client.GUI;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;


public class DrawArea extends JPanel{
	
	private ArrayList<Shape> shapeList;
	
	private BufferedImage WbImage;
	
	public DrawArea() {
		shapeList = new ArrayList<Shape>();
	}
	
	// Clear all the shapes on the white board.
	public void clearAll() {
		this.WbImage = null;
		this.shapeList = new ArrayList<Shape>();
		repaint();
	}
	
	public BufferedImage getCanvasImage() {
		Dimension size = this.getSize();
		BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_BGR);
		Graphics2D g = image.createGraphics();
        this.paintComponent(g);
        g.dispose();
		return image;
	}
	
	public void load(BufferedImage WbImage) {
		
		clearAll();
	
		this.WbImage = WbImage;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (WbImage != null) {
			g.drawImage(WbImage, 0, 0, null);
		}
		for (Shape shape: shapeList) {
			Graphics2D g1 = (Graphics2D) g;
			shape.draw(g1);
		}
	}
	
	
	
	public void addShape(Shape shape) {
		shapeList.add(shape);
	}

}
